package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * @author t0096643
 *
 */
public interface UILoggerFactory_i {
	/**
	 * Get the logger. Should be implemented by Factory owner
	 * 
	 * @param className
	 * @return return logger retrieved
	 */
	UILogger_i getUILogger(String className);
	/**
	 * Get the logger. Should be implemented by Factory owner
	 * 
	 * @param logger
	 * @param className
	 * @return return logger retrieved
	 */
	UILogger_i getUILogger(String logger, String className);
}
