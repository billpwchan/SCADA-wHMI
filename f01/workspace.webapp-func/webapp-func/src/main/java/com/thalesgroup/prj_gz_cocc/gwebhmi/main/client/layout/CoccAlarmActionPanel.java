
package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixConfigType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccAlarmListPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccAlarmListPanelMenu;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccMatrixPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ActionPanel;

/**
 * This class implements an action panel which allows to visualize a detailed situation view and send
 * commands.
 */
public class CoccAlarmActionPanel extends ResizeComposite implements IClientLifeCycle {

    /**
     * Bus used for events exchanged between widgets of this {@link ActionPanel}
     */
    private final EventBus actionPanelEventBus_;

    /**
     * Identifier of the matrix panel contained within this {@link ActionPanel}
     */
    protected final String matrixId_;
    
    /**
     * Identifier of the data grid contained within this {@link ActionPanel}
     */
    protected final String gridId_;

    /**
     * Panel used to vertically split the situation view and the object tree / info command panels
     */
    private SplitLayoutPanel outerSplitPanel_;
    
    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();


    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    private CoccMatrixPanel alarmDiagramPanel_ = null;
    
    private CoccAlarmListPanel alarmListPanelForListTab_ = null;
    
    private final String axisRowId_;
    
    private final String axisColId_;
    
    private final MatrixConfigType mxType_;
    
    private boolean isTerminated_ = false;

    /**
     * Constructor.
     *
     * @param matrixId identifier of the matrix panel contained within this {@link ActionPanel}
     * @param gridId identifier of the data grid panel contained within this {@link ActionPanel}
     * @param eventBus application bus into which {@link ActionPanel}'s events are forwarded
     */
    public CoccAlarmActionPanel(final String matrixId, final String gridId, final EventBus eventBus) {
     
    	matrixId_ = matrixId;
    	gridId_ = gridId;
        //appEventBus_ = eventBus;
        actionPanelEventBus_ = new MwtEventBus();
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();
        axisRowId_ = null;
        axisColId_ = null;
        mxType_ = MatrixConfigType.STATIC;

        createLayout();

        setupEventHandlers();

        initWidget(outerSplitPanel_);
    }
    
    public CoccAlarmActionPanel(final String matrixId, final String axisRowId, final String axisColId, final MatrixConfigType mxType, final String gridId, final EventBus eventBus) {
        
    	matrixId_ = matrixId;
    	gridId_ = gridId;
        //appEventBus_ = eventBus;
        actionPanelEventBus_ = new MwtEventBus();
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();
        axisRowId_ = axisRowId;
        axisColId_ = axisColId;
        mxType_ = mxType;

        createLayout();

        setupEventHandlers();

        initWidget(outerSplitPanel_);
    }
    
    public boolean isTerminated() {
        return isTerminated_;
    }

    public EventBus getActionPanelEventBus() {
        return actionPanelEventBus_;
    }
    
    /** CHECKSTYLE:OFF MagicNumberCheck HMI is magic */
    private void createLayout() {
        outerSplitPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        outerSplitPanel_.addStyleName("showcase-action-panel");
        
        // Extract lineID from gridId (e.g. alarmDiagram_GZ5_Subsystem)
        String [] tmpList = gridId_.split( "_" );
        if (tmpList.length > 2 && tmpList[1].length() == 3) {
            
            // Set filter event
            String criteria = Dictionary.getWording( "MCS_" + tmpList[1] );
            s_logger.debug( "CoccAlarmActionPanel criteria=" + criteria );
            
            StringFilterDescription filterdesc = new StringFilterDescription(StringFilterDescription.StringFilterTypes.CONTAINS, criteria );

            final FilterSetEvent filterEvent = new FilterSetEvent("scsalarmList_lineID_name", filterdesc);
            final Set<FilterSetEvent> filterSet = new HashSet<FilterSetEvent>();
            filterSet.add(filterEvent);
            addAlarmListTab(filterSet, false, null);
        }
        else {
            addAlarmListTab(null, false, null);
        }
        addAlarmDiagramTab();
    }

    public void addAlarmDiagramTab(){
        if (alarmDiagramPanel_ == null || alarmDiagramPanel_.isTerminated()){
            alarmDiagramPanel_  = new CoccMatrixPanel(AppUtils.EVENT_BUS, matrixId_, axisRowId_, axisColId_, mxType_, false);
        } else {
            alarmDiagramPanel_.initHandler();
        }
        outerSplitPanel_.add(alarmDiagramPanel_);
    }
    
    public void addAlarmListTab(Set<FilterSetEvent> filterSet, boolean withFullListBtn, CoccAlarmListPanelMenu menu){
        if(alarmListPanelForListTab_ == null || alarmListPanelForListTab_.isTerminated()){
        	alarmListPanelForListTab_ = new CoccAlarmListPanel(AppUtils.EVENT_BUS, gridId_, false, true, filterSet, withFullListBtn, menu);
        }else{
            alarmListPanelForListTab_.initHandler();
        }
        outerSplitPanel_.addSouth(alarmListPanelForListTab_, 300);
    }
    
    /** CHECKSTYLE:ON MagicNumberCheck HMI is magic */

    /**
     * Return the identifier of the matrix contained within this {@link ActionPanel}.
     *
     * @return the identifier of the matrix contained within this {@link ActionPanel}
     */
    public String getMatrixId() {
        return matrixId_;
    }
    
    /**
     * Return the identifier of the data grid contained within this {@link ActionPanel}.
     *
     * @return the identifier of the data grid contained within this {@link ActionPanel}
     */
    public String getGridId() {
        return gridId_;
    }
   
    /**
     * Return the matrix axisRowId contained within this {@link ActionPanel}.
     *
     * @return the matrix axisRowId contained within this {@link ActionPanel}
     */
    public String getAxisRowId() {
        return axisRowId_;
    }
    
    /**
     * Return the matrix axisColId contained within this {@link ActionPanel}.
     *
     * @return the matrix axisColId contained within this {@link ActionPanel}
     */
    public String getMatrixConfigType() {
        return axisColId_;
    }
    
    /**
     * Return the matrix axisColId contained within this {@link ActionPanel}.
     *
     * @return the matrix axisColId contained within this {@link ActionPanel}
     */
    public String getAxisColId() {
        return axisColId_;
    }

    /**
     * Register to both action panel bus and application bus in order to forward events.
     */
    private void setupEventHandlers() {
        // Publish alarm selection events to the application bus
        /*handlerRegistrations_.add(actionPanelEventBus_.addHandler(AlarmSelectionChangeEvent.TYPE,
            new AlarmSelectionEventHandler() {

                @Override
                public void onSelectionChange(final AlarmSelectionChangeEvent event) {
                    if (!event.containsEventBus(appEventBus_)) {
                        appEventBus_.fireEventFromSource(event, this);
                    }
                }
            }));

        // Publish alarm selection events to the application bus
        handlerRegistrations_.add(appEventBus_.addHandler(AlarmSelectionChangeEvent.TYPE,
            new AlarmSelectionEventHandler() {

                @Override
                public void onSelectionChange(final AlarmSelectionChangeEvent event) {
                    if (!event.containsEventBus(actionPanelEventBus_)) {
                        actionPanelEventBus_.fireEventFromSource(event, this);
                    }
                }
            }));*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        isTerminated_ = true;

        for (final HandlerRegistration handlerRegistration : handlerRegistrations_) {
            handlerRegistration.removeHandler();
        }
        handlerRegistrations_.clear();

        if (alarmDiagramPanel_ != null && !alarmDiagramPanel_.isTerminated()) {
        	alarmDiagramPanel_.terminate();
        }
		if (alarmListPanelForListTab_ != null && !alarmListPanelForListTab_.isTerminated()) {
			alarmListPanelForListTab_.terminate();
		}
    }

}
