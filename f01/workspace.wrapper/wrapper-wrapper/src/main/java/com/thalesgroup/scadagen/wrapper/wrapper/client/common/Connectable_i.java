package com.thalesgroup.scadagen.wrapper.wrapper.client.common;

/**
 * Interface for the connectivity
 * 
 * @author syau
 *
 */
public interface Connectable_i {

	/**
	 * Implement the connect method
	 * Init and Connect to database
	 * 
	 */
	void connect();
	
	/**
	 * Implement the disconnect method
	 * Disconnect and Terminate the database
	 */
	void disconnect();
	
}
