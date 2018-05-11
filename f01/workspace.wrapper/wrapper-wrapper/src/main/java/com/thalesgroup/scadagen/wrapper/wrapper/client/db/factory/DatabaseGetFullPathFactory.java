package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPath;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPathSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetFullPathProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetFullPathProxySingleton;

/**
 * Database Get Children Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseGetFullPathFactory {
	
	private static final String className = DatabaseGetFullPathFactory.class.getSimpleName();
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(DatabaseGetFullPathFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Get FullPath Class Request
	 * @return    Instance of the Database Get FullPath Object 
	 */
	public static DatabaseSingle2SingleRead_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		
		DatabaseSingle2SingleRead_i databaseGetFullPath_i = null;
		
		if ( null != key ) {

			if ( 0 == DatabaseGetFullPath.class.getSimpleName().compareTo(key) ) {
				databaseGetFullPath_i = new DatabaseGetFullPath();
			}
			else if ( 0 == DatabaseGetFullPathSingleton.class.getSimpleName().compareTo(key) ) {
				databaseGetFullPath_i = DatabaseGetFullPathSingleton.getInstance();
			}
			else if ( 0 == DatabaseGetFullPathProxy.class.getSimpleName().compareTo(key) ) {
				databaseGetFullPath_i = new DatabaseGetFullPathProxy();
			}
			else if ( 0 == DatabaseGetFullPathProxySingleton.class.getSimpleName().compareTo(key) ) {
				databaseGetFullPath_i = DatabaseGetFullPathProxySingleton.getInstance();
			}
			
		}
		logger.end(className, function);
		return databaseGetFullPath_i;
	}
}
