package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
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
	
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
		logger.begin(function);
		initDatabaseReadingSingletonKey("DatabaseMultiReadingProxySingleton");
		initDatabaseSubscribeSingleton("DatabaseGroupPollingDiffSingleton", 500);
		initDatabaseWritingSingleton("DatabaseWritingSingleton");
		initDatabaseGetFullPathSingleton("DatabaseGetFullPathSingleton");
		logger.end(function);
	}
	
	public void initDatabaseReadingSingletonKey(String strDatabaseReadingSingletonKey) {
		final String function = "initDatabaseReadingSingletonKey";
		logger.begin(function);
		logger.debug(function, "strDatabaseReadingSingletonKey[{}]", strDatabaseReadingSingletonKey);
		
		DatabaseMultiRead_i databaseReading_i = DatabaseMultiReadFactory.get(strDatabaseReadingSingletonKey);
		if ( null != databaseReading_i ) {
			try {
				if ( databaseReading_i instanceof DatabaseSingleton_i ) {
					logger.debug(function, "strDatabaseReadingSingletonKey instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseReading_i).connectOnce();
				} else {
					databaseReading_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(function, "databaseReading_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(function, "databaseReading_i IS NULL");
		}
		logger.end(function);
	}
	
	public void initDatabaseSubscribeSingleton(String strDatabaseSubscribeSingletonKey, int intDatabaseSubscribePeriodMillis) {
		final String function = "initDatabaseSubscribeSingleton";
		logger.begin(function);
		logger.debug(function, "strDatabaseSubscribeSingletonKey[{}]", strDatabaseSubscribeSingletonKey);
		logger.debug(function, "intDatabaseSubscribePeriodMillis[{}]", intDatabaseSubscribePeriodMillis);
		
		DatabaseSubscribe_i databaseSubscribe_i = DatabaseSubscribeFactory.get(strDatabaseSubscribeSingletonKey);
		if ( null != databaseSubscribe_i ) {
			databaseSubscribe_i.setPeriodMillis(intDatabaseSubscribePeriodMillis);
			try {
				if ( databaseSubscribe_i instanceof DatabaseSingleton_i ) {
					logger.debug(function, "databaseSubscribe_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseSubscribe_i).connectOnce();
				} else {
					databaseSubscribe_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(function, "DatabaseSingleton_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(function, "databaseSubscribe_i IS NULL");
		}
		logger.end(function);
	}
	
	public void initDatabaseWritingSingleton(String strDatabaseWritingSingleton) {
		final String function = "initDatabaseWritingSingleton";
		logger.begin(function);
		logger.debug(function, "strDatabaseWritingSingleton[{}]", strDatabaseWritingSingleton);
		
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingSingleton);
		if ( null != databaseWriting_i ) {
			try {
				if ( databaseWriting_i instanceof DatabaseSingleton_i ) {
					logger.debug(function, "databaseWriting_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseWriting_i).connectOnce();
				} else {
					databaseWriting_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(function, "databaseWriting_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(function, "databaseWriting_i IS NULL");
		}
		logger.end(function);
	}
	
	public void initDatabaseGetFullPathSingleton(String strDatabaseGetFullPathSingleton) {
		final String function = "initDatabaseGetFullPathSingleton";
		logger.begin(function);
		logger.debug(function, "strDatabaseGetFullPathSingleton[{}]", strDatabaseGetFullPathSingleton);
		
		DatabaseSingle2SingleRead_i databaseSingle2SingleRead_i = DatabaseGetFullPathFactory.get(strDatabaseGetFullPathSingleton);
		if ( null != databaseSingle2SingleRead_i ) {
			try {
				if ( databaseSingle2SingleRead_i instanceof DatabaseSingleton_i ) {
					logger.debug(function, "databaseSingle2SingleRead_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseSingle2SingleRead_i).connectOnce();
				} else {
					databaseSingle2SingleRead_i.connect();
				}
			} catch (Exception ex) {
				logger.warn(function, "databaseSingle2SingleRead_i init Exception:"+ex.toString());
			}
		} else {
			logger.debug(function, "databaseSingle2SingleRead_i IS NULL");
		}
		logger.end(function);
	}
}
