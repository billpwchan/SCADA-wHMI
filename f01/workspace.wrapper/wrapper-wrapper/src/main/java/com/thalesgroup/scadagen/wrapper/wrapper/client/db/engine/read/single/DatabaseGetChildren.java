package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database.ScsRTDBComponentAccessGetChildrenResult;

public class DatabaseGetChildren implements DatabaseSingleRead_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetChildren.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private HashMap<String, DatabaseReadEvent_i> databaseReadEvents = new HashMap<String, DatabaseReadEvent_i>();
	
	private Database database = new Database();

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		database.setScsRTDBComponentAccessGetChildenResult(new ScsRTDBComponentAccessGetChildrenResult() {

			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				final String function = "setGetChildrenResult";
				logger.begin(className, function);
				if ( logger.isDebugEnabled() ) {
					logger.debug(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
					for(int i = 0; i < instances.length; ++i) {
						logger.debug(className, function, "instances[{}][{}]", i, instances[i]);
					}	
				}
				DatabaseReadEvent_i databaseReadEvent = databaseReadEvents.get(clientKey);
				if ( null != databaseReadEvent ) {
					databaseReadEvent.update(clientKey, instances);
					databaseReadEvents.remove(clientKey);
				}
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
		database.disconnect();
		logger.end(className, function);
	}

	/**
	 * @param api : Database API to call
	 * @param clientKey : Key for the Reading and Result
	 * @param scsEnvId : scsEnvId to connect
	 * @param dbaddresses : database address to read
	 * @param databaseEvent : Callback for result
	 */
	@Override
	public void addGetChildrenRequest(String clientKey, String scsEnvId, String dbaddress, DatabaseReadEvent_i databaseEvent) {
		final String function = "addGetChildrenRequest";
		logger.begin(className, function);
		logger.debug(className, function, "clientKey[{}] scsEnvId[{}]", new Object[]{clientKey, scsEnvId});
		logger.debug(className, function, "dbaddress[{}]", dbaddress);
		if ( null != databaseEvent ) {
			databaseReadEvents.put(clientKey, databaseEvent);
			database.getChildren(clientKey, scsEnvId, dbaddress);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
	}

}
