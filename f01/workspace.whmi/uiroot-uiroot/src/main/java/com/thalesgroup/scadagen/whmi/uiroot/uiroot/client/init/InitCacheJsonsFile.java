package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class InitCacheJsonsFile {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(UIScreenMMI.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void initCacheJsonsFile (String folder, String extention) {
		initCacheJsonsFile(folder, extention, null);
	}
	
	public static void initCacheJsonsFile (String folder, String extention, final InitReady_i initReady_i) {
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
				if ( null != initReady_i ) initReady_i.ready(received);
			}
		});

		logger.end(className, function);
	}
}
