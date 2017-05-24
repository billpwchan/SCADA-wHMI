package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws_i;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.Util;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.InitProcess;

public class SCADAgenStandalone implements IAppEntryPoint {
	
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[SCADAgenStandalone] ";
	
	@Override
	public void launch(final Map<String, Object> params) {
    	LOGGER.debug(LOG_PREFIX+" launch...");
    	
    	UIWidgetEntryPoint.init(params, InitProcess.getInstance().get(), new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {
				
				LOGGER.debug(LOG_PREFIX+" getting parameter...");
				
				String uiCtrl = Util.getStringParameter(params, UIGws_i.Parameters.uiCtrl.toString());
				String uiView = Util.getStringParameter(params, UIGws_i.Parameters.uiView.toString());
				String uiOpts = Util.getStringParameter(params, UIGws_i.Parameters.uiOpts.toString());
				String uiElem = Util.getStringParameter(params, UIGws_i.Parameters.uiElem.toString());
				String uiDict = Util.getStringParameter(params, UIGws_i.Parameters.uiDict.toString());
				
				LOGGER.debug(LOG_PREFIX+" uiCtrl["+uiCtrl+"]");
				LOGGER.debug(LOG_PREFIX+" uiView["+uiView+"]");
				LOGGER.debug(LOG_PREFIX+" uiOpts["+uiOpts+"]");
				LOGGER.debug(LOG_PREFIX+" uiElem["+uiElem+"]");
				LOGGER.debug(LOG_PREFIX+" uiDict["+uiDict+"]");
				
				LOGGER.debug(LOG_PREFIX+" create UIWidgetEntryPoint...");
				
				UIWidgetEntryPoint uiWidgetEntryPoint = new UIWidgetEntryPoint(uiCtrl, uiView, uiOpts, uiElem, uiDict);
				
				LOGGER.debug(LOG_PREFIX+" get widget from UIWidgetEntryPoint...");
				
				Widget widget = uiWidgetEntryPoint.asWidget();
				
				LOGGER.debug(LOG_PREFIX+" add main panel...");
				
		        // Create layout
		        RootLayoutPanel.get().add(widget);
		        
			}
		});

	}
}
