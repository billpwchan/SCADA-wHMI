package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;

/**
 * Implementation the Database Multi Reading Operation with Singleton
 * 
 * @author t0096643
 *
 */
public class DatabaseMultiReadingSingleton extends DatabaseMultiReading implements DatabaseSingleton_i {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static DatabaseMultiReadingSingleton instance = null;
	private DatabaseMultiReadingSingleton() {}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseMultiReadingSingleton getInstance() {
		if ( null == instance ) instance = new DatabaseMultiReadingSingleton();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading#connect()
	 */
	@Override
	public void connect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.DatabaseMultiReading#disconnect()
	 */
	@Override
	public void disconnect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#connectOnce()
	 */
	@Override
	public void connectOnce() {
		final String function = "connectOnce";
		logger.begin(function);
		super.connect();
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#disconnectOnce()
	 */
	@Override
	public void disconnectOnce() {
		final String function = "disconnectOnce";
		logger.begin(function);
		super.disconnect();
		logger.end(function);
	}
}
