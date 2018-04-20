package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IFramework;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.LoaderFactory;

public class SCADAgenStandalone implements IFramework {
	
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_ = "["+className_+"] ";
	
	@Override
	public void launch(final Map<String, Object> params) {
		final String f = "launch";
		logger_.debug(logPrefix_+f+" launch...");
    	
    	realize(params);
	}
	
    private void realize(final Map<String, Object> params) {
    	final String f = "realize";
    	logger_.debug(logPrefix_+f);
    	
    	UIWidgetEntryPoint.init(params, LoaderFactory.getInitProcess("SingleLoader"), new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {

				logger_.debug(logPrefix_+f+" create UIWidgetEntryPoint...");
				
				final UIWidgetEntryPoint uiWidgetEntryPoint = new UIWidgetEntryPoint(params);
				
				logger_.debug(logPrefix_+f+" get widget from UIWidgetEntryPoint...");
				
				final Widget widget = uiWidgetEntryPoint.asWidget();
				
				logger_.debug(logPrefix_+f+" add main panel...");
				
		        // Create layout
		        RootLayoutPanel.get().add(widget);
			}
		});
    }
}
