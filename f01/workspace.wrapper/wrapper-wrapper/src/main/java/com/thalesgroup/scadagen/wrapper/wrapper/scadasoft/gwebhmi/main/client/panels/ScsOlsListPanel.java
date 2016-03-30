package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.CaptionPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.event.GDGCounterChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsOlsListPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.presenter.WrapperScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.view.ScsGenericDataGridView;

/**
 * A widget displaying an alamm list panel.
 */
public class ScsOlsListPanel extends ResizeComposite implements IClientLifeCycle, GDGCounterChangeEvent.GDGCounterChangeEventHandler {

    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private HasWidgets mainPanel_;

    /**
     * s Client presenter of this alarm list widget
     */
    private WrapperScsAlarmDataGridPresenterClient gridPresenter_;

    /**
     * Bus used to subscribe to and publish alarm-related events
     */
    private EventBus eventBus_;

    /**
     * Used to remove event handler
     */
    private List<HandlerRegistration> handlerRegistrations_;

    /**
     * The alarm list context menu
     */
    private ScsOlsListPanelMenu contextMenu_;

    /**
     * Include or not the banner in a CaptionPanel
     */
    private final boolean withCaption_;

    /**
     * The configuration id of the datagrid
     */
    private final String listConfigId_;

    /**
     * The view that represents the alarm datagrid
     */
    private ScsGenericDataGridView gridView_;

    private boolean isTerminated_ = false;
    
    /**
     * To color the line according to the severity (for instance)
     */
    private static final String EVENT_CSS_SEVERITY_PREFIX = "event_";


    /**
     * To get the label of the list for the panel caption
     */
    private static final String DICTIONATY_CAPTION_SUFFIX = "_caption";

    /**
     * Builds an alarm list panel which uses a given event bus.
     *
     * @param eventBus
     *            bus used to subscribe to and publish alarm-related events
     * @param listConfigId
     *            the id of the datagrid configuration
     * @param withCaption
     *            display or not a caption with the name of the alarm list
     */
    public ScsOlsListPanel(final EventBus eventBus, String listConfigId, boolean withCaption, Set<FilterSetEvent> filterSet) {

        eventBus_ = eventBus;
        withCaption_ = withCaption;
        listConfigId_ = listConfigId;
        initComponents();
        initPresenter(filterSet);
        initWidget((Widget) mainPanel_);
    }

    public boolean isTerminated() {
        return isTerminated_;
    }

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter(Set<FilterSetEvent> filterSet) {
        if (listConfigId_ != null && gridView_ != null && eventBus_ != null && contextMenu_ != null) {
            gridPresenter_ = new WrapperScsAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus_, filterSet);
            gridPresenter_.setSelectionModel(new MultipleSelectionModel());
            gridPresenter_.setMenu(contextMenu_);

            initHandler();
        } else {
            s_logger.error(ScsOlsListPanel.class.getName()
                    + " initPresenter : listConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
    }

    private void initHandler() {
        if (eventBus_ != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
        }
    }

    /**
     * Initialize and create all needed components
     */
    private void initComponents() {
        contextMenu_ = new ScsOlsListPanelMenu(eventBus_);
        initDataGridView();
        initMainPanel();
    }

    /**
     * Create the main container with a Caption or not
     */
    private void initMainPanel() {
        if (withCaption_) {
            mainPanel_ = new CaptionPanel();
            final String captionLabel = Dictionary.getWording(listConfigId_ + DICTIONATY_CAPTION_SUFFIX);
            ((CaptionPanel) mainPanel_).setCaption(captionLabel);

        } else {
            mainPanel_ = new DockLayoutPanel(Unit.PX);
        }

        // Create datagrid container
        DockLayoutPanel containerPanel = new DockLayoutPanel(Unit.PX);
        Widget gridWidget = gridView_.asWidget();
        gridWidget.setVisible(true);

        // Add containers in the main panel
        containerPanel.add(gridWidget);
        mainPanel_.add(containerPanel);
    }

    /**
     * Create the datagrid view and customize the CSS rules
     */
    private void initDataGridView() {
        gridView_ = new ScsGenericDataGridView();
        // Customize CSS class according to the alarm state
        gridView_.setRowStyles(new RowStyles<EntityClient>() {

            @Override
            public String getStyleNames(EntityClient row, int rowIndex) {
            	
            	String CSS_EVENT_		= "CSS_EVENT";
            	            	
                String _SUPER_CRITICAL	= "_SUPER_CRITICAL";
                String _CRITICAL		= "_CRITICAL";
                String _LESS_CRITIICAL	= "_LESS_CRITIICAL";
                String _EVENT			= "_EVENT";
                
                String strCssResult		= CSS_EVENT_;
            	
                logger.log(Level.FINE, "getStyleNames Begin");
				logger.log(Level.FINE, "getStyleNames rowIndex["+rowIndex+"]");
				
                if (row != null) {
                	
                	AttributeClientAbstract<Integer> severity = null;
                    try {
                    	severity = row.getAttribute("severity");
                    } catch ( ClassCastException e) {
                    	logger.log(Level.FINE, "getStyleNames ClassCastException e["+e.toString()+"]");
                    }                    	
                	
                    for (String attname : row.attributeNames()) {
                    	logger.log(Level.FINE, "getStyleNames attname["+attname+"]");
                        AttributeClientAbstract<Object> att = row.getAttribute(attname);
                        if (!att.isValid()) {
                        	logger.log(Level.FINE, "getStyleNames !att.isValid()");
                            return "gdg_invalid";
                        }
                    }
                    if ( null != severity ) {
                    	if ( severity.isValid() ) {
                    		
                    		int iValue = severity.getValue();
                    		
                    		switch ( iValue ) {
                    		case 4:
                    			strCssResult += _SUPER_CRITICAL;
                    			break;
                    		case 3:
                    			strCssResult += _CRITICAL;
                    			break;
                    		case 2:
                    			strCssResult += _LESS_CRITIICAL;
                    			break;
                    		case 1:
                    		case 0:
                    			strCssResult += _EVENT;
                    			default:
                    		break;
                    		}
                    		
                    		logger.log(Level.SEVERE, "getStyleNames rowIndex["+rowIndex+"] iValue["+iValue+"] => strCssResult["+strCssResult+"]");
                    		
                    		return strCssResult;
                    		
                    		//CSS_EVENT_SUPER_CRITICAL
                    		//CSS_EVENT_CRITICAL
                    		//CSS_EVENT_LESS_CRITIICAL
                    		//CSS_EVENT_EVENT

                    	} else {
                    		logger.log(Level.FINE, "getStyleNames severity.isValid()");
                    	}
                    } else {
                    	logger.log(Level.FINE, "getStyleNames severity is null");
                    }
                } else {
                	logger.log(Level.FINE, "getStyleNames row is null");
                }

                logger.log(Level.FINE, "getStyleNames End");
                return "gdg_normal";
            }
        });
    }

    @Override
    public void onResize() {
        super.onResize();
        gridView_.onResize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        isTerminated_ = true;
        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        try {
            gridPresenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
            s_logger.error("Error while trying to terminate the Alarm List Panel.", e);
        }
    }
    
    private static Logger logger = Logger.getLogger(ScsOlsListPanel.class.getName());
    private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    public Integer getCounter(String key) { 
    	return this.hashMap.get(key); 
    }
    private String [] counterNames;
    public void setCounterNames(String [] counterNames) {
    	logger.log(Level.SEVERE, "setCounterNames counterNames");
    	for(String s: counterNames) {
    		logger.log(Level.SEVERE, "setCounterNames s["+s+"]");
    	}
    	this.counterNames = counterNames;
    }
    private WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent;
	public void setWrapperScsOlsListPanelEvent(WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent) { 
		this.wrapperScsOlsListPanelEvent = wrapperScsOlsListPanelEvent; 
	}
	@Override
	public void onCounterChange(GDGCounterChangeEvent event) {
        if (event.getSource() == gridPresenter_) {
        	logger.log(Level.SEVERE, "onCounterChange");
        	try {
	        	Iterator<Entry<String, Integer>> iter = hashMap.entrySet().iterator();
	        	while (iter.hasNext()) {
	        	    Entry<String, Integer> entry = iter.next();
	        	    String key = entry.getKey();
	        	    Integer value = entry.getValue();
	        	    logger.log(Level.SEVERE, "onCounterChange key["+key+"] value["+value+"]");
	        	    if ( null != value ) hashMap.put(key, value);
	        	} 
	        	
	            if ( null != wrapperScsOlsListPanelEvent ) {
	            	for ( int i = 0 ; i < counterNames.length ; ++i ) {
	                	String n = counterNames[i];
	                    Integer v = event.getValues().get(n);
	                    logger.log(Level.SEVERE, "onCounterChange n["+n+"] v["+v+"]");
	                    if (null != v) wrapperScsOlsListPanelEvent.valueChanged(n, v);
	            	}            	
	            } else {
	            	logger.log(Level.SEVERE, "onCounterChange wrapperScsOlsListPanelEvent is NULL");
	            }
        	} catch (Exception e ) {
        		logger.log(Level.SEVERE, "onCounterChange Exception on onCounterChange["+e.toString()+"]");
        	}
        }
	}
}
