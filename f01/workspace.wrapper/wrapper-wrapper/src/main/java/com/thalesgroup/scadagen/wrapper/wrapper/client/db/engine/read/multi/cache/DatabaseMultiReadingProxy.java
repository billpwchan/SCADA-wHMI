package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache;

import java.util.HashMap;
import java.util.LinkedList;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.MultiPairResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;

public class DatabaseMultiReadingProxy implements DatabaseMultiRead_i, MultiPairResponsible_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseMultiReadingProxy.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	protected HashMap<String, ReadingRequest> requests = new HashMap<String, ReadingRequest>();
	
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

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		databaseReading.connect();
		logger.end(className, function);
	}

	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		requests.clear();
		databaseReading.disconnect();
		logger.end(className, function);
	}
	
	@Override
	public void addMultiReadValueRequest(String clientKey, String scsEnvId, String[] dbAddresses,
			DatabasePairEvent_i databaseEvent) {
		final String function = "addMultiReadValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}]", clientKey);
		if ( logger.isDebugEnabled() ) {
			for ( String address : dbAddresses ) {
				logger.debug(className, function, "address[{}]", address);
			}
		}
		
		if ( null != databaseEvent ) {
			
			ReadingRequest rq = new ReadingRequest(clientKey, scsEnvId, dbAddresses, databaseEvent);
			requests.put(clientKey, rq);
			
			HashMap<String, HashMap<String, String>> caches = DatabaseMultiReadingCache.getInstance().getCache();
			if ( ! caches.containsKey(scsEnvId) ) {
				caches.put(scsEnvId, new HashMap<String, String>());
			}
			HashMap<String, String> cache = caches.get(scsEnvId);
			
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
						HashMap<String, HashMap<String, String>> caches = DatabaseMultiReadingCache.getInstance().getCache();
						HashMap<String, String> vs = caches.get(rq.scsEnvId);
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
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}

	@Override
	public void buildRespond(String clientKey, String[] dbAddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}]", clientKey);
		ReadingRequest rq = requests.get(clientKey);
		rq.databaseEvent.update(clientKey, dbAddresses, values);
		requests.remove(clientKey);
		logger.end(className, function);
	}

}
