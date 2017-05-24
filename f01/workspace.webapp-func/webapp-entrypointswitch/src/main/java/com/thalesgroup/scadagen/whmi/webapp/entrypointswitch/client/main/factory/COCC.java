package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;

public class COCC implements IAppEntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[COCC] ";

	@Override
	public void launch(Map<String, Object> params) {
		LOGGER.debug(LOG_PREFIX+" launch...");
		
		final CoccAppPanel appPanel = new CoccAppPanel();
		RootLayoutPanel.get().add(appPanel);
	}

}
