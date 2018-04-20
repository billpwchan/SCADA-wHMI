package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse;

import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IFramework;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;

public class FAS implements IFramework {
	
	/** logger */
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";
	
//	private AppPanel appPanel_;
	
	@Override
	public void launch(final Map<String, Object> params) {
		final String f = "launch";
		logger_.debug(logPrefix_+f);
    	
		realize(params);
	}
	
	private void realize(final Map<String, Object> params) {
		final String f = "realize";
		logger_.debug(logPrefix_+f);
    	UIWidgetEntryPoint.init(new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {
				
		        // Create layout
				logger_.debug(logPrefix_+f+" create main panel");
//		    	appPanel_ = new AppPanel();
//		        RootLayoutPanel.get().add(appPanel_);
			}
		});
	}
}
