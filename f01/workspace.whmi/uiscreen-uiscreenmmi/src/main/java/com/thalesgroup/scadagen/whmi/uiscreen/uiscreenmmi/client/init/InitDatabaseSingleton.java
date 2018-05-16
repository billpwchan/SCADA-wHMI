package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseSubscribeFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class InitDatabaseSingleton {
	
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(InitDatabaseSingleton.class.getName());
	
	public static void initDatabaseReadingSingletonKey(String strDatabaseReadingSingletonKey) {
		final String function = "initDatabaseReadingSingletonKey";
		logger.begin(function);
		logger.debug(function, "strDatabaseReadingSingletonKey[{}]", strDatabaseReadingSingletonKey);
		
		DatabaseMultiRead_i databaseReading_i = DatabaseMultiReadFactory.get(strDatabaseReadingSingletonKey);
		if ( null != databaseReading_i ) {
			if ( databaseReading_i instanceof DatabaseSingleton_i ) {
				logger.debug(function, "strDatabaseReadingSingletonKey instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseReading_i).connectOnce();
			} else {
				databaseReading_i.connect();
			}
		} else {
			logger.debug(function, "databaseReading_i IS NULL");
		}
		logger.end(function);
	}
	
	public static void initDatabaseSubscribeSingleton(String strDatabaseSubscribeSingletonKey, int intDatabaseSubscribePeriodMillis) {
		final String function = "initDatabaseSubscribeSingleton";
		logger.begin(function);
		logger.debug(function, "strDatabaseSubscribeSingletonKey[{}]", strDatabaseSubscribeSingletonKey);
		logger.debug(function, "intDatabaseSubscribePeriodMillis[{}]", intDatabaseSubscribePeriodMillis);
		
		DatabaseSubscribe_i databaseSubscribe_i = DatabaseSubscribeFactory.get(strDatabaseSubscribeSingletonKey);
		if ( null != databaseSubscribe_i ) {
			databaseSubscribe_i.setPeriodMillis(intDatabaseSubscribePeriodMillis);
			if ( databaseSubscribe_i instanceof DatabaseSingleton_i ) {
				logger.debug(function, "databaseSubscribe_i instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseSubscribe_i).connectOnce();
			} else {
				databaseSubscribe_i.connect();
			}
			
		} else {
			logger.debug(function, "databaseSubscribe_i IS NULL");
		}
		logger.end(function);
	}
	
	public static void initDatabaseWritingSingleton(String strDatabaseWritingSingleton) {
		final String function = "initDatabaseWritingSingleton";
		logger.begin(function);
		logger.debug(function, "strDatabaseWritingSingleton[{}]", strDatabaseWritingSingleton);
		
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingSingleton);
		if ( null != databaseWriting_i ) {
			if ( databaseWriting_i instanceof DatabaseSingleton_i ) {
				logger.debug(function, "databaseWriting_i instanceof DatabaseSingleton_i");
				((DatabaseSingleton_i) databaseWriting_i).connectOnce();
			} else {
				databaseWriting_i.connect();
			}
		} else {
			logger.debug(function, "databaseWriting_i IS NULL");
		}
		logger.end(function);
	}
}
