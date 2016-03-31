package com.thalesgroup.scadasoft.gwebhmi.main.server;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.HypervisorPresenterAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.factory.IPresenterFactory;
import com.thalesgroup.scadasoft.gwebhmi.main.client.presenter.MobileToolBarPresenter;

public class PresenterFactoryMyProject implements IPresenterFactory {

	@Override
	public HypervisorPresenterAbstract createPresenter(String type, IPresenterContext context, PresenterParams params) 
			throws PresenterException 
	{
		HypervisorPresenterAbstract toReturn = null;

		if (MobileToolBarPresenter.PRESENTER_TYPE.equals(type)) {
			// Demo presenter for Mobile layout toolbar
			toReturn = new MobileToolbarPresenterServer(params);
		}
		return toReturn;

	}

}
