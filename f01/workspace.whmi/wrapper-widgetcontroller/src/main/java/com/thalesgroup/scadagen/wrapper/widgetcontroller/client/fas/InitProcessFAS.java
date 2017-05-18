package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.fas;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCachePropertiesFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitDatabase;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitOpm;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitTranslation;

public class InitProcessFAS {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(InitProcessFAS.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static InitProcessFAS instance = null; 
	public static InitProcessFAS getInstance() {
		if ( null == instance ) {
			instance = new InitProcessFAS();
		}
		return instance;
	}
	
	public InitProcess_i get() { return initProcess; };

	private InitProcess_i initProcess = null;
	
	private InitProcessFAS() {
		final String function = "InitProcessFAS";
		logger.begin(className, function);
		
		initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(className, function);
					
				// Loading the UIJson Data Dictionary
				InitCacheJsonsFile.getInstance().initCacheJsonsFile("UIJson", "*.json");
			    
			    // Loading the UIInspector Data Dictionary
			    InitCachePropertiesFile.getInstance().initCachePropertiesFile("UIInspectorPanel", "*.properties");
			    
			    // Loading the XML Data Dictionary
			    InitCacheXMLFile.getInstance().initCacheXMLFile("UIWidgetGeneric", "*.xml", new InitReady_i() {
					
					@Override
					public void ready(final Map<String, Object> params) {
						
						int received = 0;
						if ( null != params) {
							Object obj = params.get("received");
							if ( null != obj && obj instanceof Integer ) {
								received = (Integer)obj;
							}
						}
						
						logger.debug(className, function, " UIWidgetEntryPoint.init ready received["+received+"]");
						
						// Loading SCADAgen OPM Factory
						InitOpm.getInstance().initOpmFactory();
				        
				        // Init the SCADAgen OPM API
				        InitOpm.getInstance().initOpm("UIOpmSCADAgen");
				        
						// Init for the Database Singleton Usage		        
				        InitDatabase.getInstance().initDatabaseReadingSingletonKey("DatabaseMultiReadingProxySingleton");
				        InitDatabase.getInstance().initDatabaseSubscribeSingleton("DatabaseGroupPollingDiffSingleton", 500);
				        InitDatabase.getInstance().initDatabaseWritingSingleton("DatabaseWritingSingleton");
						
						InitTranslation.getInstance().initTranslation("&\\w+", "g");
	
						if ( null != initReady ) {
							initReady.ready(params);
						} else {
							logger.warn(className, function, "initReady IS NULL");
						}
					}
				});
			    
			    logger.end(className, function);
			}
		};
		
		logger.end(className, function);
	}
	
}
