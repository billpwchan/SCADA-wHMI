package com.thalesgroup.scadasoft.gwebhmi.security.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;

import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsLogin;

/**
 * Login application for MAESTRO demo.
 */
public class ScsLoginEntryPoint_WHMI implements EntryPoint {

    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {

        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

		String SPRING_SEC_PROCESSING_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		String user = "chief";
		String pass = "thales";

		UIGwsLogin uiGwsLogin = new UIGwsLogin();
		uiGwsLogin.set(SPRING_SEC_PROCESSING_URL, user_name, user, pass_name, pass);
		RootLayoutPanel.get().add(uiGwsLogin.getMainPanel());
		uiGwsLogin.submit();
    }
}
