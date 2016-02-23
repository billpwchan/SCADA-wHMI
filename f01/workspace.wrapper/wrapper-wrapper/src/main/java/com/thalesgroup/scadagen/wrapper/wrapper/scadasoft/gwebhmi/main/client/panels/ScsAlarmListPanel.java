package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.CaptionPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.event.GDGCounterChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.view.ScsGenericDataGridView;
/**
 * A widget displaying an alarm list panel.
 */
public class ScsAlarmListPanel extends ResizeComposite
        implements IClientLifeCycle, GDGCounterChangeEvent.GDGCounterChangeEventHandler {

    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private DockLayoutPanel mainPanel_;

    /**
     * Client presenter of this alarm list widget
     */
    private ScsAlarmDataGridPresenterClient gridPresenter_;

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
    private ScsAlarmListPanelMenu contextMenu_;
    
    /**
     * True if the list will show Action Bar
     */
    private final boolean withAction_;    

    /**
     * True if the list can acknowledge entities in the list
     */
    private final boolean withAck_;

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

    private Label counterLabel_ = null;

    private boolean isTerminated_ = false;

    /*
	private int m_cc = 0;
	private int m_hc = 0;
	private int m_mc = 0;
	private int m_lc = 0;
	*/

    /**
     * To color the line according to the severity (for instance)
     */
    private static final String CSS_SEVERITY_PREFIX = "alarm_";

    /**
     * To change the font of the line is the alarm is ack or not (for instance)
     */
    private static final String CSS_ALARM_ACK_PREFIX = "alarm_ack_";

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
     * @param withAck
     *            display or not the acknowledge button and menu
     */
    public ScsAlarmListPanel(final EventBus eventBus, String listConfigId, boolean withAction, boolean withCaption, boolean withAck) {

        eventBus_ = eventBus;
        withAction_ = withAction;
        withAck_ = withAck;
        withCaption_ = withCaption;
        listConfigId_ = listConfigId;
        initComponents();
        initPresenter();
        initWidget((Widget) mainPanel_);
    }

    public boolean isTerminated() {
        return isTerminated_;
    }

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter() {
        if (listConfigId_ != null && gridView_ != null && eventBus_ != null && contextMenu_ != null) {
            gridPresenter_ = new ScsAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus_);
            gridPresenter_.setSelectionModel(new MultipleSelectionModel());
            gridPresenter_.setMenu(contextMenu_);

            initHandler();
        } else {
            s_logger.error(ScsAlarmListPanel.class.getName()
                    + " initPresenter : listConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
    }

    private void initHandler() {
        if (eventBus_ != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
            handlerRegistrations_.add(eventBus_.addHandler(GDGCounterChangeEvent.TYPE, this));
        }
    }

    /**
     * Initialize and create all needed components
     */
    private void initComponents() {
        contextMenu_ = new ScsAlarmListPanelMenu(eventBus_, withAck_);
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
        // Create action panel
        FlexTable actionPanel = createActionPanel();

        // Create datagrid container
        DockLayoutPanel containerPanel = new DockLayoutPanel(Unit.PX);
        Widget gridWidget = gridView_.asWidget();
        gridWidget.setVisible(true);

        // Add containers in the main panel
        int actionHeight = 25;
        if ( ! withAction_ ) actionHeight = 25;
        actionPanel.setVisible(withAction_);
        containerPanel.addNorth(actionPanel, actionHeight);
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
                AttributeClientAbstract<String> severity = row.getAttribute("priority");
                AttributeClientAbstract<String> state = row.getAttribute("state");
                if (!state.isValid()) {
                    return "gdg_invalid";
                }
                
                if ( true ) {
                	
                String CSS_ALARM_		= "CSS_ALARM";
                
                String EVENT			= "LOW";
                String LESS_CRITIICAL	= "MEDIUM";
                String CRITICAL			= "HIGH";
                String SUPER_CRITICAL	= "CRITICAL";
                
                String YES1				= "NPA";
                String YES2				= "PA";
                String NO1				= "NPNA";
                String NO2				= "PNA";
                
                String _NA				= "_NA";
                String _A				= "_A";
                String _SUPER_CRITICAL	= "_SUPER_CRITICAL";
                String _CRITICAL		= "_CRITICAL";
                String _LESS_CRITICAL	= "_LESS_CRITICAL";
                String _EVENT			= "_EVENT";
                
                String strSeverity		= severity.getValue();
                String strState			= state.getValue();
                
                String strCssResult		= CSS_ALARM_;
                
                if ( 0 == strSeverity.compareTo(SUPER_CRITICAL) ) {
                	strCssResult += _SUPER_CRITICAL;
                } else if ( 0 == strSeverity.compareTo(CRITICAL) ) {
                	strCssResult += _CRITICAL;
                } else if ( 0 == strSeverity.compareTo(LESS_CRITIICAL) ) {
                	strCssResult += _LESS_CRITICAL;
                } else {
                	strCssResult += _EVENT;
                }
                
                if ( 0 == strState.compareTo(YES1) || 0 == strState.compareTo(YES2) ) {
                	strCssResult += _A;
                } else if ( 0 == strState.compareTo(NO1) || 0 == strState.compareTo(NO2) ) {
                	strCssResult += _NA;
                }
                
logger.log(Level.SEVERE, "getStyleNames rowIndex["+rowIndex+"] strSeverity["+strSeverity+"] strState["+strState+"] => strCssResult["+strCssResult+"]");
                
                return strCssResult;
                
	            } else {
	
	                return CSS_SEVERITY_PREFIX + severity.getValue() + " " + CSS_ALARM_ACK_PREFIX + state.getValue();
	                
	            }
                
                //CSS_SUPER_CRITICAL_NA
                //CSS_SUPER_CRITICAL_A
                //CSS_CRITICAL_NA
                //CSS_CRITICAL_A
                //CSS_LESS_CRITICAL_NA
                //CSS_LESS_CRITICAL_A
                //CSS_EVENT_NA
                //CSS_EVENT_A
                
                //dataGridSelectedRow
                //dataGridSelectedRowCell
                
            }
        });
    }

    /**
     * Create a FlexTable with buttons to perform action on alarm (like
     * "Ack Page" button)
     * 
     * @return the action buttons flextable
     */
    private FlexTable createActionPanel() {
        int lineNumber = 0;
        FlexTable actionPanel = new FlexTable();

        if (withAck_) {
            Button ackPage = new Button();
            ackPage.setText(Dictionary.getWording("alarmList_action_ack_page"));
            ackPage.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    gridView_.ackPage();
                }
            });
            actionPanel.setWidget(0, lineNumber++, ackPage);
        }
        final Button showContextMenu = new Button();

        showContextMenu.setText(Dictionary.getWording("alarmList_action_show_menu"));

        showContextMenu.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                contextMenu_.showRelativeTo(showContextMenu);
                contextMenu_.showRelativeTo(gridPresenter_.getSelectedEntities(), showContextMenu);
            }
        });
        actionPanel.setWidget(0, lineNumber++, showContextMenu);
        counterLabel_ = new Label("L: 0 M: 0 H: 0 C: 0");

        actionPanel.setWidget(0, lineNumber++, counterLabel_);

        return actionPanel;
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
    private WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent;
	public void setWrapperScsAlarmListPanelEvent(WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent) { 
		this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent; 
	}
	private static Logger logger = Logger.getLogger(ScsAlarmListPanel.class.getName());
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
	        	/*
	            String label = "";
	
	            Integer lc = event.getValues().get("lowPriorityCounter");
	            Integer mc = event.getValues().get("mediumPriorityCounter");
	            Integer hc = event.getValues().get("highPriorityCounter");
	            Integer cc = event.getValues().get("criticalPriorityCounter");
	
	            if (lc != null) {
	                m_lc = lc;
	            }
	
	            if (mc != null) {
	            	m_mc = mc;
	            } 
	
	            if (hc != null) {
	            	m_hc = hc;
	            }
	
	            if (cc != null) {
	            	m_cc = cc;
	            } 
	
	            label = "L:" + m_lc + " M:" + m_mc + " H:" + m_hc + " C:" + m_cc;
	            
	            counterLabel_.setText(label);
	            */
	            
	            if ( null != wrapperScsAlarmListPanelEvent ) {
	            	for ( int i = 0 ; i < counterNames.length ; ++i ) {
	                	String n = counterNames[i];
	                    Integer v = event.getValues().get(n);
	                    logger.log(Level.SEVERE, "onCounterChange n["+n+"] v["+v+"]");
	                    if (null != v) wrapperScsAlarmListPanelEvent.valueChanged(n, v);
	            	}            	
	            } else {
	            	logger.log(Level.SEVERE, "onCounterChange wrapperScsAlarmListPanelEvent is NULL");
	            }
        	} catch (Exception e ) {
        		logger.log(Level.SEVERE, "onCounterChange Exception on onCounterChange["+e.toString()+"]");
        	}
        }
    }
}
