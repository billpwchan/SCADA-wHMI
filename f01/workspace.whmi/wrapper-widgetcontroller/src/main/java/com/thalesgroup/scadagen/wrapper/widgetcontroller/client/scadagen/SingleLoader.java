package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;

public class SingleLoader implements Loader_i {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(SingleLoader.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static SingleLoader instance = null; 
	public static SingleLoader getInstance() {
		if ( null == instance ) {
			instance = new SingleLoader();
			instance.iniDefaultParameterName();
		}
		return instance;
	}

	@Override
	public void iniDefaultParameterName() { }
	
	public Map<String, String> parameters = new HashMap<String, String>();
	@Override
	public void setParameter(String key, String value) { parameters.put(key, value); }
	
	@Override
	public InitProcess_i getLoader() { return initProcess; };

	private InitProcess_i initProcess = null;
	
	private SingleLoader() {
		final String function = "Init";
		logger.begin(className, function);
		
		initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(className, function);
		
				UIWidgetEntryPoint.init(params, LoaderFactory.getInitProcess("PhaseALoader"), new InitReady_i() {
					
					@Override
					public void ready(Map<String, Object> params) {
						
						UIWidgetEntryPoint.init(params, LoaderFactory.getInitProcess("PhaseBLoader"), new InitReady_i() {
							
							@Override
							public void ready(final Map<String, Object> params) {

								if ( null != initReady ) {
									initReady.ready(params);
								} else {
									logger.warn(className, function, "initReady IS NULL");
								}
							}
						});
					}
				});

			    logger.end(className, function);
			}
		};
		
		logger.end(className, function);
	}
	
}
