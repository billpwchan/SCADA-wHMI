package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.prj_gz_cocc.gwebhmi.security.client.CoccLoginPanel;

public class COCC implements IScsLoginEntryPoint {
	
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[COCC] ";

	@Override
	public void launch(Map<String, Object> params) {
		LOGGER.debug(LOG_PREFIX+" launch.init...");
		
    	UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

    	CoccLoginPanel loginPanel_ = new CoccLoginPanel();
    	loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
        
	}

}
