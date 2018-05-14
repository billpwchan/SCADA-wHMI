package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2MultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildren;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildrenSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetChildrenProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetChildrenProxySingleton;

/**
 * Database Get Children Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseGetChildrenFactory {
	
	private static final String className = DatabaseGetChildrenFactory.class.getSimpleName();
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(DatabaseGetChildrenFactory.class.getSimpleName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Get Children Class Request
	 * @return    Instance of the Database Get Children Object 
	 */
	public static DatabaseSingle2MultiRead_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		
		DatabaseSingle2MultiRead_i databaseGetChildren_i = null;
		
		if ( null != key ) {

			if ( 0 == DatabaseGetChildren.class.getSimpleName().compareTo(key) ) {
				databaseGetChildren_i = new DatabaseGetChildren();
			} else if ( 0 == DatabaseGetChildrenSingleton.class.getSimpleName().compareTo(key) ) {
				databaseGetChildren_i = DatabaseGetChildrenSingleton.getInstance();
			} else if ( 0 == DatabaseGetChildrenProxy.class.getSimpleName().compareTo(key) ) {
				databaseGetChildren_i = new DatabaseGetChildrenProxy();
			} else if ( 0 == DatabaseGetChildrenProxySingleton.class.getSimpleName().compareTo(key) ) {
				databaseGetChildren_i = DatabaseGetChildrenProxySingleton.getInstance();
			}
		}
		logger.end(className, function);
		return databaseGetChildren_i;
	}
}
