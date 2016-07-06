package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author syau
 * Singleton
 */
public class Settings {
	private HashMap<String, String> hashMap = null;
	private Settings() {
		if ( null == hashMap) hashMap = new HashMap<String, String>();
	}
	private static Settings instance = null;
	public static Settings getInstance() {
		if ( null == instance ) {
			instance = new Settings();
		}
		return instance; 
	}
	public String get(String string){
		return hashMap.get(string);
	}
	public void set(String string, String value){
		hashMap.put(string, value);
	}
	public HashMap<String, String> getMaps() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for ( Map.Entry<String, String> entry : this.hashMap.entrySet() ) {
			hashMap.put(entry.getKey(), entry.getValue());
		}
		return hashMap;
	}
}
