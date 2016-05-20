package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationActivationEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.event.GDGCounterChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.CoccAlarmDataGridPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.scadasoft.gwebhmi.main.client.view.ScsGenericDataGridView;

/**
 * A widget displaying an alarm list panel.
 */
public class CoccAlarmListPanel extends ResizeComposite implements IClientLifeCycle, GDGCounterChangeEvent.GDGCounterChangeEventHandler {


    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private DockLayoutPanel mainPanel_;

    /**s
     * Client presenter of this alarm list widget
     */
    private CoccAlarmDataGridPresenterClient gridPresenter_;

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
    private CoccAlarmListPanelMenu contextMenu_;
    
    /**
     * True if the list can acknowledge entities in the list
     */
    private final boolean withAck_;
    
    /**
     *  Include or not the banner in a CaptionPanel
     */
    private final boolean withCaption_;
       
    /**
     *  Include or not the button to show banner in full screen
     */
    private boolean withFullListBtn_ = false;
    
    /**
     * The configuration id of the datagrid
     */
    private final String listConfigId_;
    
    /**
     * The view that represents the alarm datagrid
     */
    private ScsGenericDataGridView gridView_;

    private Label counterLabel_ = null;
    
    private Label totalCounterLabel_ = null;
    
    private int notPresentCnt_ = 0;
    
    private int mediumPriorityCnt_ = 0;
    
    private int highPriorityCnt_ = 0;
    
    private int criticalPriorityCnt_ = 0;
    
    private int totalAlarmCnt_ = 0;
    
    private boolean isTerminated_ = false;
    
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
     * @param eventBus bus used to subscribe to and publish alarm-related events
     * @param listConfigId the id of the datagrid configuration
     * @param withCaption display or not a caption with the name of the alarm list
     * @param withAck display or not the acknowledge button and menu
     */
    public CoccAlarmListPanel(final EventBus eventBus,String listConfigId, boolean withCaption, boolean withAck, Set<FilterSetEvent> filterSet, boolean withFullListBtn, CoccAlarmListPanelMenu menu) {
        
        eventBus_ = eventBus;
        withAck_ = withAck;
        withCaption_ = withCaption;
        listConfigId_ = listConfigId;
        withFullListBtn_ = withFullListBtn;
        initComponents(menu);
        initPresenter(filterSet);
        initWidget((Widget)mainPanel_);
    }

    public boolean isTerminated() {
        return isTerminated_;
    }
    
    /**
     * Create and initialize the presenter with its view, selectionModel, context menu and Handlers
     */
    public void initPresenter(Set<FilterSetEvent> filterSet) {
        if(listConfigId_ != null && gridView_ != null && eventBus_ != null && contextMenu_ != null){
            gridPresenter_ = new CoccAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus_, filterSet);
            gridPresenter_.setSelectionModel(new MultipleSelectionModel());
            gridPresenter_.setMenu(contextMenu_);
            
            eventBus_.addHandler(GDGCounterChangeEvent.TYPE, this);
            
            initHandler();
        }else{
            s_logger.error(CoccAlarmListPanel.class.getName()+" initPresenter : listConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
    }
    
    public void initHandler(){
        if (eventBus_ != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
        } 
    }
    
    /**
     * Initialize and create all needed components
     */
    private void initComponents(CoccAlarmListPanelMenu menu){
        if (menu == null) {
            contextMenu_ =  new CoccAlarmListPanelMenu(eventBus_,withAck_);
        } else {
            contextMenu_ = menu;
        }
        initDataGridView();
        initMainPanel();
    }
    
    /**
     * Create the main container with a Caption or not
     */
    private void initMainPanel(){
        if(withCaption_){
            mainPanel_ = new CoccCaptionPanel();
            final String captionLabel = Dictionary.getWording(listConfigId_+DICTIONATY_CAPTION_SUFFIX);
            ((CoccCaptionPanel)mainPanel_).setCaption(captionLabel);
          
        }else{
            mainPanel_ = new DockLayoutPanel(Unit.PX);
        }
        //Create action panel
        DockLayoutPanel actionPanel = createActionPanel();
        
        //Create datagrid container
        DockLayoutPanel containerPanel  = new DockLayoutPanel(Unit.PX);
        Widget gridWidget = gridView_.asWidget();
        gridWidget.setVisible(true);
        
        //Add containers in the main panel
        containerPanel.addNorth(actionPanel, 25);
        containerPanel.add(gridWidget);
        mainPanel_.add(containerPanel);
    }
    
    /**
     * Create the datagrid view and customize the CSS rules
     */
    private void initDataGridView(){
        gridView_ = new ScsGenericDataGridView();
        // Customize CSS class according to the alarm state
        gridView_.setRowStyles(new RowStyles<EntityClient>() {
            
            @Override
            public String getStyleNames(EntityClient row, int rowIndex) {
                AttributeClientAbstract<String> severity =  row.getAttribute("priority");
                AttributeClientAbstract<String> state =  row.getAttribute("state");
                if (!state.isValid()) {
                    return "gdg_invalid";
                }
                
                return CSS_SEVERITY_PREFIX+severity.getValue()+" "+CSS_ALARM_ACK_PREFIX+state.getValue();
            }
        });
    }
    
    /**
     * Create a FlexTable with buttons to perform action on alarm (like "Ack Page" button)
     * @return the action buttons flextable
     */
    private DockLayoutPanel createActionPanel(){
        int lineNumber = 0;
        DockLayoutPanel actionPanel = new DockLayoutPanel(Unit.PX);
        
        FlexTable leftPanel = new FlexTable();
        
        FlexTable rightPanel = new FlexTable();
        
        if(withAck_){
            Button ackPage = new Button();
            ackPage.setText(Dictionary.getWording("alarmList_action_ack_page"));
            ackPage.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent event) {
                    gridView_.ackPage();
                }
            });
            leftPanel.setWidget(0, lineNumber++, ackPage);
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
        leftPanel.setWidget(0, lineNumber++, showContextMenu);
        
        counterLabel_ = new Label(Dictionary.getWording("notAckCount") + "   " + Dictionary.getWording( "notPresentCounter" ) + ": 0  " + Dictionary.getWording( "mediumPriorityCounter" ) + ": 0  " +
                        Dictionary.getWording( "highPriorityCounter" ) + ": 0  " + Dictionary.getWording( "criticalPriorityCounter" ) + ": 0");
        
        leftPanel.setWidget(0, lineNumber++, counterLabel_);
        
        totalCounterLabel_ = new Label(Dictionary.getWording("totalCounter") + ": 0");
        
        rightPanel.setWidget(0, lineNumber++, totalCounterLabel_);
        
        if (withFullListBtn_) {
            final Button showFullListButton = new Button ();
            
            showFullListButton.setText( Dictionary.getWording("alarmList_action_show_full_list") );
            
            showFullListButton.addClickHandler( new ClickHandler() {
    
                @Override
                public void onClick( ClickEvent event )
                {
                    final NavigationActivationEvent navigationEvent =
                                    new NavigationActivationEvent(ConfigurationConstantUtil.ALARM_LIST_BANNER_ID);
    
                    AppUtils.EVENT_BUS.fireEvent(navigationEvent);
                }
            });
            rightPanel.setWidget(0, lineNumber++, showFullListButton);
            actionPanel.addEast( rightPanel, 320 );
        } else {
            actionPanel.addEast( rightPanel, 200 );
        }

        actionPanel.add( leftPanel );
            
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

    @Override
    public void onCounterChange(GDGCounterChangeEvent event) {
        String label = Dictionary.getWording("notAckCount") + "   ";

        String counterId = listConfigId_ + "_" + "notPresentCounter";
        Integer npc = event.getValues().get(counterId);
        
        counterId = listConfigId_ + "_" + "mediumPriorityCounter";
        Integer mc = event.getValues().get(counterId);
        
        counterId = listConfigId_ + "_" + "highPriorityCounter";
        Integer hc = event.getValues().get(counterId);
        
        counterId = listConfigId_ + "_" + "criticalPriorityCounter";
        Integer cc = event.getValues().get(counterId);
        
        counterId = listConfigId_ + "_" + "totalCounter";
        Integer tc = event.getValues().get(counterId);
        
        if (npc != null) {
            notPresentCnt_ = npc;
        }
        label += Dictionary.getWording( "notPresentCounter" ) + ": " + notPresentCnt_;
        
        if (mc != null) {
            mediumPriorityCnt_ = mc;
        }
        label += "  " + Dictionary.getWording( "mediumPriorityCounter" ) + ": " + mediumPriorityCnt_;
        
        if (hc != null) {
            highPriorityCnt_ = hc;
        }
        label += "  " + Dictionary.getWording( "highPriorityCounter" ) + ": " + highPriorityCnt_;
        
        if (cc != null) {
            criticalPriorityCnt_ = cc;
        }
        label += "  " + Dictionary.getWording( "criticalPriorityCounter" ) + ": " + criticalPriorityCnt_;

        counterLabel_.setText(label);
         
        if (tc != null) {
            totalAlarmCnt_ = tc;
        }
        
        label = "  " + Dictionary.getWording( "totalCounter" ) + ": " + totalAlarmCnt_;

        totalCounterLabel_.setText(label);
    }
}
