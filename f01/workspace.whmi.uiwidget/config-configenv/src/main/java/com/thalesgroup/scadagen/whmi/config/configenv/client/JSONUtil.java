package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class JSONUtil {
	
	public static String convertJsonObject2String(JSONObject jsonObject) {
		String ret = null;
		if(null!=jsonObject) ret = jsonObject.toString();
		return ret;
	}
	
	public static JSONObject convertMapToJSONObject(Map<String, Object> map) {
		JSONObject jsonObject = new JSONObject();
		if(null!=map) {
			for(Entry<String, Object> keyValue: map.entrySet()) {
				String k = keyValue.getKey();
				Object v = keyValue.getValue();
				if(null==k||null==v) {} 
				else if (0 == k.length()) {}
//				else if (v instanceof Number)  { jsonObject.put(k, new JSONNumber(((Number)v).doubleValue())); }
				else if (v instanceof Short)   { jsonObject.put(k, new JSONNumber((Short)v)); }
				else if (v instanceof Integer) { jsonObject.put(k, new JSONNumber((Integer)v)); }
				else if (v instanceof Double)  { jsonObject.put(k, new JSONNumber((Double)v)); }
				else if (v instanceof Long)    { jsonObject.put(k, new JSONNumber((Long)v)); }
				else if (v instanceof String)  { jsonObject.put(k, new JSONString((String)v)); }
			}
		}
		return jsonObject;
	}
	
	public static JSONArray convertStringsToJSONArray(String string[]) {
		JSONArray jsonArray = new JSONArray();
		for ( int i = 0 ; i < string.length ; ++i ) {
			jsonArray.set(i, new JSONString(string[i]));
		}
		return jsonArray;
	}
	
	public static int[] convertStringToInts(String strings[]) {
		int ints [] = new int[strings.length];
		for ( int i = 0 ; i < strings.length ; ++i ) {
			ints[i] = Integer.parseInt(strings[i]);
		}
		return ints;
	}
	
	public static JSONArray convertIntsToJSONArray(int integer[]) {
		JSONArray jsonArray = new JSONArray();
		for ( int i = 0 ; i < integer.length ; ++i ) {
			jsonArray.set(i, new JSONNumber(integer[i]));
		}
		return jsonArray;
	}
}
