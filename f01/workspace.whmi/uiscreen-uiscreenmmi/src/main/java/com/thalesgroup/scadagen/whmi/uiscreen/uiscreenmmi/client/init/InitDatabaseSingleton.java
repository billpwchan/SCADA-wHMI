package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseSubscribeFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class InitDatabaseSingleton {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitDatabaseSingleton.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void initDatabaseReadingSingletonKey(String strDatabaseReadingSingletonKey) {
		final String function = "initDatabaseReadingSingletonKey";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseReadingSingletonKey[{}]", strDatabaseReadingSingletonKey);
		
		DatabaseMultiRead_i databaseReading_i = DatabaseMultiReadFactory.get(strDatabaseReadingSingletonKey);
		if ( null != databaseReading_i ) {
			if ( databaseReading_i instanceof DatabaseSingleton_i ) {
				logger.debug(className, function, "strDatabaseReadingSingletonKey instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseReading_i).connectOnce();
			} else {
				databaseReading_i.connect();
			}
		} else {
			logger.debug(className, function, "databaseReading_i IS NULL");
		}
		logger.end(className, function);
	}
	
	public static void initDatabaseSubscribeSingleton(String strDatabaseSubscribeSingletonKey, int intDatabaseSubscribePeriodMillis) {
		final String function = "initDatabaseSubscribeSingleton";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseSubscribeSingletonKey[{}]", strDatabaseSubscribeSingletonKey);
		logger.debug(className, function, "intDatabaseSubscribePeriodMillis[{}]", intDatabaseSubscribePeriodMillis);
		
		DatabaseSubscribe_i databaseSubscribe_i = DatabaseSubscribeFactory.get(strDatabaseSubscribeSingletonKey);
		if ( null != databaseSubscribe_i ) {
			databaseSubscribe_i.setPeriodMillis(intDatabaseSubscribePeriodMillis);
			if ( databaseSubscribe_i instanceof DatabaseSingleton_i ) {
				logger.debug(className, function, "databaseSubscribe_i instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseSubscribe_i).connectOnce();
			} else {
				databaseSubscribe_i.connect();
			}
			
		} else {
			logger.debug(className, function, "databaseSubscribe_i IS NULL");
		}
		logger.end(className, function);
	}
	
	public static void initDatabaseWritingSingleton(String strDatabaseWritingSingleton) {
		final String function = "initDatabaseWritingSingleton";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseWritingSingleton[{}]", strDatabaseWritingSingleton);
		
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingSingleton);
		if ( null != databaseWriting_i ) {
			if ( databaseWriting_i instanceof DatabaseSingleton_i ) {
				logger.debug(className, function, "databaseWriting_i instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseWriting_i).connectOnce();
			} else {
				databaseWriting_i.connect();
			}
		} else {
			logger.debug(className, function, "databaseWriting_i IS NULL");
		}
		logger.end(className, function);
	}
}
