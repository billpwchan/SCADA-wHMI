package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.Multi2MultiResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling;

/**
 * Implementation the Database Group Polling Operation
 * 
 * @author t0096643
 *
 */
public class DatabaseGroupPolling implements DatabaseSubscribe_i, Multi2MultiResponsible_i {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	protected Map<String, Map<String, PollingRequest>> requests = new HashMap<String, Map<String, PollingRequest>>();
	protected Map<String, String> requestKeyScsEnvIds = new HashMap<String, String>();
	
	private String name = null;
	public DatabaseGroupPolling (String name) {
		this.name = name; 
		databasePolling = new DatabasePolling(name);
	}
	
	/**
	 * Instance for the database
	 */
	protected DatabasePolling databasePolling = null;
	
	public class PollingRequest {
		public String key = null;
		public String[] dbaddresses = null;
		public String[] values = null;
		public DatabasePairEvent_i databaseEvent = null;
		public PollingRequest(String key, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
			this.key = key;
			this.dbaddresses = dbaddresses;
			this.databaseEvent = databaseEvent;
		}
	}
	
	private int periodMillis = 1000;
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
		logger.begin(function);
		logger.debug(function, "periodMillis[{}]", periodMillis);
		databasePolling.setPeriodMillis(periodMillis);
		databasePolling.connect();
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		for ( String clientKey : requestKeyScsEnvIds.keySet() ) {
			databasePolling.addUnSubscribeRequest(clientKey);
		}
		requestKeyScsEnvIds.clear();
		requests.clear();
		databasePolling.disconnect();
		logger.end(function);
	}

	private Date lastUpdate = null;
	private Map<String, String[]> dbAddresSet = new HashMap<String, String[]>();
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i#addSubscribeRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
		final String function = "addSubscribeRequest";
		logger.begin(function);
		logger.debug(function, "clientKey[{}] scsEnvId[{}]", clientKey, scsEnvId);
		if ( logger.isDebugEnabled() ) {
			for ( String address : dbaddresses ) {
				logger.debug(function, "address[{}]", address);
			}
		}
		
		if ( null != databaseEvent) {
			
			lastUpdate = new Date();

			PollingRequest rq = new PollingRequest(clientKey, dbaddresses, databaseEvent);
			
			Map<String, PollingRequest> rqs = null;
			if ( ! requests.containsKey(scsEnvId) ) {
				requests.put(scsEnvId, new HashMap<String, PollingRequest>());
			}
			rqs = requests.get(scsEnvId);
			rqs.put(clientKey, rq);
			
			buildRequest(className);
			
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
		
		lastUpdate = new Date();
		
		for ( String scsEnvId : requests.keySet() ) {
			Map<String, PollingRequest> rqs = requests.get(scsEnvId);
			rqs.remove(clientKey);
		}
		
		buildRequest(className);
		
		logger.end(function);
	}
	
	/**
	 * @param clientKey
	 */
	protected void buildRequest(String clientKey) {
		final String function = "buildRequest";
		logger.begin(function);
		
		boolean rebuild = false;
		if ( null != lastUpdate ) {
			rebuild = true;
			lastUpdate = null;
		}
		
		for ( String scsEnvId : requests.keySet() ) {
			
			String clientKeyScsEnvId = clientKey+"_"+scsEnvId;
			
			databasePolling.addUnSubscribeRequest(clientKeyScsEnvId);
			
			String[] dbaddresses = null;
			
			if ( rebuild ) {
				Set<String> dbAddressSet = new HashSet<String>();
				Map<String, PollingRequest> rqs = requests.get(scsEnvId);
				for ( String key2 : rqs.keySet() ) {
					PollingRequest rq = rqs.get(key2);
					for ( String dbaddress : rq.dbaddresses ) {
						dbAddressSet.add(dbaddress);
					}
				}
				dbaddresses = dbAddressSet.toArray(new String[0]);
				
				dbAddresSet.put(scsEnvId, dbaddresses);
			} else {
				
				dbaddresses = dbAddresSet.get(scsEnvId);
			}
			
			if ( null != dbaddresses && dbaddresses.length > 0 ) {
				
				requestKeyScsEnvIds.put(clientKeyScsEnvId, scsEnvId);
				
				databasePolling.addSubscribeRequest(clientKeyScsEnvId, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
					
					@Override
					public void update(String key, String[] dbaddresses, String[] values) {
						final String function = "update";
						logger.begin(function);
						logger.debug(function, "key[{}]", key);
						buildRespond(key, dbaddresses, values);
						logger.end(function);
					}
				});
			}
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
		
		String scsEnvId = requestKeyScsEnvIds.get(key);
		
		Map<String, String> addressValue = new HashMap<String, String>();
		for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
			addressValue.put(dbaddresses[i], values[i]);
		}
		
		Map<String, PollingRequest> rqs = requests.get(scsEnvId);
		for ( String key2 : rqs.keySet() ) {
			PollingRequest rq = rqs.get(key2);
			LinkedList<String> valuelist = new LinkedList<String>();
			for ( String dbaddress : rq.dbaddresses ) {
				valuelist.add(addressValue.get(dbaddress));
			}
			rq.values = valuelist.toArray(new String[0]);
			
			rq.databaseEvent.update(rq.key, rq.dbaddresses, rq.values);
		}
		logger.end(function);
	}

}
