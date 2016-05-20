package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.view;

import java.util.List;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.IGenericDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;

public class CoccEquipmentQueryFormView extends GenericDataGridView {

	/** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();
    
    private IGenericDataGridPresenterClient presenter_;
    
	@Override
	public void setPresenter(final HypervisorPresenterClientAbstract< ? > presenter) {

        if (presenter instanceof IGenericDataGridPresenterClient) {
            presenter_ = (IGenericDataGridPresenterClient) presenter;
        } else {
            logger_.error("Cannot cast " + presenter.getClass() + " into " + IGenericDataGridPresenterClient.class);
        }

    }
	
	@Override
	protected IGenericDataGridPresenterClient getPresenter() {
		return presenter_;
	}
	
	@Override
	public void updateDisplayedWindow(int start, List<EntityClient> entities) {
		super.updateDisplayedWindow(start, entities);
		
		for (EntityClient entityclient : entities) {
			logger_.debug("CoccEquipmentQueryFormView.updateDisplayedWindow  entityclient=" + entityclient.toString());
		}
	}

}
