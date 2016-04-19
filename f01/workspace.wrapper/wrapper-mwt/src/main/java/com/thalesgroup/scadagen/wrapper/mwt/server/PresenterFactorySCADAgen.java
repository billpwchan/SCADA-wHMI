package com.thalesgroup.scadagen.wrapper.mwt.server;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.HypervisorPresenterAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.factory.IPresenterFactory;

public class PresenterFactorySCADAgen implements IPresenterFactory {

	@Override
	public HypervisorPresenterAbstract createPresenter(String type, IPresenterContext context, PresenterParams params) 
			throws PresenterException 
	{
		HypervisorPresenterAbstract toReturn = null;

		/*
		if (com.thalesgroup.scadagen.wrapper.mwt.client.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.GenericDataGridPresenterClientAbstract.PRESENTER_TYPE.equals(type)) {
			toReturn = new com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.GenericDataGridPresenter.GenericDataGridPresenter(params);
		}
		*/
		return toReturn;

	}

}
