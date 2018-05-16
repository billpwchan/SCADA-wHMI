package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;

/**
 * Implementation the Database Writing Operation
 * 
 * @author syau
 *
 */
public class DatabaseWriting implements DatabaseWrite_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

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
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i#addWriteDateValueRequest(java.lang.String, java.lang.String, java.lang.String, long, long)
	 */
	@Override
	public void addWriteDateValueRequest(String key, String scsEnvId, String address, long second, long usecond) {
		final String function = "addWriteDateValueRequest";
		logger.begin(function);
		logger.debug(function, "key[{}] scsEnvId[{}] address[{}] second[{}] usecond[{}]", new Object[]{key, scsEnvId, address, second, usecond});
		if ( null != database ) {
			database.writeDateValueRequest(key, scsEnvId, address, second, usecond);
		} else {
			logger.warn(function, "database IS NULL");
		}
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i#addWriteIntValueRequest(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public void addWriteIntValueRequest(String key, String scsEnvId, String address, int value) {
		final String function = "addWriteIntValueRequest";
		logger.begin(function);
		logger.debug(function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		if ( null != database ) {
			database.writeIntValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(function, "database IS NULL");
		}
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i#addWriteFloatValueRequest(java.lang.String, java.lang.String, java.lang.String, float)
	 */
	@Override
	public void addWriteFloatValueRequest(String key, String scsEnvId, String address, float value) {
		final String function = "addWriteFloatValueRequest";
		logger.begin(function);
		logger.debug(function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		logger.debug(function, "value[{}]", value);
		if ( null != database ) {
			database.writeFloatValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(function, "database IS NULL");
		}
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i#addWriteStringValueRequest(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addWriteStringValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteStringValueRequest";
		logger.begin(function);
		logger.debug(function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		logger.debug(function, "value[{}]", value);
		if ( null != database ) {
			database.writeStringValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(function, "database IS NULL");
		}
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i#addWriteValueRequest(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addWriteValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteValueRequest";
		logger.begin(function);
		logger.debug(function, "key[{}] scsEnvId[{}] address[{}] value[{}]", new Object[]{key, scsEnvId, address, value});
		if ( null != database ) {
			database.writeValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(function, "database IS NULL");
		}
		logger.end(function);
	}

}
