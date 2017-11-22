package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseGetFullPathFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseSubscribeFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class InitDatabase implements Init_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(InitDatabase.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private InitDatabase() {}
	private static InitDatabase instance = null;
	public static InitDatabase getInstance() { 
		if ( null == instance ) instance = new InitDatabase();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initDatabaseSingle() {
		final String function = "initDatabaseSingleton";
		logger.begin(className, function);
		initDatabaseReadingSingletonKey("DatabaseMultiReadingProxySingleton");
		initDatabaseSubscribeSingleton("DatabaseGroupPollingDiffSingleton", 500);
		initDatabaseWritingSingleton("DatabaseWritingSingleton");
		initDatabaseGetFullPathSingleton("DatabaseGetFullPathSingleton");
		logger.end(className, function);
	}
	
	public void initDatabaseReadingSingletonKey(String strDatabaseReadingSingletonKey) {
		final String function = "initDatabaseReadingSingletonKey";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseReadingSingletonKey[{}]", strDatabaseReadingSingletonKey);
		
		DatabaseMultiRead_i databaseReading_i = DatabaseMultiReadFactory.get(strDatabaseReadingSingletonKey);
		if ( null != databaseReading_i ) {
			try {
				if ( databaseReading_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "strDatabaseReadingSingletonKey instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseReading_i).connectOnce();
				} else {
					databaseReading_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(className, function, "databaseReading_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(className, function, "databaseReading_i IS NULL");
		}
		logger.end(className, function);
	}
	
	public void initDatabaseSubscribeSingleton(String strDatabaseSubscribeSingletonKey, int intDatabaseSubscribePeriodMillis) {
		final String function = "initDatabaseSubscribeSingleton";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseSubscribeSingletonKey[{}]", strDatabaseSubscribeSingletonKey);
		logger.debug(className, function, "intDatabaseSubscribePeriodMillis[{}]", intDatabaseSubscribePeriodMillis);
		
		DatabaseSubscribe_i databaseSubscribe_i = DatabaseSubscribeFactory.get(strDatabaseSubscribeSingletonKey);
		if ( null != databaseSubscribe_i ) {
			databaseSubscribe_i.setPeriodMillis(intDatabaseSubscribePeriodMillis);
			try {
				if ( databaseSubscribe_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "databaseSubscribe_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseSubscribe_i).connectOnce();
				} else {
					databaseSubscribe_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(className, function, "DatabaseSingleton_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(className, function, "databaseSubscribe_i IS NULL");
		}
		logger.end(className, function);
	}
	
	public void initDatabaseWritingSingleton(String strDatabaseWritingSingleton) {
		final String function = "initDatabaseWritingSingleton";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseWritingSingleton[{}]", strDatabaseWritingSingleton);
		
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingSingleton);
		if ( null != databaseWriting_i ) {
			try {
				if ( databaseWriting_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "databaseWriting_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseWriting_i).connectOnce();
				} else {
					databaseWriting_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(className, function, "databaseWriting_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(className, function, "databaseWriting_i IS NULL");
		}
		logger.end(className, function);
	}
	
	public void initDatabaseGetFullPathSingleton(String strDatabaseGetFullPathSingleton) {
		final String function = "initDatabaseGetFullPathSingleton";
		logger.begin(className, function);
		logger.debug(className, function, "strDatabaseGetFullPathSingleton[{}]", strDatabaseGetFullPathSingleton);
		
		DatabaseSingle2SingleRead_i databaseSingle2SingleRead_i = DatabaseGetFullPathFactory.get(strDatabaseGetFullPathSingleton);
		if ( null != databaseSingle2SingleRead_i ) {
			try {
				if ( databaseSingle2SingleRead_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "databaseSingle2SingleRead_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseSingle2SingleRead_i).connectOnce();
				} else {
					databaseSingle2SingleRead_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(className, function, "databaseSingle2SingleRead_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(className, function, "databaseSingle2SingleRead_i IS NULL");
		}
		logger.end(className, function);
	}
}
