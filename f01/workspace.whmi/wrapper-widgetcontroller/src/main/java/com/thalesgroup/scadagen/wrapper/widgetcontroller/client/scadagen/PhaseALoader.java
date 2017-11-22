package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCachePropertiesFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;

public class PhaseALoader implements Loader_i {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(PhaseALoader.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static PhaseALoader instance = null; 
	public static PhaseALoader getInstance() {
		if ( null == instance ) {
			instance = new PhaseALoader();
			instance.iniDefaultParameterName();
		}
		return instance;
	}
	
	public Map<String, String> parameters = new HashMap<String, String>();
	@Override
	public void setParameter(String key, String value) { parameters.put(key, value); }
	
	private InitProcess_i initProcess = null;
	@Override
	public InitProcess_i getLoader() { return initProcess; };
	
	public final static String strUIDict = "uiDict";
	public final static String strUIProp = "uiProp";
	public final static String strUIJson = "uiJson";
	
	@Override
	public void iniDefaultParameterName() {
		parameters.put(strUIDict, "UIWidgetGeneric");
		parameters.put(strUIProp, "UIInspectorPanel");
		parameters.put(strUIJson, "UIJson");
	}
	
	private PhaseALoader() {
		final String function = "PhaseALoader";
		logger.begin(className, function);
		
		initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(className, function);

				// Loading the UIJson Data Dictionary
				InitCacheJsonsFile.getInstance().initCacheJsonsFile(parameters.get(strUIJson), "*.json");
			    
			    // Loading the UIInspector Data Dictionary
			    InitCachePropertiesFile.getInstance().initCachePropertiesFile(parameters.get(strUIProp), "*.properties");
			    
			    // Loading the XML Data Dictionary
			    InitCacheXMLFile.getInstance().initCacheXMLFile(parameters.get(strUIDict), "*.xml", new InitReady_i() {
					
					@Override
					public void ready(final Map<String, Object> keyValues) {
						
						int received = 0;
						if ( null != keyValues) {
							Object obj = keyValues.get("received");
							if ( null != obj && obj instanceof Integer ) {
								received = (Integer)obj;
							}
						}
						
						logger.debug(className, function, " UIWidgetEntryPoint.init ready received["+received+"]");
						
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
