package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.presenter.WrapperScsAlarmDataGridPresenterClient;
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
    public ScsAlarmListPanel(final EventBus eventBus, String listConfigId, boolean withAction, boolean withCaption, boolean withAck, Set<FilterSetEvent> filterSet) {

        eventBus_ = eventBus;
        withAction_ = withAction;
        withAck_ = withAck;
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
            	
            	String strCssResult		= "gdg_invalid";
            	
            	String strPriority		= "priority";
            	String strState			= "state";
            	
            	String priorityValue	= null;
            	String stateValue	= null;
            	
            	if ( null == row ) return strCssResult;
            	
                AttributeClientAbstract<String> priority	= row.getAttribute(strPriority);
                AttributeClientAbstract<String> state		= row.getAttribute(strState);
                
                if (!priority.isValid()) return strCssResult;
                if (!state.isValid()) return strCssResult;
                
                if ( null != priority && priority.isValid() ) {
                	priorityValue = priority.getValue();	
                }
                
                if ( null != strState && state.isValid() ) {
                	stateValue = state.getValue();
                }
                
                strCssResult = "CSS_OLS_LIST_PANEL_SEVERITY" + "_" + priorityValue + "_" + stateValue;
                
                logger.log(Level.FINE, "getStyleNames rowIndex["+rowIndex+"] priorityValue["+priorityValue+"] stateValue["+stateValue+"] => strCssResult["+strCssResult+"]");
                
                return strCssResult;
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

    private WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent;
	public void setWrapperScsAlarmListPanelEvent(WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent) { 
		this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent; 
	}
	private static Logger logger = Logger.getLogger(ScsAlarmListPanel.class.getName());
    @Override
    public void onCounterChange(GDGCounterChangeEvent event) {
    	
        if (event.getSource() == gridPresenter_) {
        	logger.log(Level.FINE, "onCounterChange Begin");
        	try {
	        	Map<String, Integer> maps = event.getValues();
	        	Set<String> names = maps.keySet();
	        	Iterator<String> iter = names.iterator();
	        	while (iter.hasNext()) {
	        		String name = iter.next();
	        		if ( null != name ) {
	        			Integer value = maps.get(name);
	        			logger.log(Level.FINE, "onCounterChange key["+name+"] value["+value+"]");
	        			if (null != wrapperScsAlarmListPanelEvent) wrapperScsAlarmListPanelEvent.valueChanged(name, String.valueOf(value));
	        		}
	        	}
        	} catch (Exception e ) {
        		logger.log(Level.FINE, "onCounterChange Exception on onCounterChange["+e.toString()+"]");
        	}
        	logger.log(Level.FINE, "onCounterChange End");
    	}
    }
    
    public void ackVisible() {
    	if ( null != gridView_ )	gridView_.ackVisible();
    }
    
    public void ackVisibleSelected() {
    	if ( null != gridView_ )	gridView_.ackVisibleSelected();
    }
}
