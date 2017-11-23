package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadSingle2MultiEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2MultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.Single2MultiResponsible_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetChildren;

/**
 * Implementation the Database Get Children Operation with Proxy (Caches)
 * 
 * @author syau
 *
 */
public class DatabaseGetChildrenProxy implements DatabaseSingle2MultiRead_i, Single2MultiResponsible_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetChildrenProxy.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected HashMap<String, ReadingRequest> requests = new HashMap<String, ReadingRequest>();
	
	/**
	 * Instance for the database
	 */
	private DatabaseSingle2MultiRead_i databaseReading = new DatabaseGetChildren();
	
	class ReadingRequest {
		public String key = null;
		public String scsEnvId = null;
		public String dbAddress = null;
		public String[] values = null;
		public DatabaseReadSingle2MultiEvent_i databaseEvent = null;
		public ReadingRequest(String key, String scsEnvId, String dbAddress, DatabaseReadSingle2MultiEvent_i databaseEvent) {
			this.key = key;
			this.scsEnvId = scsEnvId;
			this.dbAddress = dbAddress;
			this.databaseEvent = databaseEvent;
		}
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		databaseReading.connect();
		logger.end(className, function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "connect";
		logger.begin(className, function);
		requests.clear();
		databaseReading.disconnect();
		logger.end(className, function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleRead_i#addGetChildrenRequest(java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadEvent_i)
	 */
	@Override
	public void addSingle2MultiRequest(String clientKey, String scsEnvId, String dbAddress,
			DatabaseReadSingle2MultiEvent_i databaseEvent) {
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
				
				databaseReading.addSingle2MultiRequest(clientKey, scsEnvId, dbAddress, new DatabaseReadSingle2MultiEvent_i() {
					
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
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.SinglePairResponsible_i#buildRespond(java.lang.String, java.lang.String, java.lang.String[])
	 */
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
