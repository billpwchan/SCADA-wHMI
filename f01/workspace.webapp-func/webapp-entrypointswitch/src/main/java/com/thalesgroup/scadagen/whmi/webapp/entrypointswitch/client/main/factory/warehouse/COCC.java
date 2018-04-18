package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IFramework;

public class COCC implements IFramework {
	
	/** logger */
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";

	@Override
	public void launch(final Map<String, Object> params) {
		final String f = "launch";
		logger_.debug(logPrefix_+f);
		
		final CoccAppPanel appPanel = new CoccAppPanel();
		RootLayoutPanel.get().add(appPanel);
	}

}
