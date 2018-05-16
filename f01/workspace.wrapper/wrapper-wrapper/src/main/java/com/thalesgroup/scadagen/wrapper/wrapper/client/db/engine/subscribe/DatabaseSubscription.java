package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database.ScsPollerComponentAccessResult;

/**
 * Implementation the Database Subscription Operation
 * 
 * @author syau
 *
 */
public class DatabaseSubscription implements DatabaseSubscribe_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	/**
	 * Instance for the database
	 */
	private Database database = new Database();
	
	/**
	 * @author syau
	 *
	 */
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
		database.setScsPollerComponentAccessResult(new ScsPollerComponentAccessResult() {
			
			@Override
			public void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
					String[] values, int errorCode, String errorMessage) {
				final String function = "setSubscribeResult";
				logger.begin(function);
				
				if ( null != dbaddress ) {
					logger.debug(function, "dbaddress.length[{}]", dbaddress.length);
				} else {
					logger.warn(function, "dbaddress IS NULL");
				}
				if ( null != values ) {
					logger.debug(function, "values.length[{}]", values.length);
				} else {
					logger.warn(function, "values IS NULL");
				}

				if ( logger.isDebugEnabled() ) {
					logger.debug(function, "clientKey[{}] pollerState[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, pollerState, errorCode, errorMessage});
					for(int i = 0; i < dbaddress.length; ++i) {
						logger.debug(function, "i[{}] dbaddress[{}] value[{}]", new Object[]{i, dbaddress[i], values[i]});
					}
				}
				
				SubscriptionRequest request = subscriptionRequests.get(clientKey);
				
				if ( null == request.subUUID ) {
					request.subUUID = subUUID;
				}
				
				request.databaseEvent.update(clientKey, dbaddress, values);				

				logger.end(function);
			}
		});
		
		database.connect();
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		
		// Remove
		Iterator<Map.Entry<String, SubscriptionRequest>> iter = subscriptionRequests.entrySet().iterator();
		while ( iter.hasNext() ) {
			Entry<String, SubscriptionRequest> sr = iter.next();
			String clientKey = sr.getKey();
			addUnSubscribeRequest(clientKey);
			iter.remove();
		}
		
		database.disconnect();
		logger.end(function);
	}
	
	private Map<String, SubscriptionRequest> subscriptionRequests = new HashMap<String, SubscriptionRequest>();
	
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
		
		String groupName = clientKey;
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(clientKey, scsEnvId, groupName, dbaddresses, databaseEvent);
		subscriptionRequests.put(clientKey, subscriptionRequest);
		
		database.addSubscriptionRequest(clientKey, scsEnvId, groupName, dbaddresses, periodMillis);
		
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

		SubscriptionRequest readingRequestSubscription = subscriptionRequests.get(clientKey);
		String scsEnvId = readingRequestSubscription.scsEnvId;
		String subscriptionId = readingRequestSubscription.subUUID;
		String groupName = readingRequestSubscription.groupName;
		
		database.addUnSubscriptionRequest(clientKey, scsEnvId, groupName, subscriptionId);
		
		subscriptionRequests.remove(clientKey);
		
		logger.end(function);
	}
	
}
