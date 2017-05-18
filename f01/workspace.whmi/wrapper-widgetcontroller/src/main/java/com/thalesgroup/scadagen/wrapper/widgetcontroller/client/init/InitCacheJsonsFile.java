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

public class InitCacheJsonsFile implements Init_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(InitCacheJsonsFile.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private InitCacheJsonsFile() {}
	private static InitCacheJsonsFile instance = null;
	public static InitCacheJsonsFile getInstance() { 
		if ( null == instance ) instance = new InitCacheJsonsFile();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initCacheJsonsFile(final String folder, final String extention) {
		initCacheJsonsFile(folder, extention, null);
	}
	public void initCacheJsonsFile(final String folder, final String extention, final InitReady_i initReady) {
		final String function = "initCacheJsonsFile";
		logger.begin(className, function);
		logger.debug(className, function, "folder[{}] extention[{}]", folder, extention);

		String mode = ConfigurationType.JsonFile.toString();
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
