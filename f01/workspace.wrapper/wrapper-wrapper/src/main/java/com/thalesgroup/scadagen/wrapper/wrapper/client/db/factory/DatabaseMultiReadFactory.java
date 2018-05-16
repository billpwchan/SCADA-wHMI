package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReadingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxySingleton;

/**
 * Database Get Multi Read Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseMultiReadFactory {

	private static final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(DatabaseMultiReadFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Multi Read Class Request
	 * @return    Instance of the Database Multi Read Object 
	 */
	public static DatabaseMultiRead_i get(String key) {
		final String function = "get";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		DatabaseMultiRead_i databaseRead_i = null;
		
		if ( null != key ) {

			if ( 0 == DatabaseMultiReading.class.getSimpleName().compareTo(key) ) {
				databaseRead_i = new DatabaseMultiReading();
			}
			else if ( 0 == DatabaseMultiReadingSingleton.class.getSimpleName().compareTo(key) ) {
				databaseRead_i = DatabaseMultiReadingSingleton.getInstance();
			}
			else if ( 0 == DatabaseMultiReadingProxy.class.getSimpleName().compareTo(key) ) {
				databaseRead_i = new DatabaseMultiReadingProxy();
			}
			else if ( 0 == DatabaseMultiReadingProxySingleton.class.getSimpleName().compareTo(key) ) {
				databaseRead_i = DatabaseMultiReadingProxySingleton.getInstance();
			}
		}
		logger.end(function);
		return databaseRead_i;
	}
}
