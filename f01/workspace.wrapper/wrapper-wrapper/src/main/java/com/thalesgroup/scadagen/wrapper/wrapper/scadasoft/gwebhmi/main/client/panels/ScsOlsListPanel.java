package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        contextMenu_.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
			
			@Override
			public void onSelection(Set<HashMap<String, String>> entity) {
				if ( null != scsOlsListPanelMenuHandler ) {
					scsOlsListPanelMenuHandler.onSelection(entity);
				}
			}
		});
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
            	
            	String strCssResult		= "gdg_invalid";
            	
            	String strPriority		= "priority";
            	
            	String priorityValue	= null;
            	
            	if ( null == row ) return strCssResult;
            	
                AttributeClientAbstract<String> priority	= row.getAttribute(strPriority);
                
                if (!priority.isValid()) return strCssResult;
                
                if ( null != priority && priority.isValid() ) {
                	priorityValue = priority.getValue();	
                }
                
                strCssResult = "CSS_OLS_LIST_PANEL_SEVERITY" + "_" + priorityValue;
                
                logger.log(Level.FINE, "getStyleNames rowIndex["+rowIndex+"] priorityValue["+priorityValue+"] => strCssResult["+strCssResult+"]");
                
                return strCssResult;
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
    
    private WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent;
	public void setWrapperScsOlsListPanelEvent(WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent) { 
		this.wrapperScsOlsListPanelEvent = wrapperScsOlsListPanelEvent; 
	}
	@Override
	public void onCounterChange(GDGCounterChangeEvent event) {
        if (event.getSource() == gridPresenter_) {
        	logger.log(Level.SEVERE, "onCounterChange Begin");
        	try {
	        	Map<String, Integer> maps = event.getValues();
	        	Set<String> keys = maps.keySet();
	        	Iterator<String> iter = keys.iterator();
	        	while (iter.hasNext()) {
	        		String key = iter.next();
	        		if ( null != key ) {
	        			Integer value = maps.get(key);
	        			logger.log(Level.SEVERE, "onCounterChange key["+key+"] value["+value+"]");
	        			if (null != wrapperScsOlsListPanelEvent) wrapperScsOlsListPanelEvent.valueChanged(key, value);
	        		}
	        	}
        	} catch (Exception e ) {
        		logger.log(Level.SEVERE, "onCounterChange Exception on onCounterChange["+e.toString()+"]");
        	}
        	logger.log(Level.SEVERE, "onCounterChange End");
    	}
	}
	
    private ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler = null;
    public void setScsOlsListPanelMenuHandler(ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler) {
    	this.scsOlsListPanelMenuHandler = scsOlsListPanelMenuHandler;
    }
	
}
