package com.thalesgroup.scadasoft.gwebhmi.main.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.AppPanel;

public class FAS implements IAppEntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[FAS] ";
	
	private AppPanel appPanel_;
	
	@Override
	public void launch(Map<String, Object> params) {
    	LOGGER.debug(LOG_PREFIX+" UIWidgetEntryPoint.init...");
    	
    	UIWidgetEntryPoint.init(new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {
				
		        // Create layout
		    	LOGGER.debug(LOG_PREFIX+" create main panel");
		    	appPanel_ = new AppPanel();
		        RootLayoutPanel.get().add(appPanel_);
			}
		});

	}

}
