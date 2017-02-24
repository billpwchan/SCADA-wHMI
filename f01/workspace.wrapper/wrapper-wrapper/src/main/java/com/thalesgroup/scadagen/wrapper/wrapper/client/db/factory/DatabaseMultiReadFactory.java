package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReadingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache.DatabaseMultiReadingProxySingleton;

public class DatabaseMultiReadFactory {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(DatabaseMultiReadFactory.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static DatabaseMultiRead_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		DatabaseMultiRead_i databaseRead_i = null;
		
		if ( null != key ) {
			
			String strDatabaseMultiReading					= UIWidgetUtil.getClassSimpleName(DatabaseMultiReading.class.getName());
			String strDatabaseMultiReadingSingleton			= UIWidgetUtil.getClassSimpleName(DatabaseMultiReadingSingleton.class.getName());
			String strDatabaseMultiReadingProxy				= UIWidgetUtil.getClassSimpleName(DatabaseMultiReadingProxy.class.getName());
			String strDatabaseMultiReadingProxySingleton	= UIWidgetUtil.getClassSimpleName(DatabaseMultiReadingProxySingleton.class.getName());
			
			if ( 0 == key.compareTo(strDatabaseMultiReading) ) {
				databaseRead_i = new DatabaseMultiReading();
			}
			else if ( 0 == key.compareTo(strDatabaseMultiReadingSingleton) ) {
				databaseRead_i = DatabaseMultiReadingSingleton.getInstance();
			}
			else if ( 0 == key.compareTo(strDatabaseMultiReadingProxy) ) {
				databaseRead_i = new DatabaseMultiReadingProxy();
			}
			else if ( 0 == key.compareTo(strDatabaseMultiReadingProxySingleton) ) {
				databaseRead_i = DatabaseMultiReadingProxySingleton.getInstance();
			}
		}
		logger.end(className, function);
		return databaseRead_i;
	}
}
