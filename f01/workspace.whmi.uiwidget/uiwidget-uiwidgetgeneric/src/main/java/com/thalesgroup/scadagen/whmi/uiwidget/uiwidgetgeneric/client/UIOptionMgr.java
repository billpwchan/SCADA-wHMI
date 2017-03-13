package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;


import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIOptionMgr {
	private final String className = UIWidgetUtil.getClassSimpleName(UIOptionMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	
	public UIOptionMgr(String logPrefix) {
		final String function = "initActionKeys";
		
		logger.begin(className, function);
		
		this.logPrefix = "-> "+logPrefix+" ";
		
		logger.trace(className, function, "this.logPrefix[{}]", this.logPrefix);
		
		logger.end(className, function);
	}
	
	public HashMap<String, HashMap<String, String>> getOptions( final String dictionariesCacheName, final String fileName, final String tag) {
		final String function = "getOptionKey";
		logger.begin(className, function);
		
		logger.trace(className, function, this.logPrefix+"dictionariesCacheName[{}]", dictionariesCacheName);
		logger.trace(className, function, this.logPrefix+"fileName[{}]", fileName);
		logger.trace(className, function, this.logPrefix+"tag[{}]", tag);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		Dictionary dictionary = dictionariesCache.getDictionary(fileName+(null!=tag?"|"+tag:""));
		
		HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
		if ( null != dictionary ) {
			logger.trace(className, function, logPrefix+"begin of for dictionary");
			for ( Object o : dictionary.getValueKeys() ) {
				if ( null != o ) {
					Dictionary d2 = (Dictionary) dictionary.getValue(o);
					logger.trace(className, function, logPrefix+"d2[{}]", d2);
					if ( null != d2 ) {
						String d2ak = "key";
						logger.trace(className, function, logPrefix+"d2ak[{}]", d2ak);
						String d2av = (String) d2.getAttribute(d2ak);
						logger.trace(className, function, logPrefix+"d2ak[{}] d2av[{}]", d2ak, d2av);
						HashMap<String, String> option = new HashMap<String, String>();
						for ( Object objd2vk : d2.getValueKeys() ) {
							String d2vk = (String) objd2vk;
							String d2vv = (String) d2.getValue(objd2vk);
							logger.trace(className, function, logPrefix+"d2vk[{}] d2vv[{}]", d2vk, d2vv);
							option.put(d2vk, d2vv);
						}
						result.put(d2av, option);
					} else {
						logger.warn(className, function, logPrefix+"d2 IS NULL");
					}
				} else {
					logger.warn(className, function, logPrefix+"o IS NULL");
				}
			}
			logger.trace(className, function, logPrefix+"end of for dictionary");
		} else {
			logger.warn(className, function, logPrefix+"dictionary IS NULL");
		}
		logger.end(className, function);
		return result;
	}

}