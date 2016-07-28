package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.AlarmDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterChangeEventAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterRemoveEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;

/**
 * SCADAsoft AlarmDataGridPresenterClient implementation.
 */
public class ScsAlarmDataGridPresenterClient extends AlarmDataGridPresenterClient {
	
	/** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
	
	private SelectionEvent selectionEvent = null;
	public SelectionEvent getSelectionEvent() { return selectionEvent; }
	public void setSelectionEvent(SelectionEvent selectionEvent) { this.selectionEvent = selectionEvent; }
	
	private FilterEvent filterEvent = null;
	public FilterEvent getFilterEvent() { return filterEvent; }
	public void setFilterEvent(FilterEvent filterEvent) { this.filterEvent = filterEvent; }

    /**
     * Used to know which entities are selected in the view
     */
    private ScsGenericDataGridView view_;

    /**
     * Constructs a custom SCADAsoft Alarm grid.
     * 
     * @param configurationId
     *            the configuration of the grid.
     * @param view
     *            the grid view.
     * @param eventBus
     *            the event bus.
     */
    public ScsAlarmDataGridPresenterClient(String configurationId, ScsGenericDataGridView view, EventBus eventBus) {
        super(configurationId, view, eventBus);
        view_ = view;
    }

    private LinkedList<String> filterColumns = new LinkedList<String>();
    public LinkedList<String> getFilterColumns() { return filterColumns; }

    @Override
    public void onFilterChange(final FilterChangeEventAbstract event) {
    	
    	super.onFilterChange(event);
		if (event instanceof FilterSetEvent) {
			final FilterSetEvent filterSetEvent = (FilterSetEvent) event;
			filterColumns.add(filterSetEvent.getColumnName());
		} else if (event instanceof FilterRemoveEvent) {
			final FilterRemoveEvent filterRemoveEvent = (FilterRemoveEvent) event;
			filterColumns.remove(filterRemoveEvent.getColumnName());
		}
		
		ArrayList<String> columns = new ArrayList<String>(); 
		for ( String column : filterColumns ) {
			columns.add(column);
		}
		if ( null != filterEvent ) {
			filterEvent.onFilterChange(columns);
		}
    }

    /**
     * Call when an instance of AlarmSelectionChangeEvent has been published in
     * the bus (from another datagrid, the situation view or other...)
     * 
     * RBI : Override to enable the entities selection only when the source is
     * the situation view
     */
    @Override
    public void onSelectionChange(AlarmSelectionChangeEvent event) {
        if (event.getSource() instanceof SituationViewPresenterClient) {
            super.onSelectionChange(event);
        }
    }

    /**
     * Call when a line is selected in the grid, create and instance of
     * AlarmSelectionChangeEvent and publish it
     */
    @Override
    protected void onSelectionChange(Set<EntityClient> selectedEntities) {
        super.onSelectionChange(selectedEntities);
        
        s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + selectedEntities);
        
        s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + "selected filter");
        
        HashMap<String, String> values = new HashMap<String, String>();
//        ArrayList<String> ids = new ArrayList<String>(); 
        for ( EntityClient ec : selectedEntities ) {
//        	ids.add(ec.getAttribute("equipmentname").getValue().toString());
        	for ( String attributeName : ec.attributeNames() ) {
        		
        		s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + " *** attributeName value[" + ec.getAttribute(attributeName).getValue().toString() + "]");	
        		
        		values.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
        	}
        }
        
        s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + "call");
        
        if ( null != selectionEvent ) { 
//        	this.selectionEvent.onSelection(ids); 
        	this.selectionEvent.onSelection(values);
//        	s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + " *** equipmentname.value[" + ids + "]");	
        }
        
        s_logger.debug(ScsAlarmDataGridPresenterClient.class.getName() + "end");

    }

    /**
     * 
     * @return the set of selected entities in the view
     */
    public Set<EntityClient> getSelectedEntities() {
        Set<EntityClient> selectedEntities = new HashSet<EntityClient>();
        List<EntityClient> visibleItems = view_.getInnerDataGrid().getVisibleItems();
        for (EntityClient entity : visibleItems) {
            if (view_.getInnerDataGrid().getSelectionModel().isSelected(entity)) {
                selectedEntities.add(entity);
            }
        }
        return selectedEntities;
    }



}
