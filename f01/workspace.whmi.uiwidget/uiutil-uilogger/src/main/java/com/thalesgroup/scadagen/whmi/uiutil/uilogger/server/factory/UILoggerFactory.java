package com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;

public class UILoggerFactory {

	private static UILoggerFactory instance = null;
	public static UILoggerFactory getInstance() { 
		if(null==instance) { instance = new UILoggerFactory(); }
		return instance;
	}

	private UILoggerFactory() {}

	private Map<String, UILogger_i> map = new HashMap<String, UILogger_i>();
	public UILogger_i get(String name) {
		return get(UILogger.class.getSimpleName(), name); 
	}
	public UILogger_i get(String key, String name) {
		UILogger_i ret = null;
		ret = map.get(key);
		if(null==ret) {
			if(0==UILogger.class.getSimpleName().compareTo(key)) {
				ret = new UILogger(name);
			}
		}
		return ret; 
	}
	public void set(String key, UILogger_i logger) { map.put(key, logger); }
}
