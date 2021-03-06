package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.AlarmDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.event.GDGCounterChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.main.client.view.ScsGenericDataGridView;

/**
 * SCADAsoft AlarmDataGridPresenterClient implementation.
 */
public class CoccAlarmDataGridPresenterClient extends AlarmDataGridPresenterClient{
	

	/**
	 * Used to know which entities are selected in the view
	 */
	private ScsGenericDataGridView view_;

	/**
     *  Set initial filters
     */
	private Set<FilterSetEvent> filterSet_;
	
	private EventBus eventBus_;
	
	/** This is a logger. */
    protected final ClientLogger logger_ = ClientLogger.getClientLogger();
	
	/**
     * Constructs a custom SCADAsoft Alarm grid.
     * 
     * @param configurationId the configuration of the grid.
     * @param view the grid view.
     * @param eventBus the event bus.
	 */
    public CoccAlarmDataGridPresenterClient(String configurationId,
			ScsGenericDataGridView view, EventBus eventBus, Set<FilterSetEvent> filterSet ) {
		super(configurationId, view, eventBus);
		view_ = view;
		eventBus_ = eventBus;
		filterSet_ = filterSet;
	}

	/**
	 * Call when an instance of AlarmSelectionChangeEvent has been published in the bus
	 *  (from another datagrid, the situation view or other...)
	 *  
	 *  RBI : Override to enable the entities selection only when the source is the situation view
	 */
	@Override
	public void onSelectionChange(AlarmSelectionChangeEvent event) {
	   if(event.getSource() instanceof SituationViewPresenterClient){
	        super.onSelectionChange(event);
	    }
	}
	
	/**
	 * Call when a line is selected in the grid, create and instance of AlarmSelectionChangeEvent and publish it
	 */
	@Override
	protected void onSelectionChange(Set<EntityClient> selectedEntities) {
	    super.onSelectionChange(selectedEntities);
	}
	
	/**
	 * 
	 * @return the set of selected entities in the view
	 */
    public Set<EntityClient> getSelectedEntities() {
        Set<EntityClient> selectedEntities = new HashSet<EntityClient>();
        List<EntityClient> visibleItems =  view_.getInnerDataGrid().getVisibleItems();
        for(EntityClient entity : visibleItems){
            if(view_.getInnerDataGrid().getSelectionModel().isSelected(entity)){
                selectedEntities.add(entity);
            }
        }
        return selectedEntities;
    }
    
    @Override
    public void onInitialize( StateTransitionReturn transitionResult )
    {
        super.onInitialize( transitionResult );

//        if (criticalAlarmOnly_) {
//            final Set<String> values = new HashSet<String>();
//            values.add( "CRITICAL" );
//            StringEnumFilterDescription filterdesc = new StringEnumFilterDescription(values);
//            setContainerFilter("scsalarmList_priority_name", filterdesc);
//        }
    }
    
    @Override
    public void onActivate( StateTransitionReturn transitionResult )
    {
        super.onActivate( transitionResult );

        if (filterSet_ != null) {
            for (FilterSetEvent filter : filterSet_) {
                setContainerFilter(filter.getColumnName(), filter.getFilterDescription());
            }
        }
    }
    
    @Override
    protected void updateCounter( Map<String, Integer> countersValue )
    {
        logger_.debug("CoccAlarmDataGridPresenter.updateCounter ");
        for (String key : countersValue.keySet()) {
            logger_.debug( key + " : " + countersValue.get( key ));
        }

        if (eventBus_ != null) {
            eventBus_.fireEventFromSource(new GDGCounterChangeEvent(countersValue), this);
        }
    }
}
