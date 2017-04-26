package com.thalesgroup.scadasoft.gwebhmi.main.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint.InitReady_i;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.AppPanel;

public class FAS implements IAppEntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[FAS] ";
	
	private AppPanel appPanel_;
	@Override
	public void launch(Map<String, String> map) {
    	LOGGER.debug(LOG_PREFIX+" UIWidgetEntryPoint.init...");
    	
    	// Loading the UIJson Data Dictionary
        UIWidgetEntryPoint.initCacheJsonsFile("UIJson", "*.json");
        // Loading the XML Data Dictionary
        UIWidgetEntryPoint.initCacheXMLFile("UIWidgetGeneric", "*.xml", new InitReady_i() {
			
			@Override
			public void ready(int received) {
				LOGGER.debug(LOG_PREFIX+" UIWidgetEntryPoint.init ready received["+received+"]");
				
				// Loading SCADAgen OPM Factory
		        UIWidgetEntryPoint.initOpmFactory();
		        // Init the SCADAgen OPM API
		        UIWidgetEntryPoint.initOpm("UIOpmSCADAgen");
		        
//				Init for the Database Singleton Usage		        
//		        UIWidgetEntryPoint.initDatabaseReadingSingletonKey("DatabaseMultiReadingProxySingleton");
//				UIWidgetEntryPoint.initDatabaseSubscribeSingleton("DatabaseGroupPollingDiffSingleton", 500);
//				UIWidgetEntryPoint.initDatabaseWritingSingleton("DatabaseWritingSingleton");
				
		        // Create layout
		    	LOGGER.debug(LOG_PREFIX+" create main panel");
		    	appPanel_ = new AppPanel();
		        RootLayoutPanel.get().add(appPanel_);
			}
		});
	}

}
