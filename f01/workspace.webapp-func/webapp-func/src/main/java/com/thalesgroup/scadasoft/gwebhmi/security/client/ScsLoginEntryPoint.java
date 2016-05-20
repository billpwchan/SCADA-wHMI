package com.thalesgroup.scadasoft.gwebhmi.security.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.LoginPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;

/**
 * Login application for MAESTRO demo.
 */
public class ScsLoginEntryPoint implements EntryPoint {

    /**
     * MWT {@link LoginPanel} panel
     */
    private ScsLoginPanel loginPanel_;

    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {

        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

        loginPanel_ = new ScsLoginPanel();
        loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
    }
}
