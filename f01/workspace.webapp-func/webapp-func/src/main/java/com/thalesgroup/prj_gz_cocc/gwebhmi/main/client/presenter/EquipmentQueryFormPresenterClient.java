package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.AsyncCallbackMwtAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.EquipmentDataGridPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.IGenericDataGridView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event.EqptFilterChangeEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event.EqptFilterChangeEventHandler;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.action.EqptSearchRequest;

/**
 * Client implementation of the EquipmentQueryFormPresenter
 *
 */
public class EquipmentQueryFormPresenterClient extends EquipmentDataGridPresenterClient implements EqptFilterChangeEventHandler {

	/** logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();
    
	public EquipmentQueryFormPresenterClient(String configId,
			IGenericDataGridView view, EventBus eventBus) {
		super(configId, view, eventBus);
	}
	

	/** Description of the presenter type. Used to link the client presenter with the server one. */
	public static final String PRESENTER_TYPE = EquipmentQueryFormPresenterClient.class.getName();

	@Override
	public String getPresenterTypeIdentifier() {
		return PRESENTER_TYPE;
	}

	@Override
	public void onEqptFilterChange(EqptFilterChangeEvent event) {
 
        final AsyncCallback<IOperatorActionReturn> searchCallback =
            new AsyncCallbackMwtAbstract<IOperatorActionReturn>() {

                @Override
                public void onSuccessMwt(final IOperatorActionReturn result) {
                    // Nothing to do, waiting for update by the classic update canal.
                }

                @Override
                public void onFailureMwt(final Throwable caught) {

                }
            };
         
        final EqptSearchRequest action = new EqptSearchRequest(this.getUid());
        action.setQuery(event.getEqptQuery());
        
        this.sendOperatorAction(action, searchCallback);	
        logger_.debug("**** EquipmentQueryFormPresenterClient sendOperatorAction ****");
	}
}
