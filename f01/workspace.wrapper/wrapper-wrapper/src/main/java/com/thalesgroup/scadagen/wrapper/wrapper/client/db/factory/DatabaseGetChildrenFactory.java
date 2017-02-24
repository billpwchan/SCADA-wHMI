package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildren;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildrenSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetChildrenProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache.DatabaseGetChildrenProxySingleton;

public class DatabaseGetChildrenFactory {
	private static final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenFactory.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static DatabaseSingleRead_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		
		DatabaseSingleRead_i databaseGetChildren_i = null;
		
		if ( null != key ) {
			
			String strDatabaseGetChildren				= UIWidgetUtil.getClassSimpleName(DatabaseGetChildren.class.getName());
			String strDatabaseGetChildrenSingleton		= UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenSingleton.class.getName());
			String strDatabaseGetChildrenProxy			= UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenProxy.class.getName());
			String strDatabaseGetChildrenProxySingleton	= UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenProxySingleton.class.getName());
			
			if ( 0 == key.compareTo(strDatabaseGetChildren) ) {
				databaseGetChildren_i = new DatabaseGetChildren();
			} else if ( 0 == key.compareTo(strDatabaseGetChildrenSingleton) ) {
				databaseGetChildren_i = DatabaseGetChildrenSingleton.getInstance();
			} else if ( 0 == key.compareTo(strDatabaseGetChildrenProxy) ) {
				databaseGetChildren_i = new DatabaseGetChildrenProxy();
			} else if ( 0 == key.compareTo(strDatabaseGetChildrenProxySingleton) ) {
				databaseGetChildren_i = DatabaseGetChildrenProxySingleton.getInstance();
			}
		}
		logger.end(className, function);
		return databaseGetChildren_i;
	}
}
