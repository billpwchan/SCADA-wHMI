package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the Singleton database connectivity
 * It using to avoid the multi connect/disconnect activity
 * 
 * @author syau
 *
 */
public interface DatabaseSingleton_i {
	
	/**
	 * Implement the connect method
	 * Init and Connect to database
	 */
	void connectOnce();
	
	/**
	 * Implement the disconnect method
	 * Disconnect and Terminate the database
	 */
	void disconnectOnce();
}
