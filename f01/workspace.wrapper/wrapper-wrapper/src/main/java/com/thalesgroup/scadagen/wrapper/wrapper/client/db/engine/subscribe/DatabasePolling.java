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
	
	private String name = null;
	public DatabasePolling (String name) { 
		final String f = "DatabasePolling";
		logger.begin(f + getName());
		this.name = name;
		logger.end(f + getName());
	}
	
	private String getName() { return "name[" + name + "] this[" + this + "]"; }

	public class PollingRequest {

		String key = null;
		String scsEnvId = null;
		String[] dbaddress = null;
		DatabasePairEvent_i databaseEvent = null;
		public PollingRequest(String key, String scsEnvId, String[] dbaddress, DatabasePairEvent_i databaseEvent) {
			final String f = "PollingRequest";
			logger.begin(f + getName());
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.dbaddress = dbaddress;
			this.databaseEvent = databaseEvent;
			logger.end(f + getName());
		}
	}
	
	/**
	 * Timer for the database reading
	 */
	private Timer timer = null;
	
	private int periodMillis = 1500;
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
		final String f = "connect";
		logger.begin(f + getName());
		if(!connected) {
			databaseReading.connect();
			scheduleTimer();
			connected = true;
		} else {
			logger.warn(f, "connected, SKIP getName(){}", getName());
		}
		logger.end(f + getName());
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String f = "disconnect";
		logger.begin(f + getName());
		requests.clear();
		cancelTimer();
		databaseReading.disconnect();
		connected = false;
		logger.end(f + getName());
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addSubscribeRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
		final String f = "addSubscribeRequest";
		logger.begin(f + getName());
		logger.debug(f, "clientKey[{}] scsEnvId[{}] getName(){}", new Object[]{clientKey, scsEnvId, getName()});

		for ( String address : dbaddresses ) {
			logger.debug(f, "address[{}] getName(){}", address, getName());
		}
		
		if ( null != databaseEvent) {
			
			logger.debug(f, "send request to database[{}]", clientKey);

			PollingRequest rq = new PollingRequest(clientKey, scsEnvId, dbaddresses, databaseEvent);
			
			logger.debug(f, "add clientKey[{}] rq[{}] getName(){}", new Object[]{clientKey, rq, getName()});
			requests.put(clientKey, rq);
			logger.debug(f, "requests.size()[{}] getName(){}", requests.size(), getName());
		} else {
			logger.warn(f, "databaseEvent IS NULL getName(){}", getName());
		}
		logger.end(f + getName());
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addUnSubscribeRequest(java.lang.String)
	 */
	@Override
	public void addUnSubscribeRequest(String clientKey) {
		final String f = "addUnSubscribeRequest";
		logger.begin(f + getName());
		logger.debug(f, "remove clientKey[{}]", clientKey);
		requests.remove(clientKey);
		logger.debug(f, "requests.size()[{}]", requests.size());
		logger.end(f + getName());
	}

	/**
	 * 
	 */
	private void scheduleTimer() {
		final String f = "scheduleTimer";
		logger.begin(f + getName());
		cancelTimer();
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					String f2 = f + " run ";
					logger.debug(f2, "requests.size()[{}] getName(){}", requests.size(), getName());
					if ( requests.size() > 0 ) {

						for ( String clientKey : requests.keySet() ) {
							
							PollingRequest rq = requests.get(clientKey);
							if(null!=rq) {
								String scsEnvId = rq.scsEnvId;
								String [] dbaddresses = rq.dbaddress;
								
								databaseReading.addMultiReadValueRequest(clientKey, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
									
									@Override
									public void update(String key, String [] dbaddress, String[] values) {
										String f3 = f + " run";
										
										PollingRequest rq = requests.get(key);
										if(null!=rq) {
											String [] dbaddresses = rq.dbaddress;
											buildRespond(key, dbaddresses, values);
										} else {
											logger.warn(f3, "rq with key[{}] IS NULL getName(){}", key, getName());
										}
									}
								});
							} else {
								logger.warn(f2, "rq with clientKey[{}] IS NULL getName(){}", clientKey, getName());
							}

						}

					}
				}
			};
			logger.debug(f, "periodMillis[{}] getName(){}", periodMillis, getName());
			timer.scheduleRepeating(periodMillis);
		} else {
			logger.warn(f, "timer CREATED, SKIP getName(){}", getName());
		}
		logger.end(f + getName());
	}

	/**
	 * 
	 */
	private void cancelTimer() {
		final String f = "cancelTimer";
		logger.begin(f + getName());
		if ( null != timer ) {
			if(!timer.isRunning()) {
				logger.warn(f, "timer NOT RUNNING getName(){}", getName());
			}
			timer.cancel();
			timer = null;
		} else {
			logger.warn(f, "timer NOT CREATED, SKIP getName(){}", getName());
		}
		logger.end(f + getName());
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i#buildRespond(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public void buildRespond(String key, String[] dbaddresses, String[] values) {
		final String f = "buildReponse";
		logger.begin(f + getName());
		logger.debug(f, "key[{}] getName(){}", key, getName());
		
		PollingRequest rq = requests.get(key);
		if(null!=rq){
			rq.databaseEvent.update(key, dbaddresses, values);
		} else {
			logger.warn(f, "rq with key[{}] NOT FOUND getName(){}", key, getName());
		}

		logger.end(f + getName());
	}
}
