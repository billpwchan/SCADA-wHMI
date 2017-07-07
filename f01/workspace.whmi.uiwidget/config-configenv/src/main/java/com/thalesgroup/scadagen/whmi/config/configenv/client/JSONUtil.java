package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

public class JSONUtil {
	
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
