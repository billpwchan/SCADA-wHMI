package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class ReadJson {

	private static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(ReadJson.class.getName());

	public static JSONObject readJson(String json) {
		final String function = "readJson";
		logger.begin(function);
		JSONObject jsonObject = null;

		logger.trace(function, "json[{}]", json);

		if (json != null) {
			if (JsonUtils.safeToEval(json)) {
				jsonObject = JSONParser.parseStrict(json).isObject();
			} else {
				logger.warn(function, "json IS NOT SAFE TO EVAL");
			}
		} else {
			logger.warn(function, "json IS NULL");
		}

		logger.end(function);
		return jsonObject;
	}

	public static String readString(JSONObject jsonObject, String key, String defaultValue) {
		final String function = "readString";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}] defaultValue[{}]", new Object[]{jsonObject, key, defaultValue});
		String result = defaultValue;
		if (null != jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			if (jsonValue != null && jsonValue.isString() != null) {
				result = jsonValue.isString().stringValue();
			} else {
				logger.warn(function, "jsonValue[{}] IS INVALID", jsonValue);
			}
		} else {
			logger.debug(function, "jsonObject[{}] IS NULL", jsonObject);
		}
		logger.debug(function, "result[{}]", result);
		logger.end(function);
		return result;
	}

	public static int readInt(JSONObject jsonObject, String key, int defaultValue) {
		final String function = "readInt";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}] defaultValue[{}]", new Object[]{jsonObject, key, defaultValue});
		int result = defaultValue;
		
		if (null != jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			if (jsonValue != null && jsonValue.isNumber() != null) {
				result = (int) jsonValue.isNumber().doubleValue();
			} else {
				logger.warn(function, "jsonValue[{}] IS INVALID", jsonValue);
			}
		} else {
			logger.warn(function, "jsonObject IS NULL");
		}
		logger.debug(function, "result[{}]", result);
		logger.end(function);
		return result;
	}
	
	public static boolean readBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
		final String function = "readBoolean";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}] defaultValue[{}]", new Object[]{jsonObject, key, defaultValue});
		boolean result = defaultValue;
		
		if (null != jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			if (jsonValue != null && jsonValue.isBoolean() != null) {
				result = jsonValue.isBoolean().booleanValue();
			} else {
				logger.warn(function, "jsonValue[{}] IS INVALID", jsonValue);
			}
		} else {
			logger.warn(function, "jsonObject IS NULL");
		}
		
		logger.debug(function, "result[{}]", result);
		logger.end(function);
		return result;
	}

	public static JSONArray readArray(JSONObject jsonObject, String key) {
		final String function = "readArray";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}]", jsonObject, key);
		JSONArray result = null;

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isArray() != null) {
				result = m.isArray();
			}
		} else {
			logger.warn(function, "jsonObject IS NULL");
		}

		logger.end(function);
		return result;
	}
	
	public static String [] readStringArray(JSONArray jsonArray) {
		final String function = "readObject";
		logger.begin(function);
		logger.trace(function, "jsonArray[{}]", jsonArray);

		String array [] = null;
		if ( null != jsonArray ) {
			array = new String[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); ++i) {
				JSONValue jsonValue = jsonArray.get(i);
				if (jsonValue != null && jsonValue.isString() != null) {
					array[i] = jsonValue.isString().stringValue();
				} else {
					logger.warn(function, "jsonValue[{}] IS INVALID", jsonValue);
				}
			}
		} else {
			logger.warn(function, "jsonArray IS NULL");
		}

		logger.end(function);
		return array;
	}
	
	public static String [] readStringArray(JSONObject jsonObject, String key) {
		final String function = "readArray";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}]", jsonObject, key);
		String array [] = null;

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isArray() != null) {
				array = readStringArray(m.isArray());
			}
		} else {
			logger.warn(function, "jsonObject IS NULL");
		}

		logger.end(function);
		return array;
	}
	
	public static int [] readIntsFromArray(JSONArray jsonArray, String key) {	
		final String function = "readIntsFromArray";
		logger.begin(function);
		logger.trace(function, "jsonArray[{}] key[{}]", new Object[]{jsonArray, key});

		int ints [] = null;
		
		if ( null != jsonArray ) {
			ints = new int[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); ++i) {
				if ( null != jsonArray.get(i) ) {
					JSONObject object = jsonArray.get(i).isObject();
					if (null != object.get(key)) {
						if ( null != object.get(key).isNumber() ) {
							ints[i] = (int) object.get(key).isNumber().doubleValue();
						}
					} else {
						logger.warn(function, "object.get({}) IS NULL", key);
					}
				} else {
					logger.warn(function, "jsonArray.get({}) IS NULL", i);
				}
			}
		} else {
			logger.warn(function, "jsonArray IS NULL");
		}
		
		return ints;
	}
	
	public static int [] readIntArray(JSONArray jsonArray) {
		final String function = "readIntArray";
		logger.begin(function);
		logger.trace(function, "jsonArray[{}]", jsonArray);

		int array [] = null;
		if ( null != jsonArray ) {
			array = new int[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); ++i) {
				JSONValue jsonValue = jsonArray.get(i);
				if (jsonValue != null && jsonValue.isNumber() != null) {
					array[i] = (int) jsonValue.isNumber().doubleValue();
				} else {
					logger.warn(function, "jsonValue[{}] IS INVALID", jsonValue);
				}
			}
		} else {
			logger.warn(function, "jsonArray IS NULL");
		}

		logger.end(function);
		return array;
	}
	
	public static int [] readIntArray(JSONObject jsonObject, String key) {
		final String function = "readIntArray";
		logger.begin(function);
		logger.trace(function, "jsonObject[{}] key[{}]", jsonObject, key);
		int array [] = null;

		if (null != jsonObject) {
			JSONValue m = jsonObject.get(key);
			if (m != null && m.isArray() != null) {
				array = readIntArray(m.isArray());
			}
		} else {
			logger.warn(function, "jsonObject IS NULL");
		}

		logger.end(function);
		return array;
	}
	
	public static JSONObject readObject(JSONObject json, String key) {
		final String function = "readObject";
		logger.begin(function);
		logger.trace(function, "json[{}] key[{}]", new Object[]{json, key});
		
		JSONValue jsonValue = json.get(key);
		JSONObject jsonObject = jsonValue.isObject();

		logger.end(function);
		return jsonObject;
	}

	public static JSONObject readObject(JSONArray jsonArray, String indexKey, String keyValue) {
		final String function = "readObject";
		logger.begin(function);
		logger.trace(function, "jsonArray[{}] indexKey[{}] keyValue[{}]", new Object[]{jsonArray, indexKey, keyValue});

		JSONObject object = null;
		for (int i = 0; i < jsonArray.size(); ++i) {
			JSONValue jsonValue = jsonArray.get(i);
			JSONObject tJsonObect = jsonValue.isObject();
			if (null != tJsonObect) {
				if (null != tJsonObect.get(indexKey)) {
					JSONString tValue = tJsonObect.get(indexKey).isString();
					if (null != tValue) {
						String sValue = tValue.stringValue();
						if (sValue.equals(keyValue)) {
							object = tJsonObect;
							break;
						}
					} else {
						logger.warn(function, "tValue IS NULL");
					}
				} else {
					logger.warn(function, "keyValue IS NULL");
				}
			} else {
				logger.warn(function, "tJsonObect IS NULL");
			}
		}

		logger.end(function);
		return object;
	}
	
	public static Map<String, Integer> readObjectEntrySetStrInt(JSONArray jsonArray) {
		String f = "readObjectEntrySetStrInt";
		logger.begin(f);
		
		Map<String, Integer> map = null;

		if(null!=jsonArray) {
			map = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < jsonArray.size(); ++i) {
				JSONValue jsonValue = jsonArray.get(i);
				if (null != jsonValue) {
					JSONObject tJsonObect = jsonValue.isObject();
					if (null != tJsonObect) {
						Set<String> set = tJsonObect.keySet();
						if(null!=set) {
							Iterator<String> iter = set.iterator();
							if(null!=iter) {
								while(iter.hasNext()) {
									String k = iter.next();
									if(null!=k) {
										JSONValue jv = tJsonObect.get(k);
										if(null!=jv) {
											JSONNumber jn = jv.isNumber();
											if(null!=jn) {
												int v = (int) jn.doubleValue();
												logger.info(f, "k[{}] v[{}]", k, v);
												map.put(k, v);
											} else {
												logger.warn(f, "k IS NULL");
											}
										} else {
											logger.warn(f, "jv IS NULL");
										}
									} else {
										logger.warn(f, "k IS NULL");
									}
								}
							} else {
								logger.warn(f, "iter IS NULL");
							}
						} else {
							logger.warn(f, "set IS NULL");
						}
					} else {
						logger.warn(f, "tJsonObect IS NULL");
					}
				} else {
					logger.warn(f, "jsonValue IS NULL");
				}
			}
		} else {
			logger.warn(f, "jsonArray IS NULL");
		}

		logger.end(f);
		return map;
	}

}
