package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.database;

public interface DatabaseEvent {
	void update(String key, String value[]);
//	abstract void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue);
//	abstract void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue);
}
