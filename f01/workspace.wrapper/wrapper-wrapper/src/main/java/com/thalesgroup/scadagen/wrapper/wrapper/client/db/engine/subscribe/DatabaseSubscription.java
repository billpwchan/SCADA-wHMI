package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database.ScsPollerComponentAccessResult;

public class DatabaseSubscription implements DatabaseSubscribe_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseSubscription.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private Database database = new Database();
	
	public class SubscriptionRequest {
		String key = null;
		String subUUID = null;
		String groupName = null;
		String scsEnvId = null;
		String[] dbaddress = null;
		DatabasePairEvent_i databaseEvent = null;
		public SubscriptionRequest(String key, String scsEnvId, String groupName, String[] dbaddress, DatabasePairEvent_i databaseEvent) {
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.groupName = groupName;
			this.dbaddress = dbaddress;
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
		database.setScsPollerComponentAccessResult(new ScsPollerComponentAccessResult() {
			
			@Override
			public void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
					String[] values, int errorCode, String errorMessage) {
				final String function = "setSubscribeResult";
				logger.begin(className, function);
				
				if ( null != dbaddress ) {
					logger.debug(className, function, "dbaddress.length[{}]", dbaddress.length);
				} else {
					logger.warn(className, function, "dbaddress IS NULL");
				}
				if ( null != values ) {
					logger.debug(className, function, "values.length[{}]", values.length);
				} else {
					logger.warn(className, function, "values IS NULL");
				}

				if ( logger.isDebugEnabled() ) {
					logger.debug(className, function, "clientKey[{}] pollerState[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, pollerState, errorCode, errorMessage});
					for(int i = 0; i < dbaddress.length; ++i) {
						logger.debug(className, function, "i[{}] dbaddress[{}] value[{}]", new Object[]{i, dbaddress[i], values[i]});
					}
				}
				
				SubscriptionRequest request = subscriptionRequests.get(clientKey);
				
				if ( null == request.subUUID ) {
					request.subUUID = subUUID;
				}
				
				request.databaseEvent.update(clientKey, dbaddress, values);				

				logger.end(className, function);
			}
		});
		
		database.connect();
		logger.end(className, function);
	}

	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		
		// Remove
		Iterator<Map.Entry<String, SubscriptionRequest>> iter = subscriptionRequests.entrySet().iterator();
		while ( iter.hasNext() ) {
			Entry<String, SubscriptionRequest> sr = iter.next();
			String clientKey = sr.getKey();
			addUnSubscribeRequest(clientKey);
			iter.remove();
		}
		
		database.disconnect();
		logger.end(className, function);
	}
	
	private HashMap<String, SubscriptionRequest> subscriptionRequests = new HashMap<String, SubscriptionRequest>();
	
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
		
		String groupName = clientKey;
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(clientKey, scsEnvId, groupName, dbaddresses, databaseEvent);
		subscriptionRequests.put(clientKey, subscriptionRequest);
		
		database.addSubscriptionRequest(clientKey, scsEnvId, groupName, dbaddresses, periodMillis);
		
		logger.end(className, function);
	}

	@Override
	public void addUnSubscribeRequest(String clientKey) {
		final String function = "addUnSubscribeRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}]", clientKey);

		SubscriptionRequest readingRequestSubscription = subscriptionRequests.get(clientKey);
		String scsEnvId = readingRequestSubscription.scsEnvId;
		String subscriptionId = readingRequestSubscription.subUUID;
		String groupName = readingRequestSubscription.groupName;
		
		database.addUnSubscriptionRequest(clientKey, scsEnvId, groupName, subscriptionId);
		
		subscriptionRequests.remove(clientKey);
		
		logger.end(className, function);
	}
	
}