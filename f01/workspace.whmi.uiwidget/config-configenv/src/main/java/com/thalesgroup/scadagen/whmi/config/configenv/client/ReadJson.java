package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadJson {

	private static final String className = UIWidgetUtil.getClassSimpleName(ReadJson.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public static JSONObject readJson(String json) {
		final String function = "readJson";
		logger.begin(className, function);
		JSONObject jsonObject = null;

		logger.trace(className, function, "json[{}]", json);

		if (json != null) {
			if (JsonUtils.safeToEval(json)) {
				jsonObject = JSONParser.parseStrict(json).isObject();
			} else {
				logger.warn(className, function, "json IS NOT SAFE TO EVAL");
			}
		} else {
			logger.warn(className, function, "json IS NULL");
		}

		logger.end(className, function);
		return jsonObject;
	}
	
	public static JSONObject readJson(String dictionariesCacheName, String fileName) {
		final String function = "readJson";
		logger.begin(className, function);
		JSONObject jsonObject = null;

		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if (null != dictionariesCache) {

			String data = dictionariesCache.getData(fileName);
			
			if ( null != data ) {
				jsonObject = readJson(data);
			} else {
				logger.warn(className, function, "data IS NULL");
			}

		} else {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] dictionariesCache IS NULL",
					dictionariesCacheName, fileName);
		}

		logger.end(className, function);
		return jsonObject;
	}

	public static String readString(JSONValue jsonValue) {
		final String function = "readString";
		logger.begin(className, function);
		String string = null;
		if (jsonValue != null && jsonValue.isString() != null) {
			string = jsonValue.isString().stringValue();
		} else {
			logger.warn(className, function, "jsonValue[{}] IS INVALID", jsonValue);
		}
		logger.end(className, function);
		return string;
	}
	
	public static String readString(JSONObject jsonObject, String key) {
		final String function = "readString";
		logger.begin(className, function);
		String string = null;
		if (null != jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			string = readString(jsonValue);
		} else {
			logger.debug(className, function, "jsonObject[{}] IS NULL", jsonObject);
		}
		logger.end(className, function);
		return string;
	}

	public static String readString(String dictionariesCacheName, String fileName, String key, String defaulValue) {
		final String function = "readString";
		logger.begin(className, function);

		String string = defaulValue;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);

		string = readString(jsonObject, key);

		logger.end(className, function);
		return string;
	}
	
	public static String getStringFromJsonArray(String dictionariesCacheName, String fileName, String arrayKey, String arrayIndexKeyValue, String arrayIndexKey, String valueKey) {
		final String function = "getStringFromJsonArray";
		logger.begin(className, function);
		
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] arrayKey[{}] arrayIndexKeyValue[{}] arrayIndexKey[{}] arrayIndexKey[{}] valueKey"
				, new Object[]{dictionariesCacheName, fileName, arrayKey, arrayIndexKeyValue, arrayIndexKey, arrayIndexKey, valueKey});
		
		JSONArray jsonArray = ReadJson.readArray(dictionariesCacheName, fileName, arrayKey);
		
		JSONObject jsonObject = ReadJson.readObject(jsonArray, arrayIndexKeyValue, arrayIndexKey);
		
		String value = ReadJson.readString(jsonObject, valueKey);
		logger.end(className, function);
		return value;
	}
	
	public static String getStringFromJson(String dictionariesCacheNameValue, String fileNameValue, String key) {
		final String function = "getStringFromJson";
		logger.begin(className, function);
		logger.debug(className, function, "dictionariesCacheNameValue[{}] fileNameValue[{}] key[{}]", new Object[]{dictionariesCacheNameValue, fileNameValue, key});
		
		String value = ReadJson.readString(dictionariesCacheNameValue, fileNameValue, key, null);
		
		logger.debug(className, function, "dictionariesCacheNameValue[{}] fileNameValue[{}] key[{}] value[{}]", new Object[]{dictionariesCacheNameValue, fileNameValue, key, value});
		logger.end(className, function);
		return value;
	}

	public static int readInt(JSONValue jsonValue, int defaultValue) {
		final String function = "readInt";
		logger.begin(className, function);
		int result = defaultValue;
		if (jsonValue != null && jsonValue.isNumber() != null) {
			result = (int) jsonValue.isNumber().doubleValue();
		} else {
			logger.warn(className, function, "jsonValue[{}] IS INVALID", jsonValue);
		}
		logger.end(className, function);
		return result;
	}
	
	public static int readInt(JSONObject jsonObject, String key, int defaultValue) {
		final String function = "readInt";
		logger.begin(className, function);
		int result = defaultValue;
		
		if (null != jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			result = readInt(jsonValue, defaultValue);
		} else {
			logger.warn(className, function, "jsonObject IS NULL");
		}
		
		logger.end(className, function);
		return result;
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
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL",
					dictionariesCacheName);
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
		} else {
			logger.warn(className, function, "dictionariesCacheName[{}], dictionariesCache IS NULL",
					dictionariesCacheName);
		}

		logger.end(className, function);
		return result;
	}

	public static JSONArray readArray(String dictionariesCacheName, String fileName, String index) {
		final String function = "readArray";
		logger.begin(className, function);

		JSONArray result = null;

		JSONObject jsonObject = readJson(dictionariesCacheName, fileName);

		result = readArray(jsonObject, index);

		logger.end(className, function);
		return result;
	}

	public static JSONArray readArray(JSONObject jsonObject, String index) {
		final String function = "readArray";
		logger.begin(className, function);

		JSONArray result = null;

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(index);
			if (m != null && m.isArray() != null) {
				result = m.isArray();
			}
		} else {
			logger.warn(className, function, "jsonObject IS NULL");
		}

		logger.end(className, function);
		return result;
	}

	public static JSONObject readObject(JSONArray jsonArray, String key, String value) {
		final String function = "readObject";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] value[{}]", key, value);

		JSONObject object = null;
		for (int i = 0; i < jsonArray.size(); ++i) {
			JSONValue jsonValue = jsonArray.get(i);
			JSONObject tJsonObect = jsonValue.isObject();
			if (null != tJsonObect) {
				JSONValue keyValue = tJsonObect.get(key);
				if (null != keyValue) {
					JSONString tValue = keyValue.isString();
					if (null != tValue) {
						String sValue = tValue.stringValue();
						if (sValue.equals(value)) {
							object = tJsonObect;
							break;
						}
					} else {
						logger.warn(className, function, "tValue IS NULL");
					}
				} else {
					logger.warn(className, function, "keyValue IS NULL");
				}
			} else {
				logger.warn(className, function, "tJsonObect IS NULL");
			}
		}

		logger.end(className, function);
		return object;
	}

}
