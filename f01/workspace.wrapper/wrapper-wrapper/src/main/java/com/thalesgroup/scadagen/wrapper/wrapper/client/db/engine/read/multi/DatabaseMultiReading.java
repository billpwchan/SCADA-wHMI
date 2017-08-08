package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database.ScsRTDBComponentAccessResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;

/**
 * Implementation the Database Multi Reading Operation
 * 
 * @author syau
 *
 */
public class DatabaseMultiReading implements DatabaseMultiRead_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseMultiReading.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	/**
	 * Operation Request Storage
	 */
	private Map<String, ReadingRequest> readingRequests = new HashMap<String, ReadingRequest>();
	
	public class ReadingRequest {
		public String key = null;
		public String env = null;
		public String[] dbaddresses = null;
		public String[] values = null;
		public DatabasePairEvent_i databaseEvent = null;
		public ReadingRequest(String key, String env, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
			this.key = key;
			this.env = env;
			this.dbaddresses = dbaddresses;
			this.databaseEvent = databaseEvent;
		}
	}
	
	/**
	 * Instance for the Database Wrapper class
	 */
	private Database database = new Database();

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		database.setScsRTDBComponentAccessResult(new ScsRTDBComponentAccessResult() {
			
			@Override
			public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
				final String function = "setReadResult";
				logger.begin(className, function);
				logger.debug(className, function, "get({})", key);
				ReadingRequest databaseReadEvent = readingRequests.get(key);
				logger.debug(className, function, "remove({})", key);
				readingRequests.remove(key);
				if ( null != databaseReadEvent ) {
					databaseReadEvent.databaseEvent.update(key, databaseReadEvent.dbaddresses, value);
				} else {
					logger.warn(className, function, "databaseReadEvent IS NULL");
				}
				logger.end(className, function);
			}

		});
		database.connect();
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		database.disconnect();
		logger.end(className, function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i#addMultiReadValueRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addMultiReadValueRequest(String clientKey, String scsEnvId, String [] dbaddress, DatabasePairEvent_i databaseEvent) {
		final String function = "addMultiReadValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}] scsEnvId[{}]", new Object[]{clientKey, scsEnvId});
		if ( logger.isDebugEnabled() ) {
			for ( int i = 0 ; i < dbaddress.length ; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddress[i]);
			}
		}
		if ( null != databaseEvent ) {
			
			ReadingRequest readingRequest = new ReadingRequest(clientKey, scsEnvId, dbaddress, databaseEvent);
			readingRequests.put(clientKey, readingRequest);
			
			database.multiReadValueRequest(clientKey, scsEnvId, dbaddress);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}
	
}
