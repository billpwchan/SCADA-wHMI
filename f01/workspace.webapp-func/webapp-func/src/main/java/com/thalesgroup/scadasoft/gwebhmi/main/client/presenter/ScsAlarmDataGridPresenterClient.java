package com.thalesgroup.scadasoft.gwebhmi.main.client.presenter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.AlarmDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.main.client.view.ScsGenericDataGridView;

/**
 * SCADAsoft AlarmDataGridPresenterClient implementation.
 */
public class ScsAlarmDataGridPresenterClient extends AlarmDataGridPresenterClient {

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
