package com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.util;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class JSONUtil {

	public static JSONValue getJSONValue(JSONObject request, String key) {
		JSONValue jsonValue = null;
		jsonValue = request.get(key);
		return jsonValue;
	}
	
	public static String getString(JSONValue jsonValue) {
		String string = null;
		if ( null != jsonValue ) {
			JSONString jsonString = jsonValue.isString();
			if ( null != jsonString ) {
				string = jsonString.stringValue();
			}
		}
		return string;
	}
}
