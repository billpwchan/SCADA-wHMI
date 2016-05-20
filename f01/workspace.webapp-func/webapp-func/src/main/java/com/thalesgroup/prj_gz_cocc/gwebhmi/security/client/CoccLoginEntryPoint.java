package com.thalesgroup.prj_gz_cocc.gwebhmi.security.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.LoginPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.prj_gz_cocc.gwebhmi.security.client.CoccLoginPanel;

/**
 * Login application for MAESTRO demo.
 */
public class CoccLoginEntryPoint implements EntryPoint {

    /**
     * MWT {@link LoginPanel} panel
     */
    private CoccLoginPanel loginPanel_;

    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {

        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

        loginPanel_ = new CoccLoginPanel();
        loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
    }
}
