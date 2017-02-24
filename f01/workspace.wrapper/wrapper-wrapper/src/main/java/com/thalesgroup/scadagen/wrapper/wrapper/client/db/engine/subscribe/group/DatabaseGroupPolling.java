package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling;

public class DatabaseGroupPolling implements DatabaseSubscribe_i, MultiPairResponsible_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGroupPolling.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected HashMap<String, HashMap<String, PollingRequest>> requests = new HashMap<String, HashMap<String, PollingRequest>>();
	protected HashMap<String, String> requestKeyScsEnvIds = new HashMap<String, String>();
	
	protected DatabasePolling databasePolling = new DatabasePolling();
	
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
	
	private int periodMillis = 250;
	@Override
	public void setPeriodMillis(int periodMillis) { this.periodMillis = periodMillis; }

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		databasePolling.setPeriodMillis(periodMillis);
		databasePolling.connect();
		logger.end(className, function);
	}

	@Override
	public void disconnect() {
		final String function = "connect";
		logger.begin(className, function);
		for ( String clientKey : requestKeyScsEnvIds.keySet() ) {
			databasePolling.addUnSubscribeRequest(clientKey);
		}
		requestKeyScsEnvIds.clear();
		requests.clear();
		databasePolling.disconnect();
		logger.end(className, function);
	}

	private Date lastUpdate = null;
	private HashMap<String, String[]> dbAddresSet = new HashMap<String, String[]>();
	@Override
	public void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbaddresses, DatabasePairEvent_i databaseEvent) {
		final String function = "addSubscribeRequest";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}] scsEnvId[{}]", clientKey, scsEnvId);
		if ( logger.isDebugEnabled() ) {
			for ( String address : dbaddresses ) {
				logger.debug(className, function, "address[{}]", address);
			}
		}
		
		if ( null != databaseEvent) {
			
			lastUpdate = new Date();

			PollingRequest rq = new PollingRequest(clientKey, dbaddresses, databaseEvent);
			
			HashMap<String, PollingRequest> rqs = null;
			if ( ! requests.containsKey(scsEnvId) ) {
				requests.put(scsEnvId, new HashMap<String, PollingRequest>());
			}
			rqs = requests.get(scsEnvId);
			rqs.put(clientKey, rq);
			
			buildRequest(className);
			
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		
		logger.end(className, function);
	}

	@Override
	public void addUnSubscribeRequest(String clientKey) {
		final String function = "addUnSubscribeRequest";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}]", clientKey);
		
		lastUpdate = new Date();
		
		for ( String scsEnvId : requests.keySet() ) {
			HashMap<String, PollingRequest> rqs = requests.get(scsEnvId);
			rqs.remove(clientKey);
		}
		
		buildRequest(className);
		
		logger.end(className, function);
	}
	
	protected void buildRequest(String clientKey) {
		final String function = "buildRequest";
		logger.begin(className, function);
		
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
				HashMap<String, PollingRequest> rqs = requests.get(scsEnvId);
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
						logger.begin(className, function);
						logger.info(className, function, "key[{}]", key);
						buildRespond(key, dbaddresses, values);
						logger.end(className, function);
					}
				});
			}
		}
		logger.end(className, function);
	}
	
	@Override
	public void buildRespond(String key, String[] dbaddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		
		String scsEnvId = requestKeyScsEnvIds.get(key);
		
		HashMap<String, String> addressValue = new HashMap<String, String>();
		for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
			addressValue.put(dbaddresses[i], values[i]);
		}
		
		HashMap<String, PollingRequest> rqs = requests.get(scsEnvId);
		for ( String key2 : rqs.keySet() ) {
			PollingRequest rq = rqs.get(key2);
			LinkedList<String> valuelist = new LinkedList<String>();
			for ( String dbaddress : rq.dbaddresses ) {
				valuelist.add(addressValue.get(dbaddress));
			}
			rq.values = valuelist.toArray(new String[0]);
			
			rq.databaseEvent.update(rq.key, rq.dbaddresses, rq.values);
		}
		logger.end(className, function);
	}

}
