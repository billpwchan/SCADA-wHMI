package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

/**
 * Helper Log Util
 * 
 * @author syau
 *
 */
public class LogUtil {

	/**
	 * Log the String array
	 * 
	 * @param logger logger to log
	 * @param className source class name
	 * @param function source function name
	 * @param name name of the variable
	 * @param values value of the variable
	 */
	public static void logArray(String className, String function, String name, String [] values) {
		UILogger_i logger = UILoggerFactory.getInstance().getUILogger(className);
		if ( logger.isDebugEnabled() ) {
			if ( null != values ) {
				logger.debug(function, "values.length[{}]", values.length);
				for ( int i = 0 ; i < values.length ; ++i ) {
					logger.debug(function, "values({})[{}]", i, values[i]);
				}
			} else {
				logger.warn(function, "name[{}] IS NULL", name);
			}
		}
	}
	
	/**
	 * Log the Map<String, String>
	 * 
	 * @param logger logger to log
	 * @param className source class name
	 * @param function source function name
	 * @param name name of the variable
	 * @param values value of the variable
	 */
	public static void logArray(String className, String function, String name, Map<String, String> values) {
		UILogger_i logger = UILoggerFactory.getInstance().getUILogger(className);
		if ( logger.isDebugEnabled() ) {
			if ( null != values ) {
				logger.debug(function, "values.size[{}]", values.size());
				for ( String key : values.keySet() ) {
					logger.debug(function, "values({})[{}]", key, values.get(key));
				}
			} else {
				logger.warn(function, "name[{}] IS NULL", name);
			}
		}
	}
	
	/**
	 * Log the int array
	 * 
	 * @param logger logger to log
	 * @param className source class name
	 * @param function source function name
	 * @param name name of the variable
	 * @param values value of the variable
	 */
	public static void logArray(String className, String function, String name, int [] values) {
		UILogger_i logger = UILoggerFactory.getInstance().getUILogger(className);
		if ( logger.isDebugEnabled() ) {
			if ( null != values ) {
				logger.debug(function, "values.length[{}]", values.length);
				for ( int i = 0 ; i < values.length ; ++i ) {
					logger.debug(function, "values({})[{}]", i, values[i]);
				}
			} else {
				logger.warn(function, "name IS NULL");
			}
		}
	}
}
