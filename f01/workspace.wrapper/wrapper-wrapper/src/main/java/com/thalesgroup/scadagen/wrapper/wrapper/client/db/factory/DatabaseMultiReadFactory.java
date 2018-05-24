package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReadingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxySingleton;

/**
 * Database Get Multi Read Class Factory
 * 
 * @author t0096643
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

			if ( UIWidgetUtil.isEqual(key, DatabaseMultiReading.class.getSimpleName()) ) {
				databaseRead_i = new DatabaseMultiReading();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseMultiReadingSingleton.class.getSimpleName()) ) {
				databaseRead_i = DatabaseMultiReadingSingleton.getInstance();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseMultiReadingProxy.class.getSimpleName()) ) {
				databaseRead_i = new DatabaseMultiReadingProxy();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseMultiReadingProxySingleton.class.getSimpleName()) ) {
				databaseRead_i = DatabaseMultiReadingProxySingleton.getInstance();
			}
		}
		logger.end(function);
		return databaseRead_i;
	}
}
