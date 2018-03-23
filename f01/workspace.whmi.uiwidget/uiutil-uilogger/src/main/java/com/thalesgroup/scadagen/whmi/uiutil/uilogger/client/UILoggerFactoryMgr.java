package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import java.util.HashMap;
import java.util.Map;

public class UILoggerFactoryMgr {
	public static Map<String, UILoggerFactory_i> loggers = new HashMap<String, UILoggerFactory_i>();
	public static void addLogger(final String name, UILoggerFactory_i log) { loggers.put(name, log); }
	public static boolean loggerExists(final String name) { return loggers.containsKey(name); }
	public static void removeLogger(final String name) { loggers.remove(name); }
	public static UILogger_i getLogger(final String cls, final String name) {
		UILogger_i log = null;
		final UILoggerFactory_i factory = loggers.get(name);
		if(null!=factory) log = factory.getLogger(cls);
		return (null!=log)?log:UILoggerFactory.getInstance().getLogger(cls);
	}
}
