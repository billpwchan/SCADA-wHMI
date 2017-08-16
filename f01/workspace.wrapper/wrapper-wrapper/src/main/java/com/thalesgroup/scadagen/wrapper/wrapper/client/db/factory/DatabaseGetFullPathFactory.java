package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPath;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPathSingleton;

/**
 * Database Get Children Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseGetFullPathFactory {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetFullPathFactory.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
			
			String strDatabaseGetFullPath				= UIWidgetUtil.getClassSimpleName(DatabaseGetFullPath.class.getName());
			String strDatabaseGetFullPathSingleton		= UIWidgetUtil.getClassSimpleName(DatabaseGetFullPathSingleton.class.getName());
			
			if ( 0 == key.compareTo(strDatabaseGetFullPath) ) {
				databaseGetFullPath_i = new DatabaseGetFullPath();
			}
			else if ( 0 == key.compareTo(strDatabaseGetFullPathSingleton) ) {
				databaseGetFullPath_i = DatabaseGetFullPathSingleton.getInstance();
			}
		}
		logger.end(className, function);
		return databaseGetFullPath_i;
	}
}
