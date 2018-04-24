package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;


import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class UIOptionMgr {

	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	private String logPrefix = "";
	
	public UIOptionMgr(String logPrefix) {
		final String f = "initActionKeys";
		logger.begin(className, f);
		
		this.logPrefix = "-> "+logPrefix+" ";
		
		logger.trace(className, f, "this.logPrefix[{}]", this.logPrefix);
		logger.end(className, f);
	}
	
	public Map<String, Map<String, String>> getOptions(final String dictionariesCacheName, final String fileName, final String tag) {
		final String f = "getOptionKey";
		logger.begin(className, f);
		
		logger.trace(className, f, this.logPrefix+"dictionariesCacheName[{}]", dictionariesCacheName);
		logger.trace(className, f, this.logPrefix+"fileName[{}]", fileName);
		logger.trace(className, f, this.logPrefix+"tag[{}]", tag);
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		final Dictionary dictionary = dictionariesCache.getDictionary(fileName+(null!=tag?"|"+tag:""));
		
		final Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		if ( null != dictionary ) {
			logger.trace(className, f, logPrefix+"begin of for dictionary");
			for ( Object o : dictionary.getValueKeys() ) {
				if ( null != o ) {
					final Dictionary d2 = (Dictionary) dictionary.getValue(o);
					logger.trace(className, f, logPrefix+"d2[{}]", d2);
					if ( null != d2 ) {
						final String d2ak = "key";
						logger.trace(className, f, logPrefix+"d2ak[{}]", d2ak);
						final String d2av = (String) d2.getAttribute(d2ak);
						logger.trace(className, f, logPrefix+"d2ak[{}] d2av[{}]", d2ak, d2av);
						final Map<String, String> option = new HashMap<String, String>();
						for ( Object objd2vk : d2.getValueKeys() ) {
							final String d2vk = (String) objd2vk;
							final String d2vv = (String) d2.getValue(objd2vk);
							logger.trace(className, f, logPrefix+"d2vk[{}] d2vv[{}]", d2vk, d2vv);
							option.put(d2vk, d2vv);
						}
						result.put(d2av, option);
					} else {
						logger.warn(className, f, logPrefix+"d2 IS NULL");
					}
				} else {
					logger.warn(className, f, logPrefix+"o IS NULL");
				}
			}
			logger.trace(className, f, logPrefix+"end of for dictionary");
		} else {
			logger.warn(className, f, logPrefix+"dictionary IS NULL");
		}
		logger.end(className, f);
		return result;
	}

}