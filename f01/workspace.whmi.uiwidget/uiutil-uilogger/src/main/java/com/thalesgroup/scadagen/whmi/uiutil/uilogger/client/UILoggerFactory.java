package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import java.util.HashMap;
import java.util.Map;

public class UILoggerFactory {

	private static UILoggerFactory instance = null;
	private UILoggerFactory () {}
	public static UILoggerFactory getInstance() {
		if ( null == instance ) instance = new UILoggerFactory();
		return instance;
	}
	public static String DEF_CLASSNAME            = "com.thalesgroup.scadagen.whmi";
	public static String DEF_LOGGER               = "UILogger";
	
	public UILogger getLogger() { return getLogger(DEF_CLASSNAME); }
	public UILogger getLogger(String namespace) { return getLogger(namespace); }
	
	public static Map<String, UILogger_i> loggers = new HashMap<String, UILogger_i>();
	public static void addLogger(final String loggername, UILogger logger) { loggers.put(loggername, logger); }
	public static void removeLogger(final String loggername) { loggers.remove(loggername); }
	public static UILogger_i getLogger(String classname, String loggername) {
		UILogger_i logger = loggers.get(loggername);
		return (null!=logger)?logger:new UILogger(classname);
	}
}
