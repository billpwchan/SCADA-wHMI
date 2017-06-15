package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.AlarmDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.update.GDGData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.update.GDGPage;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterChangeEventAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterRemoveEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.update.IPresenterUpdate;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.CounterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.SelectionEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.UpdateEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;


/**
 * SCADAsoft AlarmDataGridPresenterClient implementation.
 */
public class ScsAlarmDataGridPresenterClient extends AlarmDataGridPresenterClient {

	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[ScsAlarmDataGridPresenterClient] ";

	private SelectionEvent selectionEvent = null;
	public SelectionEvent getSelectionEvent() { return selectionEvent; }
	public void setSelectionEvent(SelectionEvent selectionEvent) { this.selectionEvent = selectionEvent; }

	private FilterEvent filterEvent = null;
	public FilterEvent getFilterEvent() { return filterEvent; }
	public void setFilterEvent(FilterEvent filterEvent) { this.filterEvent = filterEvent; }
	
	private CounterEvent counterEvent = null;
	public CounterEvent getCounterEvent() { return counterEvent; }
	public void setCounterEvent(CounterEvent counterEvent) { this.counterEvent = counterEvent; }

	private UpdateEvent updateEvent = null;
	public UpdateEvent getUpdateEvent() { return updateEvent; }
	public void setUpdateEvent(UpdateEvent updateEvent) { this.updateEvent = updateEvent; }

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

//    private LinkedList<String> filterColumns = new LinkedList<String>();
    private Set<String> filterColumns = new LinkedHashSet<String>();
    public Set<String> getFilterColumns() { return filterColumns; }

    @Override
    public void onFilterChange(final FilterChangeEventAbstract event) {
		LOGGER.trace(LOG_PREFIX  + "onFilterChange Begin");
    	super.onFilterChange(event);
    	if ( null != event ) {
    		if (event instanceof FilterSetEvent) {
    			final FilterSetEvent filterSetEvent = (FilterSetEvent) event;
    			filterColumns.add(filterSetEvent.getColumnName());
    			
    			LOGGER.debug(LOG_PREFIX  + "onFilterChange (FilterChangeEventAbstract) event instanceof FilterSetEvent");
    		} else if (event instanceof FilterRemoveEvent) {
    			final FilterRemoveEvent filterRemoveEvent = (FilterRemoveEvent) event;
    			filterColumns.remove(filterRemoveEvent.getColumnName());
    			
    			LOGGER.debug(LOG_PREFIX  + "onFilterChange (FilterChangeEventAbstract) event instanceof FilterRemoveEvent");
    		}
    		LOGGER.debug(LOG_PREFIX  + "onFilterChange (FilterChangeEventAbstract) call begin");
    		
    		// Dump
    		LOGGER.debug(LOG_PREFIX  + "onFilterChange columns.size()["+filterColumns.size()+"]");
    		for ( String filterColumn : filterColumns ) {
    			LOGGER.debug(LOG_PREFIX  + "onFilterChange filterColumn["+filterColumn+"]");
    		}
    		
    		ArrayList<String> columns = new ArrayList<String>(); 
    		for ( String column : filterColumns ) {
    			columns.add(column);
    		}
    		
    		LOGGER.trace(LOG_PREFIX  + "onFilterChange (FilterChangeEventAbstract) call begin");
    		if ( null != filterEvent ) {
    			filterEvent.onFilterChange(columns);
    		} else {
            	LOGGER.warn(LOG_PREFIX+"onFilterChange filterEvent IS NULL");
       	 	}
    		LOGGER.trace(LOG_PREFIX  + "onFilterChange (FilterChangeEventAbstract) call end");
    	}
    	LOGGER.trace(LOG_PREFIX  + "onFilterChange End");
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
    	LOGGER.trace(LOG_PREFIX+"onSelectionChange Begin");
        if (event.getSource() instanceof SituationViewPresenterClient) {
            super.onSelectionChange(event);
//            LOGGER.debug(LOG_PREFIX+"onSelectionChange (AlarmSelectionChangeEvent) call begin");
//            LOGGER.debug(LOG_PREFIX+"onSelectionChange (AlarmSelectionChangeEvent) call end");
            
        }
        LOGGER.trace(LOG_PREFIX+"onSelectionChange End");
    }

    /**
     * Call when a line is selected in the grid, create and instance of
     * AlarmSelectionChangeEvent and publish it
     */
    @Override
    protected void onSelectionChange(Set<EntityClient> selectedEntities) {
        super.onSelectionChange(selectedEntities);
        LOGGER.trace(LOG_PREFIX+"onSelectionChange Begin");
        if ( null != selectedEntities ) {
        	
//        	// Dump selectedEntities
//            Set<HashMap<String, String>> set = new HashSet<HashMap<String, String>>();
//            for ( EntityClient ec : selectedEntities ) {
//            	HashMap<String, String> details = new HashMap<String, String>();
//            	for ( String attributeName : ec.attributeNames() ) {
//            		LOGGER.debug(LOG_PREFIX+"attributeName["+attributeName+"] value[" + ec.getAttribute(attributeName).getValue().toString() + "]");	
//            	}
//            }
        	
            Set<HashMap<String, String>> set = new HashSet<HashMap<String, String>>();
            for ( EntityClient ec : selectedEntities ) {
            	HashMap<String, String> details = new HashMap<String, String>();
            	for ( String attributeName : ec.attributeNames() ) {
             		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
            	}
            	set.add(details);
            }
            LOGGER.debug(LOG_PREFIX+"onSelectionChange call begin");
            if ( null != selectionEvent ) { 
            	this.selectionEvent.onSelection(set);
            } else {
            	LOGGER.warn(LOG_PREFIX+"onSelectionChange selectionEvent IS NULL");
       	 	}
            LOGGER.debug(LOG_PREFIX+"onSelectionChange call end");
        }
        LOGGER.trace(LOG_PREFIX+"onSelectionChange End");
    }

    /**
     * 
     * @return the set of selected entities in the view
     */
    public Set<EntityClient> getSelectedEntities() {
    	LOGGER.trace(LOG_PREFIX+"getSelectedEntities Begin");
        Set<EntityClient> selectedEntities = new HashSet<EntityClient>();
        List<EntityClient> visibleItems = view_.getInnerDataGrid().getVisibleItems();
        for (EntityClient entity : visibleItems) {
            if (view_.getInnerDataGrid().getSelectionModel().isSelected(entity)) {
                selectedEntities.add(entity);
            }
        }
        LOGGER.trace(LOG_PREFIX+"getSelectedEntities End");
        return selectedEntities;
    }
    
    @Override
    public void updateCounter(final Map<String, Integer> countersValue) {
    	 super.updateCounter(countersValue);
    	 LOGGER.trace(LOG_PREFIX+"updateCounter Begin");
    	 if ( null != counterEvent ) {
    		 
//    		 // Dump countersValue
//    		 if ( null != countersValue ) {
//    			 LOGGER.warn(LOG_PREFIX+"updateCounter countersValue size["+countersValue.size()+"]");
//    			 for ( Entry<String, Integer> keyValue : countersValue.entrySet() ) {
//    				 if ( null != keyValue ) {
//    					 String key = keyValue.getKey();
//    					 LOGGER.warn(LOG_PREFIX+"updateCounter countersValue key["+key+"]");
//    					 Integer value = keyValue.getValue();
//    					 if ( null != value ) {
//    						 String strValue = value.toString();
//    	    				 LOGGER.warn(LOG_PREFIX+"updateCounter countersValue strValue["+strValue+"]");
//    					 } else {
//    						 LOGGER.warn(LOG_PREFIX+"updateCounter countersValue value IS NULL");
//    					 }
//    				 } else {
//    					 LOGGER.warn(LOG_PREFIX+"updateCounter keyValue IS NULL");
//    				 }
//    			 }
//    		 } else {
//    			 LOGGER.warn(LOG_PREFIX+"updateCounter countersValue IS NULL");
//    		 }
    		 
    		 counterEvent.onCounterChange(countersValue);
    	 } else {
    		 LOGGER.warn(LOG_PREFIX+"updateCounter counterEvent IS NULL");
    	 }
    	 LOGGER.trace(LOG_PREFIX+"updateCounter End");
    }
    
    @Override
    public void update(final IPresenterUpdate presUpdate) {
    	super.update(presUpdate);

    	if ( null != updateEvent ) {
    		
    		Set<Map<String, String>> set = null;
    		
	        if (presUpdate instanceof GDGData) {
	        	
	            final GDGData update = (GDGData) presUpdate;
	
	            GDGPage page = update.getPage();
	            if ( null != page ) {
	            	int start = page.getStart();
	
	            	LOGGER.debug(LOG_PREFIX+"update page.start["+start+"]");
	            	
	            	List<EntityClient> entitys = page.getDisplayedEntities();
	            	
	            	if ( null != entitys ) {
	            		
	            		int size = entitys.size();
	            		
	            		LOGGER.debug(LOG_PREFIX+"update entitys.size["+size+"]");
	            		
	            		set = new HashSet<Map<String, String>>();
	            		
	                	for ( EntityClient entity : entitys ) {
	    	            	Map<String, String> details = new HashMap<String, String>();
	    	            	for ( String attributeName : entity.attributeNames() ) {
//	    	            		LOGGER.debug(LOG_PREFIX+"update entity.getAttribute("+attributeName+")["+entity.getAttribute(attributeName).getValue().toString()+"]");
	    	             		details.put(attributeName, entity.getAttribute(attributeName).getValue().toString());
	    	            	}
	    	            	set.add(details);
	                	}
	            	} else {
	            		LOGGER.warn(LOG_PREFIX+"update entitys IS NULL");
	            	}
	            } else {
	            	LOGGER.warn(LOG_PREFIX+"update page IS NULL");
	            }
	            
	            if ( null != set ) {
	            	updateEvent.onUpdate(set);
	            }
	        }
		}
    }

}
