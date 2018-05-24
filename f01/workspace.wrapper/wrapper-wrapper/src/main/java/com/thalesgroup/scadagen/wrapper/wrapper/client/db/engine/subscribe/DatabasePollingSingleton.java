package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;

/**
 * Implementation the Database Polling Operation in singleton
 * 
 * @author t0096643
 *
 */
public class DatabasePollingSingleton extends DatabasePolling implements DatabaseSingleton_i {

	private static DatabasePollingSingleton instance = null;
	private String name = null;
	private DatabasePollingSingleton (String name) { 
		super(name);
		this.name = name; 
	}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabasePollingSingleton getInstance(String name) { 
		if ( null == instance ) instance = new DatabasePollingSingleton(name);
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling#connect()
	 */
	@Override
	public void connect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling#disconnect()
	 */
	@Override
	public void disconnect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#connectOnce()
	 */
	@Override
	public void connectOnce() {
		super.connect();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#disconnectOnce()
	 */
	@Override
	public void disconnectOnce() {
		super.disconnect();
	}
}
