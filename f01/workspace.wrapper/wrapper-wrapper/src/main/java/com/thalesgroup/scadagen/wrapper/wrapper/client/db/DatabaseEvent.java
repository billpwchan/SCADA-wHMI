package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

/**
 * Interface for the Database Event
 * 
 * @author syau
 *
 */
public interface DatabaseEvent {
	
	/**
	 * Interface for the Asynchronous callback of Get Children result
	 * 
	 * @param key   Client Key for the result
	 * @param value Value for the result
	 */
	void update(String key, String value[]);
	
}
