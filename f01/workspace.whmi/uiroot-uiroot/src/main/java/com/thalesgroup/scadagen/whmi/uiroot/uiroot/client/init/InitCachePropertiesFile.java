package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class InitCachePropertiesFile {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitCachePropertiesFile.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void initCachePropertiesFile (String folder, String extention) {
		initCachePropertiesFile(folder, extention, null);
	}
	
	public static void initCachePropertiesFile (String folder, String extention, final InitReady_i initReady) {
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
				if ( null != initReady ) initReady.ready(received);
			}
		});

		logger.end(className, function);
	}
}
