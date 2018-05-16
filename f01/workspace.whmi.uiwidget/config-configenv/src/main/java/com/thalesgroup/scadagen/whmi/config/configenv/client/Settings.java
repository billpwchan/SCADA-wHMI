package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

/**
 * @author t0096643
 * Singleton
 */
public class Settings {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, String> map = null;
	private Settings() {
		if ( null == map) { map = new HashMap<String, String>(); }
	}
	private static Settings instance = null;
	public static Settings getInstance() {
		if ( null == instance ) { instance = new Settings(); }
		return instance; 
	}
	public String get(String string){ return this.map.get(string); }
	public void set(String string, String value){ this.map.put(string, value); }
	public Map<String, String> getMaps() {
		Map<String, String> map = new HashMap<String, String>();
		for ( Map.Entry<String, String> entry : this.map.entrySet() ) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	
	public void dumpSetting(Map<String, String> map) {
		final String function = "dumpSetting";
		logger.begin(function);
		// Debug
		if ( logger.isDebugEnabled() ) {
			if ( null != map ) {
				for ( Map.Entry<String, String> entry : map.entrySet() ) {
					logger.debug(function, "Debug key[{}] value[{}]", entry.getKey(), entry.getValue());
				}
			} else {
				logger.debug(function, "map IS NULL");
			}
		}
		// End of Debug
		logger.end(function);
	}
	
}
