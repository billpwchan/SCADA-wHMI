package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.SinglePairResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildren;

public class DatabaseGetChildrenProxy implements DatabaseSingleRead_i, SinglePairResponsible_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenProxy.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected HashMap<String, ReadingRequest> requests = new HashMap<String, ReadingRequest>();
	
	private DatabaseSingleRead_i databaseReading = new DatabaseGetChildren();
	
	class ReadingRequest {
		public String key = null;
		public String scsEnvId = null;
		public String dbAddress = null;
		public String[] values = null;
		public DatabaseReadEvent_i databaseEvent = null;
		public ReadingRequest(String key, String scsEnvId, String dbAddress, DatabaseReadEvent_i databaseEvent) {
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.dbAddress = dbAddress;
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
		final String function = "connect";
		logger.begin(className, function);
		requests.clear();
		databaseReading.disconnect();
		logger.end(className, function);
	}
	
	@Override
	public void addGetChildrenRequest(String clientKey, String scsEnvId, String dbAddress,
			DatabaseReadEvent_i databaseEvent) {
		final String function = "addGetChildrenRequest";
		logger.debug(className, function, "clientKey[{}]", clientKey);
		if ( logger.isDebugEnabled() ) {
			logger.debug(className, function, "dbAddress[{}]", dbAddress);
		}
		
		if ( null != databaseEvent ) {
			
			ReadingRequest rq = new ReadingRequest(clientKey, scsEnvId, dbAddress, databaseEvent);
			requests.put(clientKey, rq);
			
			HashMap<String, HashMap<String, String[]>> caches = DatabaseGetChildrenCache.getInstance().getCaches();
			if ( ! caches.containsKey(scsEnvId) ) {
				caches.put(scsEnvId, new HashMap<String, String[]>());
			}
			HashMap<String, String[]> cache = caches.get(scsEnvId);

			String [] v = cache.get(dbAddress);

			if ( null != v ) {
				
				buildRespond(clientKey, dbAddress, v);
				
			} else {
				
				databaseReading.addGetChildrenRequest(clientKey, scsEnvId, dbAddress, new DatabaseReadEvent_i() {
					
					@Override
					public void update(String clientKey, String[] values) {
						ReadingRequest rq = requests.get(clientKey);
						HashMap<String, HashMap<String, String[]>> caches = DatabaseGetChildrenCache.getInstance().getCaches();
						HashMap<String, String[]> vs = caches.get(rq.scsEnvId);
						String dbAddress = rq.dbAddress;
						vs.put(dbAddress, values);
						buildRespond(clientKey, dbAddress, values);
					}
				});
			}
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}
	
	@Override
	public void buildRespond(String clientKey, String dbAddress, String[] values) {
		final String function = "buildReponse";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}]", clientKey);
		ReadingRequest rq = requests.get(clientKey);
		rq.databaseEvent.update(clientKey, values);
		requests.remove(clientKey);
		logger.end(className, function);
	}
}
