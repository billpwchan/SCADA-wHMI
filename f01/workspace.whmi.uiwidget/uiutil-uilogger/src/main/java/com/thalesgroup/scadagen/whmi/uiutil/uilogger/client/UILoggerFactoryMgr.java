package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Logger Factory to support the runtime load/remove logger
 * 
 * @author syau
 *
 */
public class UILoggerFactoryMgr {
	/**
	 * Internal variable to store to loaded logger
	 */
	private static Map<String, UILoggerFactory_i> loggers = new HashMap<String, UILoggerFactory_i>();
	/**
	 * Add a logger to factory
	 * 
	 * @param name Name of the logger
	 * @param log Loader of the logger
	 */
	public static void addLogger(final String name, UILoggerFactory_i log) { loggers.put(name, log); }
	/**
	 * Check the logger added or not
	 * 
	 * @param name Logger name to check
	 * @return true: logger already added otherwise false
	 */
	public static boolean loggerExists(final String name) { return loggers.containsKey(name); }
	/**
	 * Remove Logger from factory
	 * 
	 * @param name Logger name to remove
	 */
	public static void removeLogger(final String name) { loggers.remove(name); }
	/**
	 * Getter to create logger, default is UILogger
	 * 
	 * @param cls Class name variable to pass to logger
	 * @param name Name of the logger to load
	 * @return
	 */
	public static UILogger_i getLogger(final String cls, final String name) {
		UILogger_i log = null;
		final UILoggerFactory_i factory = loggers.get(name);
		if(null!=factory) log = factory.getUILogger(cls);
		return (null!=log)?log:UILoggerFactory.getInstance().getUILogger(cls);
	}
}
