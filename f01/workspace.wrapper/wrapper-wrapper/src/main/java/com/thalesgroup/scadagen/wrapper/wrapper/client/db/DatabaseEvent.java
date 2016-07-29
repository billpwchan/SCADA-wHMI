package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

public interface DatabaseEvent {
	void update(String key, String value[]);
//	abstract void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue);
//	abstract void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue);
}
