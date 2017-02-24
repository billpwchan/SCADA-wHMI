package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;

public class DatabaseWriting implements DatabaseWrite_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseWriting.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private Database database = new Database();
	
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
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

	public void addWriteDateValueRequest(String key, String scsEnvId, String address, long second, long usecond) {
		final String function = "addWriteDateValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] scsEnvId[{}] address[{}] second[{}] usecond[{}]", new Object[]{key, scsEnvId, address, second, usecond});
		if ( null != database ) {
			database.writeDateValueRequest(key, scsEnvId, address, second, usecond);
		} else {
			logger.warn(className, function, "database IS NULL");
		}
		logger.end(className, function);
	}
	
	public void addWriteIntValueRequest(String key, String scsEnvId, String address, int value) {
		final String function = "addWriteIntValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		if ( null != database ) {
			database.writeIntValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "database IS NULL");
		}
		logger.end(className, function);
	}
	
	public void addWriteFloatValueRequest(String key, String scsEnvId, String address, float value) {
		final String function = "addWriteFloatValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		logger.debug(className, function, "value[{}]", value);
		if ( null != database ) {
			database.writeFloatValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "database IS NULL");
		}
		logger.end(className, function);
	}
	
	public void addWriteStringValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteStringValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		logger.debug(className, function, "value[{}]", value);
		if ( null != database ) {
			database.writeStringValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "database IS NULL");
		}
		logger.end(className, function);
	}
	
	public void addWriteValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteValueRequest";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		if ( null != database ) {
			database.writeValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "database IS NULL");
		}
		logger.end(className, function);
	}

}
