package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write.DatabaseWriting;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write.DatabaseWritingSingleton;

/**
 * Database Writing Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseWriteFactory {
	
	private static final String className = DatabaseWriteFactory.class.getSimpleName();
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(DatabaseWriteFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Writing Class Request
	 * @return    Instance of the Database Writing Object 
	 */
	public static DatabaseWrite_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		DatabaseWrite_i databaseWrite_i = null;
		
		if ( null != key ) {

			if ( 0 == DatabaseWriting.class.getSimpleName().compareTo(key) ) {
				databaseWrite_i = new DatabaseWriting();
			} else if ( 0 == DatabaseWritingSingleton.class.getSimpleName().compareTo(key) ) {
				databaseWrite_i = DatabaseWritingSingleton.getInstance();
			}
		}
		
		if ( null == databaseWrite_i ) {
			logger.warn(className, function, "databaseWrite_i IS NULL");
		}
		
		logger.end(className, function);
		return databaseWrite_i;
	}
}
