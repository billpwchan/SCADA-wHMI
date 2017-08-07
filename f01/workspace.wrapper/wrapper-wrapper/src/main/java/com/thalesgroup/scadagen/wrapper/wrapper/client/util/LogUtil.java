package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.Map;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;

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
	public static void logArray(UILogger logger, String className, String function, String name, String [] values) {
		if ( logger.isInfoEnabled() ) {
			if ( null != values ) {
				logger.info(className, function, "values.length[{}]", values.length);
				for ( int i = 0 ; i < values.length ; ++i ) {
					logger.info(className, function, "values({})[{}]", i, values[i]);
				}
			} else {
				logger.warn(className, function, "name[{}] IS NULL", name);
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
	public static void logArray(UILogger logger, String className, String function, String name, Map<String, String> values) {
		if ( logger.isInfoEnabled() ) {
			if ( null != values ) {
				logger.info(className, function, "values.size[{}]", values.size());
				for ( String key : values.keySet() ) {
					logger.info(className, function, "values({})[{}]", key, values.get(key));
				}
			} else {
				logger.warn(className, function, "name[{}] IS NULL", name);
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
	public static void logArray(UILogger logger, String className, String function, String name, int [] values) {
		if ( logger.isInfoEnabled() ) {
			if ( null != values ) {
				logger.info(className, function, "values.length[{}]", values.length);
				for ( int i = 0 ; i < values.length ; ++i ) {
					logger.info(className, function, "values({})[{}]", i, values[i]);
				}
			} else {
				logger.warn(className, function, "name IS NULL");
			}
		}
	}
}
