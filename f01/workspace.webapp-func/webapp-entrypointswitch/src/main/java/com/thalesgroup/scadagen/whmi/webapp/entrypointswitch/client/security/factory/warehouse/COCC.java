package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.warehouse;

import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.prj_gz_cocc.gwebhmi.security.client.CoccLoginPanel;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.IFramework;

public class COCC implements IFramework {
	
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";

	@Override
	public void launch(final Map<String, Object> params) {
		final String f = "launch";
		logger_.debug(logPrefix_+f);

		
		realize(params);
	}
	
	private void realize(final Map<String, Object> params) {
		final String f = "realize";
		logger_.debug(logPrefix_+f);
		
    	UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

    	CoccLoginPanel loginPanel_ = new CoccLoginPanel();
    	loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
	}

}
