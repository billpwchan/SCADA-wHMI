package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.Multi2MultiResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;

/**
 * Implementation the Database Multi Reading Operation with Proxy (Caches)
 * 
 * @author t0096643
 *
 */
public class DatabaseMultiReadingProxy implements DatabaseMultiRead_i, Multi2MultiResponsible_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	protected Map<String, ReadingRequest> requests = new HashMap<String, ReadingRequest>();
	
	/**
	 * Instance for the database
	 */
	private DatabaseMultiRead_i databaseReading = new DatabaseMultiReading();

	class ReadingRequest {
		public String key = null;
		public String scsEnvId = null;
		public String[] dbAddresses = null;
		public String[] values = null;
		public DatabasePairEvent_i databaseEvent = null;
		public ReadingRequest(String key, String scsEnvId, String[] dbAddresses, DatabasePairEvent_i databaseEvent) {
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.dbAddresses = dbAddresses;
			this.databaseEvent = databaseEvent;
		}
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		databaseReading.connect();
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		requests.clear();
		databaseReading.disconnect();
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i#addMultiReadValueRequest(java.lang.String, java.lang.String, java.lang.String[], com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i)
	 */
	@Override
	public void addMultiReadValueRequest(String clientKey, String scsEnvId, String[] dbAddresses,
			DatabasePairEvent_i databaseEvent) {
		final String function = "addMultiReadValueRequest";
		logger.begin(function);
		logger.debug(function, "clientKey[{}]", clientKey);
		if ( logger.isDebugEnabled() ) {
			for ( String address : dbAddresses ) {
				logger.debug(function, "address[{}]", address);
			}
		}
		
		if ( null != databaseEvent ) {
			
			ReadingRequest rq = new ReadingRequest(clientKey, scsEnvId, dbAddresses, databaseEvent);
			requests.put(clientKey, rq);
			
			Map<String, Map<String, String>> caches = DatabaseMultiReadingCache.getInstance().getCache();
			if ( ! caches.containsKey(scsEnvId) ) {
				caches.put(scsEnvId, new HashMap<String, String>());
			}
			Map<String, String> cache = caches.get(scsEnvId);
			
			LinkedList<String> vl = new LinkedList<String>();
			
			boolean nonexists = false;
			for ( int i = 0 ; i < rq.dbAddresses.length ; ++i ) {
				String a = rq.dbAddresses[i];
				String v = cache.get(a);
				vl.add(v);
				
				if ( null == v ) {
					nonexists = true;
					break;
				}
			}

			if ( ! nonexists ) {
				String [] values = vl.toArray(new String[0]);
				buildRespond(clientKey, dbAddresses, values);
			} else {
				
				// Prepare Remote Request
				databaseReading.addMultiReadValueRequest(clientKey, scsEnvId, dbAddresses, new DatabasePairEvent_i() {
					
					@Override
					public void update(String key, String [] addresses, String[] values) {
						ReadingRequest rq = requests.get(key);
						Map<String, Map<String, String>> caches = DatabaseMultiReadingCache.getInstance().getCache();
						Map<String, String> vs = caches.get(rq.scsEnvId);
						String [] dbaddresses = rq.dbAddresses;
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							String a = dbaddresses[i];
							String v = values[i];
							vs.put(a, v);
						}
						buildRespond(key, dbaddresses, values);
					}
				});
			}
		} else {
			logger.warn(function, "databaseEvent IS NULL");
		}
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i#buildRespond(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public void buildRespond(String clientKey, String[] dbAddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(function);
		logger.debug(function, "clientKey[{}]", clientKey);
		ReadingRequest rq = requests.get(clientKey);
		rq.databaseEvent.update(clientKey, dbAddresses, values);
		requests.remove(clientKey);
		logger.end(function);
	}

}
