package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * @author syau
 *
 */
public interface UILoggerFactory_i {
	/**
	 * Get the logger. Should be implemented by Factory owner
	 * 
	 * @param className
	 * @return return logger retrieved
	 */
	UILogger_i getUILogger(final String className);
}
