package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author syau
 * Singleton
 */
public class UIPanelSetting {
	private HashMap<String, String> hashMap = null;
	private UIPanelSetting() {
		if ( null == hashMap) hashMap = new HashMap<String, String>();
	}
	private static UIPanelSetting instance = null;
	public static UIPanelSetting getInstance() {
		if ( null == instance ) {
			instance = new UIPanelSetting();
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
