package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPath;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPathSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetFullPathProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetFullPathProxySingleton;

/**
 * Database Get Children Class Factory
 * 
 * @author t0096643
 *
 */
public class DatabaseGetFullPathFactory {

	private static final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(DatabaseGetFullPathFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Get FullPath Class Request
	 * @return    Instance of the Database Get FullPath Object 
	 */
	public static DatabaseSingle2SingleRead_i get(String key) {
		final String function = "get";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		
		DatabaseSingle2SingleRead_i databaseGetFullPath_i = null;
		
		if ( null != key ) {

			if ( UIWidgetUtil.isEqual(key, DatabaseGetFullPath.class.getSimpleName()) ) {
				databaseGetFullPath_i = new DatabaseGetFullPath();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGetFullPathSingleton.class.getSimpleName()) ) {
				databaseGetFullPath_i = DatabaseGetFullPathSingleton.getInstance();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGetFullPathProxy.class.getSimpleName()) ) {
				databaseGetFullPath_i = new DatabaseGetFullPathProxy();
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGetFullPathProxySingleton.class.getSimpleName()) ) {
				databaseGetFullPath_i = DatabaseGetFullPathProxySingleton.getInstance();
			}
			
		}
		logger.end(function);
		return databaseGetFullPath_i;
	}
}
