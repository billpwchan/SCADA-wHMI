package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.Multi2MultiResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;

/**
 * Implementation the Database Polling Operation in singleton
 * 
 * @author t0096643
 *
 */
public class DatabasePolling implements DatabaseSubscribe_i, Multi2MultiResponsible_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, PollingRequest> requests  = new HashMap<String, PollingRequest>();

	private DatabaseMultiRead_i databaseReading   = new DatabaseMultiReading();

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
	
	private int periodMillis = 1000;
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#setPeriodMillis(int)
	 */
	@Override
	public void setPeriodMillis(int periodMillis) { this.periodMillis = periodMillis; }
	
	private boolean connected = false;
	public boolean isConnected() { return connected; }
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		if(!connected) {
			databaseReading.connect();
			scheduleTimer();
			connected = true;
		} else {
			logger.warn(function, "connected, SKIP");
		}
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "connect";
		logger.begin(function);
		cancelTimer();
		requests.clear();
		databaseReading.disconnect();
		connected = false;
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addSubscribeRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
		final String function = "addSubscribeRequest";
		logger.begin(function);
		logger.debug(function, "clientKey[{}] scsEnvId[{}]", clientKey, scsEnvId);

		for ( String address : dbaddresses ) {
			logger.debug(function, "address[{}]", address);
		}
		
		if ( null != databaseEvent) {
			
			logger.debug(function, "send request to database[{}]", clientKey);

			PollingRequest rq = new PollingRequest(clientKey, scsEnvId, dbaddresses, databaseEvent);
			
			requests.put(clientKey, rq);
		} else {
			logger.warn(function, "databaseEvent IS NULL");
		}
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addUnSubscribeRequest(java.lang.String)
	 */
	@Override
	public void addUnSubscribeRequest(String clientKey) {
		final String function = "addUnSubscribeRequest";
		logger.begin(function);
		logger.debug(function, "clientKey[{}]", clientKey);
		requests.remove(clientKey);
		logger.end(function);
	}

	/**
	 * 
	 */
	private void scheduleTimer() {
		final String function = "scheduleTimer";
		logger.begin(function);
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					String function2 = function + " run";
					if ( requests.size() > 0 ) {

						for ( String clientKey : requests.keySet() ) {
							
							PollingRequest rq = requests.get(clientKey);
							if(null!=rq) {
								String scsEnvId = rq.scsEnvId;
								String [] dbaddresses = rq.dbaddress;
								
								databaseReading.addMultiReadValueRequest(clientKey, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
									
									@Override
									public void update(String key, String [] dbaddress, String[] values) {
										String function3 = function + " run";
										
										PollingRequest rq = requests.get(key);
										if(null!=rq) {
																				String [] dbaddresses = rq.dbaddress;
										buildRespond(key, dbaddresses, values);
										} else {
											logger.warn(function3, "rq with key[{}] IS NULL", key);
										}
									}
								});
							} else {
								logger.warn(function2, "rq with clientKey[{}] IS NULL", clientKey);
							}

						}

					}
				}
			};
			logger.debug(function, "periodMillis[{}]", periodMillis);
			timer.scheduleRepeating(periodMillis);
		}
		logger.end(function);
	}

	/**
	 * 
	 */
	private void cancelTimer() {
		final String function = "cancelTimer";
		logger.begin(function);
		if ( null != timer ) {
			if(timer.isRunning()) {
				logger.warn(function, "timer NOT RUNNING");
			}
			timer.cancel();
			timer = null;
		} else {
			logger.warn(function, "timer NOT CREATED, SKIP");
		}
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i#buildRespond(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public void buildRespond(String key, String[] dbaddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		
		PollingRequest rq = requests.get(key);
		if(null!=rq){
			rq.databaseEvent.update(key, dbaddresses, values);
		} else {
			logger.warn(function, "rq with key[{}] NOT FOUND", key);
		}

		logger.end(function);
	}
}
