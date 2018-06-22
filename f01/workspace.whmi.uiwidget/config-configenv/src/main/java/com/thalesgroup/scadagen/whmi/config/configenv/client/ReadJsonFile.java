package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class ReadJsonFile {

	private static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(ReadJsonFile.class.getName());
	
	public static JSONObject readJson(String dictionariesCacheName, String fileName) {
		final String f = "readJson";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}]", new Object[]{dictionariesCacheName, fileName});
		
		JSONObject ret = null;

		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if (null != dictionariesCache) {
			String data = dictionariesCache.getData(fileName);
			if ( null != data ) {
				ret = ReadJson.readJson(data);
			}
			else {
				logger.warn(f, "data IS NULL");
			}
		}
		else {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(f);
		return ret;
	}

	public static String readString(String dictionariesCacheName, String fileName, String key, String defaultValue) {
		final String f = "readString";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}] defaultValue[{}]", new Object[]{dictionariesCacheName, fileName, defaultValue});

		String string = defaultValue;
		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if ( null != jsonObject ) {
			string = ReadJson.readString(jsonObject, key, defaultValue);
		}
		else {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(f);
		return string;
	}
	
	public static String getStringFromJsonArray(String dictionariesCacheName, String fileName, String arrayKey, String arrayIndexKeyValue, String arrayIndexKey, String valueKey, String defaultValue) {
		final String f = "getStringFromJsonArray";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}] arrayKey[{}] arrayIndexKeyValue[{}] arrayIndexKey[{}] arrayIndexKey[{}] valueKey"
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
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] arrayKey IS NULL", dictionariesCacheName, fileName);
		}
		
		logger.end(f);
		return value;
	}
	
	public static int readInt(String dictionariesCacheName, String fileName, String key, int defaultValue) {
		final String f = "readInt";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}] defaultValue[{}]", new Object[]{dictionariesCacheName, fileName, defaultValue});

		int result = defaultValue;
		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isNumber() != null) {
				result = (int) m.isNumber().doubleValue();
			}
		}
		else {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(f);
		return result;
	}

	public static boolean readBoolean(String dictionariesCacheName, String fileName, String key, boolean defaultValue) {
		final String f = "readBoolean";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}] defaultValue[{}]", new Object[]{dictionariesCacheName, fileName, defaultValue});

		boolean ret = defaultValue;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isBoolean() != null) {
				ret = m.isBoolean().booleanValue();
			}
		}
		else {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(f);
		return ret;
	}

	public static JSONArray readArray(String dictionariesCacheName, String fileName, String index) {
		final String f = "readArray";
		logger.begin(f);
		
		logger.trace(f, "dictionariesCacheName[{}] fileName[{}] index[{}]", new Object[]{dictionariesCacheName, fileName, index});

		JSONArray ret = null;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);
		if ( null != jsonObject) {
			ret = ReadJson.readArray(jsonObject, index);
		}
		else {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] jsonObject IS NULL", dictionariesCacheName, fileName);
		}

		logger.end(f);
		return ret;
	}
}
