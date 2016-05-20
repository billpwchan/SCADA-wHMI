package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.GenericDataGridPresenter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.HypervisorPresenterAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.factory.IPresenterFactory;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.CoccAlarmDataGridPresenterClient;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.CoccSVPresenterClient;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.EquipmentQueryFormPresenterClient;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.EquipmentQueryFormPresenter;

public class CoccPresenterFactory implements IPresenterFactory {

	@Override
	public HypervisorPresenterAbstract createPresenter(String type,
			IPresenterContext context,
			PresenterParams params) throws PresenterException
	{
		HypervisorPresenterAbstract toReturn = null;

		if (CoccSVPresenterClient.PRESENTER_TYPE.equals(type)) {
			toReturn = new CoccSVPresenter(params);
		} else if (EquipmentQueryFormPresenterClient.PRESENTER_TYPE.equals(type)) {
			toReturn = new EquipmentQueryFormPresenter(params);
		} else if (CoccAlarmDataGridPresenterClient.PRESENTER_TYPE.equals(type)) {
			//toReturn = new CoccDataGridPresenter(params);
			toReturn = new GenericDataGridPresenter(params);
		} 
		return toReturn;
	}

}
