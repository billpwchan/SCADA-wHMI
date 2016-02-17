package com.thalesgroup.scadasoft.gwebhmi.main.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.action.PresenterOperatorAction;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.config.NavigationDef;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.presenter.GenericTreeInitReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.tree.GenericTreePresenterServer;
import com.thalesgroup.scadasoft.gwebhmi.main.client.presenter.MobileToolbarStateTransitionReturn;

public class MobileToolbarPresenterServer extends GenericTreePresenterServer {

    public MobileToolbarPresenterServer(PresenterParams params) {

        super(params);

    }

    @Override
    public IOperatorActionReturn manageAction(PresenterOperatorAction presenterOperatorAction) {

        return null;
    }

    @Override
    public StateTransitionReturn onInitialize(IPresenterContext context) {
        StateTransitionReturn superTransitionReturn = super.onInitialize(context);

        Map<String, String> toolMenuMap = new HashMap<String, String>();
        if (superTransitionReturn instanceof GenericTreeInitReturn) {
            GenericTreeInitReturn navTreeInitReturn = (GenericTreeInitReturn) superTransitionReturn;
            Map<String, NavigationDef> navDef = navTreeInitReturn.getNavTreeTypeClient().getNavDefPerNavId();
            for (Entry<String, NavigationDef> entry : navDef.entrySet()) {

                if (entry.getValue().getNavId() != null && !entry.getValue().getNavId().isEmpty()
                        && entry.getValue().getNavigationDef().size() == 0) {
                    toolMenuMap.put(entry.getValue().getNavId(), entry.getValue().getLabelName());
                }
            }
        }

        MobileToolbarStateTransitionReturn transitionReturn = new MobileToolbarStateTransitionReturn();
        transitionReturn.setNavMap(toolMenuMap);

        return transitionReturn;
    }

}
