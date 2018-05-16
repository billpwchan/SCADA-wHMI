package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadSingle2MultiEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2MultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database.ScsRTDBComponentAccessGetChildrenResult;

/**
 * Implementation the Database Get Children Operation
 * 
 * @author syau
 *
 */
public class DatabaseGetChildren implements DatabaseSingle2MultiRead_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, DatabaseReadSingle2MultiEvent_i> databaseReadEvents = new HashMap<String, DatabaseReadSingle2MultiEvent_i>();
	
	/**
	 * Instance for the database
	 */
	private Database database = new Database();

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#connect()
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		
		database.setScsRTDBComponentAccessGetChildenResult(new ScsRTDBComponentAccessGetChildrenResult() {

			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				final String function = "setGetChildrenResult";
				logger.begin(function);
				if ( logger.isDebugEnabled() ) {
					logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
					for(int i = 0; i < instances.length; ++i) {
						logger.debug(function, "instances[{}][{}]", i, instances[i]);
					}	
				}
				DatabaseReadSingle2MultiEvent_i databaseReadEvent = databaseReadEvents.get(clientKey);
				databaseReadEvents.remove(clientKey);
				if ( null != databaseReadEvent ) {
					databaseReadEvent.update(clientKey, instances);
				}
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
		database.disconnect();
		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleRead_i#addGetChildrenRequest(java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadEvent_i)
	 */
	@Override
	public void addSingle2MultiRequest(String clientKey, String scsEnvId, String dbaddress, DatabaseReadSingle2MultiEvent_i databaseEvent) {
		final String function = "addSingle2MultiRequest";
		logger.begin(function);
		logger.debug(function, "clientKey[{}] scsEnvId[{}]", new Object[]{clientKey, scsEnvId});
		logger.debug(function, "dbaddress[{}]", dbaddress);
		if ( null != databaseEvent ) {
			databaseReadEvents.put(clientKey, databaseEvent);
			database.getChildren(clientKey, scsEnvId, dbaddress);
		} else {
			logger.warn(function, "databaseEvent IS NULL");
		}
	}

}
