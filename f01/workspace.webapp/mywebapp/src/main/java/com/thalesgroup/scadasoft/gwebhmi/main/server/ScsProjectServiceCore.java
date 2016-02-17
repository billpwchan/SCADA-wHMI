package com.thalesgroup.scadasoft.gwebhmi.main.server;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ProjectServiceCore;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.HypervisorPresenterAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.scadasoft.gwebhmi.ui.server.scscomponent.ScsComponentPresenterServer;
import com.thalesgroup.scadasoft.gwebhmi.main.client.presenter.MobileToolBarPresenter;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.infoPanel.presenter.ScsInfoPanelPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.server.infoPanel.ScsInfoPanelPresenterServer;

// Project specific ProjectServiceCore, this class is used to choose the presenter server
// corresponding to the presenter client (see Hypervisor MVP documentation)

public class ScsProjectServiceCore extends ProjectServiceCore {

    @Override
    public HypervisorPresenterAbstract createPresenter(String type, IPresenterContext context, PresenterParams params) throws PresenterException
    {
        HypervisorPresenterAbstract toReturn = null;

        // check if we know the type
        if (ScsInfoPanelPresenterClient.PRESENTER_TYPE.equals(type)) {
            // SCADAsoft specific Info Panel
            toReturn = new ScsInfoPanelPresenterServer(params);

        } else if (MobileToolBarPresenter.PRESENTER_TYPE.equals(type)) {
            // Demo presneter for Mobile layout toolbar
            toReturn = new MobileToolbarPresenterServer(params);

        } else if ("com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.presenter.AScsComponentPresenterClient".equals(type)) {
            toReturn = new ScsComponentPresenterServer(params);
        } else {
            // fallback to Hypervisor Presenter
            toReturn = super.createPresenter(type, context, params);
        }

        return toReturn;
    }
}
