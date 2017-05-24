package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;

public class FAS implements IAppEntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[FAS] ";
	
//	private AppPanel appPanel_;
	
	@Override
	public void launch(Map<String, Object> params) {
    	LOGGER.debug(LOG_PREFIX+" launch...");
    	
    	UIWidgetEntryPoint.init(new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {
				
		        // Create layout
		    	LOGGER.debug(LOG_PREFIX+" create main panel");
//		    	appPanel_ = new AppPanel();
//		        RootLayoutPanel.get().add(appPanel_);
			}
		});

	}

}
