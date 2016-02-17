package com.thalesgroup.scadasoft.gwebhmi.main.client.presenter;

import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;

public class MobileToolbarStateTransitionReturn extends StateTransitionReturn {

    /**
     * 
     */
    private static final long serialVersionUID = 3652216115323774451L;
    private Map<String, String> navMap_;

    public Map<String, String> getNavMap() {
        return navMap_;
    }

    public void setNavMap(Map<String, String> navMap) {
        this.navMap_ = navMap;
    }

}
