package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;

public class InitCachePropertiesFile implements Init_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(InitCachePropertiesFile.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private InitCachePropertiesFile() {}
	private static InitCachePropertiesFile instance = null;
	public static InitCachePropertiesFile getInstance() { 
		if ( null == instance ) instance = new InitCachePropertiesFile();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initCachePropertiesFile (final String folder, final String extention) {
		initCachePropertiesFile(folder, extention, null);
	}
	
	public void initCachePropertiesFile (String folder, String extention, final InitReady_i initReady) {
		final String function = "initCachePropertiesFile";
		logger.begin(className, function);
		logger.debug(className, function, "folder[{}] extention[{}]", folder, extention);

		String mode = ConfigurationType.PropertiesFile.toString();
		String module = null;
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		dictionariesCache.add(folder, extention, null);
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.debug(className, function, "dictionaryCacheEventReady received[{}]", received);
				if ( null != initReady ) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("received", received);
					initReady.ready(params);
				}
			}
		});

		logger.end(className, function);
	}
}
