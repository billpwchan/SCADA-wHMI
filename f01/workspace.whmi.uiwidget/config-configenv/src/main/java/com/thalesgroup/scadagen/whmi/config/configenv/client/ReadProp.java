package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadProp {
	private static final String className = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static boolean readBoolean(String dictionariesCacheName, String fileName, String valueKey, boolean defaulValue) {
		final String function = "readBoolean";
		
		logger.begin(className, function);
		
		boolean result = defaulValue;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			
			String strResult = dictionariesCache.getStringValue(fileName, valueKey);
			logger.debug(className, function, "strResult[{}]", strResult);
			if ( null != strResult ) {
				if ( 0 == Boolean.TRUE.toString().compareToIgnoreCase(strResult) ) {
					logger.debug(className, function, "strResult IS TRUE");
					result = true;
				} else {
					result = false;
				}
			} else {
				logger.warn(className, function, "fileName [{}] valueKey [{}] not found in DictionaryCache. Default value is used.", fileName, valueKey);
			}
			logger.debug(className, function, "strResult[{}]", strResult);
			
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.end(className, function);
		
		return result;
	}
	
	public static int readInt(String dictionariesCacheName, String fileName, String valueKey, int defaultValue) {
		final String function = "readInt";
		
		logger.begin(className, function);
		
		int result = defaultValue;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			String strInt = dictionariesCache.getStringValue(fileName, valueKey);
			logger.debug(className, function, "strInt[{}]", strInt);
			
			if (null != strInt) {
				try {
					result = Integer.parseInt(strInt);
				} catch ( NumberFormatException e ) {
					logger.warn(className, function, "invalid integer value of result[{}]", result);
				}
			} else {
				logger.warn(className, function, "fileName [{}] valueKey [{}] not found in DictionaryCache. Default value is used.", fileName, valueKey);
			}
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.debug(className, function, "result[{}]", result);
		
		return result;
	}
	
	public static String readString(String dictionariesCacheName, String fileName, String valueKey, String defaultValue) {
		final String function = "readString";
		
		logger.begin(className, function);
		
		String result = defaultValue;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			result = dictionariesCache.getStringValue(fileName, valueKey);
			if (result == null) {
				result = defaultValue;
				logger.warn(className, function, "fileName [{}] valueKey [{}] not found in DictionaryCache. Default value is used.", fileName, valueKey);
			}
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.debug(className, function, "result[{}]", result);
		
		return result;
	}
}