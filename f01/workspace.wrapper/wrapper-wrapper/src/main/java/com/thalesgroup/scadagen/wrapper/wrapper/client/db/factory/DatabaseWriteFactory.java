package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write.DatabaseWriting;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write.DatabaseWritingSingleton;

/**
 * Database Writing Class Factory
 * 
 * @author t0096643
 *
 */
public class DatabaseWriteFactory {

	private static final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(DatabaseWriteFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Writing Object
	 * 
	 * @param key Name of the Database Writing Class Request
	 * @return    Instance of the Database Writing Object 
	 */
	public static DatabaseWrite_i get(String key) {
		final String function = "get";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		DatabaseWrite_i databaseWrite_i = null;
		
		if ( null != key ) {

			if ( UIWidgetUtil.isEqual(key, DatabaseWriting.class.getSimpleName()) ) {
				databaseWrite_i = new DatabaseWriting();
			} else if ( UIWidgetUtil.isEqual(key, DatabaseWritingSingleton.class.getSimpleName()) ) {
				databaseWrite_i = DatabaseWritingSingleton.getInstance();
			}
		}
		
		if ( null == databaseWrite_i ) {
			logger.warn(function, "databaseWrite_i IS NULL");
		}
		
		logger.end(function);
		return databaseWrite_i;
	}
}
