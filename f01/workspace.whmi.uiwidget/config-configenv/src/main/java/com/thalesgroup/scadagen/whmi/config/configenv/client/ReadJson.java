package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadJson {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(ReadJson.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static JSONObject readJsonObject(String dictionariesCacheName, String fileName) {
		final String function = "readData";
		logger.begin(className, function);
		JSONObject jsonObject = null;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			
			String data = dictionariesCache.getData(fileName);
			
			logger.trace(className, function, "dictionariesCacheName[{}] fileName[{}] data[{}]", new Object[]{dictionariesCacheName, fileName, data});
			
		    if (data != null ) {
		    	if ( JsonUtils.safeToEval(data)) {
		    		jsonObject = JSONParser.parseStrict(data).isObject();
		    	} else {
		    		logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] data IS NOT SAFE TO EVAL", dictionariesCacheName, fileName);
		    	}
		    } else {
		    	logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] data IS NULL", dictionariesCacheName, fileName);
		    }
		    
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL", dictionariesCacheName, fileName);
		}
		
		logger.end(className, function);
		return jsonObject;
	}
	
	public static String readString(String dictionariesCacheName, String fileName, String key, String defaulValue) {
		final String function = "readString";
		logger.begin(className, function);
		
		String result = defaulValue;
		
		JSONObject jsonObject = readJsonObject(dictionariesCacheName, fileName);
		
		if ( null != jsonObject ) {
	        JSONValue m = jsonObject.get(key);
	        if (m != null && m.isString() != null) {
	        	result = m.isString().stringValue();
	        }
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.end(className, function);
		return result;
	}
	
	public static int readInt(String dictionariesCacheName, String fileName, String key, int defaulValue) {
		final String function = "readInt";
		logger.begin(className, function);
		
		int result = defaulValue;
		
		JSONObject jsonObject = readJsonObject(dictionariesCacheName, fileName);
		
		if ( null != jsonObject ) {
	        JSONValue m = jsonObject.get(key);
	        if (m != null && m.isNumber() != null) {
	        	result = (int) m.isNumber().doubleValue();
	        }
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.end(className, function);
		return result;
	}
	
	public static boolean readBoolean(String dictionariesCacheName, String fileName, String key, boolean defaulValue) {
		final String function = "readBoolean";
		logger.begin(className, function);
		
		boolean result = defaulValue;
		
		JSONObject jsonObject = readJsonObject(dictionariesCacheName, fileName);
		
		if ( null != jsonObject ) {
	        JSONValue m = jsonObject.get(key);
	        if (m != null && m.isNumber() != null) {
	        	result = m.isBoolean().booleanValue();
	        }
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL", dictionariesCacheName);
		}
		
		logger.end(className, function);
		return result;
	}
	
	public static JSONArray readArray(String dictionariesCacheName, String fileName, String key) {
		final String function = "readArray";
		logger.begin(className, function);
		
		JSONArray result = null;
		
		JSONObject jsonObject = readJsonObject(dictionariesCacheName, fileName);
		
		result = readArray(jsonObject, key);
		
		logger.end(className, function);
		return result;
	}
	
	public static JSONArray readArray(JSONObject jsonObject, String key) {
		final String function = "readArray";
		logger.begin(className, function);
		
		JSONArray result = null;
		
		if ( null != jsonObject ) {
	        JSONValue m = jsonObject.get(key);
	        if (m != null && m.isArray() != null) {
	        	result = m.isArray();
	        }
		} else {
			logger.warn(className, function, "jsonObject IS NULL");
		}
		
		logger.end(className, function);
		return result;
	}
}
