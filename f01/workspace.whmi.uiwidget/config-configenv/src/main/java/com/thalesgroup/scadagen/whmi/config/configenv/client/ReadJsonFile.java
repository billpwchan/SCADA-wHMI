package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadJsonFile {

	private static final String className = UIWidgetUtil.getClassSimpleName(ReadJsonFile.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static JSONObject readJson(String dictionariesCacheName, String fileName) {
		final String function = "readJson";
		logger.begin(className, function);
		JSONObject jsonObject = null;

		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if (null != dictionariesCache) {
			String data = dictionariesCache.getData(fileName);
			if ( null != data ) {
				jsonObject = ReadJson.readJson(data);
			}
			else {
				logger.warn(className, function, "data IS NULL");
			}
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return jsonObject;
	}

	public static String readString(String dictionariesCacheName, String fileName, String key, String defaultValue) {
		final String function = "readString";
		logger.begin(className, function);

		String string = defaultValue;
		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if ( null != jsonObject ) {
			string = ReadJson.readString(jsonObject, key, defaultValue);
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return string;
	}
	
	public static String getStringFromJsonArray(String dictionariesCacheName, String fileName, String arrayKey, String arrayIndexKeyValue, String arrayIndexKey, String valueKey, String defaultValue) {
		final String function = "getStringFromJsonArray";
		logger.begin(className, function);
		
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] arrayKey[{}] arrayIndexKeyValue[{}] arrayIndexKey[{}] arrayIndexKey[{}] valueKey"
				, new Object[]{dictionariesCacheName, fileName, arrayKey, arrayIndexKeyValue, arrayIndexKey, arrayIndexKey, valueKey});
		
		String value = defaultValue;
		JSONArray jsonArray = ReadJsonFile.readArray(dictionariesCacheName, fileName, arrayKey);
		if ( null != jsonArray ) {
			JSONObject jsonObject = ReadJson.readObject(jsonArray, arrayIndexKeyValue, arrayIndexKey);
			if ( null != jsonObject ) {
				value = ReadJson.readString(jsonObject, valueKey, defaultValue);
			}
			else {
			}
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] arrayKey IS NULL", dictionariesCacheName, fileName);
		}
		
		logger.end(className, function);
		return value;
	}
	
	public static int readInt(String dictionariesCacheName, String fileName, String key, int defaulValue) {
		final String function = "readInt";
		logger.begin(className, function);

		int result = defaulValue;
		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isNumber() != null) {
				result = (int) m.isNumber().doubleValue();
			}
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return result;
	}

	public static boolean readBoolean(String dictionariesCacheName, String fileName, String key, boolean defaulValue) {
		final String function = "readBoolean";
		logger.begin(className, function);

		boolean result = defaulValue;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isBoolean() != null) {
				result = m.isBoolean().booleanValue();
			}
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return result;
	}

	public static JSONArray readArray(String dictionariesCacheName, String fileName, String index) {
		final String function = "readArray";
		logger.begin(className, function);

		JSONArray result = null;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if ( null != jsonObject) {
			result = ReadJson.readArray(jsonObject, index);
		}
		else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return result;
	}
}
