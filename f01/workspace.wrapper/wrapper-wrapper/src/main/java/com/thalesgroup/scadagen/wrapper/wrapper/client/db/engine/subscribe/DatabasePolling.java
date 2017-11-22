package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import java.util.HashMap;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.Multi2MultiResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;

/**
 * Implementation the Database Polling Operation in singleton
 * 
 * @author syau
 *
 */
public class DatabasePolling implements DatabaseSubscribe_i, Multi2MultiResponsible_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabasePolling.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private HashMap<String, PollingRequest> requests		= new HashMap<String, PollingRequest>();

	private DatabaseMultiRead_i databaseReading = new DatabaseMultiReading();

	public class PollingRequest {
		String key = null;
		String scsEnvId = null;
		String[] dbaddress = null;
		DatabasePairEvent_i databaseEvent = null;
		public PollingRequest(String key, String scsEnvId, String[] dbaddress, DatabasePairEvent_i databaseEvent) {
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.dbaddress = dbaddress;
			this.databaseEvent = databaseEvent;
		}
	}
	
	/**
	 * Timer for the database reading
	 */
	private Timer timer = null;
	
	private int periodMillis = 250;
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#setPeriodMillis(int)
	 */
	@Override
	public void setPeriodMillis(int periodMillis) { this.periodMillis = periodMillis; }
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		databaseReading.connect();
		scheduleTimer();
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "connect";
		logger.begin(className, function);
		cancelTimer();
		requests.clear();
		databaseReading.disconnect();
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addSubscribeRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
		final String function = "addSubscribeRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}] scsEnvId[{}]", clientKey, scsEnvId);
		if ( logger.isDebugEnabled() ) {
			for ( String address : dbaddresses ) {
				logger.debug(className, function, "address[{}]", address);
			}
		}
		
		if ( null != databaseEvent) {
			
			logger.debug(className, function, "send request to database[{}]", clientKey);

			PollingRequest rq = new PollingRequest(clientKey, scsEnvId, dbaddresses, databaseEvent);
			
			requests.put(clientKey, rq);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addUnSubscribeRequest(java.lang.String)
	 */
	@Override
	public void addUnSubscribeRequest(String clientKey) {
		final String function = "addUnSubscribeRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}]", clientKey);
		requests.remove(clientKey);
		logger.end(className, function);
	}

	/**
	 * 
	 */
	private void scheduleTimer() {
		final String function = "scheduleTimer";
		logger.begin(className, function);
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					if ( requests.size() > 0 ) {

						for ( String clientKey : requests.keySet() ) {
							
							PollingRequest rq = requests.get(clientKey);
							String scsEnvId = rq.scsEnvId;
							String [] dbaddresses = rq.dbaddress;
							
							databaseReading.addMultiReadValueRequest(clientKey, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
								
								@Override
								public void update(String key, String [] dbaddress, String[] values) {
									
									PollingRequest rq = requests.get(key);
									String [] dbaddresses = rq.dbaddress;
									buildRespond(key, dbaddresses, values);

								}
							});
						}

					}
				}
			};
			
			timer.scheduleRepeating(periodMillis);
		}
		logger.end(className, function);
	}

	/**
	 * 
	 */
	private void cancelTimer() {
		final String function = "cancelTimer";
		logger.begin(className, function);
		if ( null != timer ) {
			timer.cancel();
			timer = null;
		}
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i#buildRespond(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public void buildRespond(String key, String[] dbaddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		
		PollingRequest rq = requests.get(key);
		rq.databaseEvent.update(key, dbaddresses, values);

		logger.end(className, function);
	}
}
