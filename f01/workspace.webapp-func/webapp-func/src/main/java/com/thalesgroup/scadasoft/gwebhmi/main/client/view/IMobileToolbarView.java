package com.thalesgroup.scadasoft.gwebhmi.main.client.view;

import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;

public interface IMobileToolbarView extends HypervisorView {
    public void setNavMap(Map<String, String> navMap, FlowPanel mainPanel);

    public FlowPanel getMainPanel();
}
