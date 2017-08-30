package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the Database Callback for the Read operation value set
 * 
 * @author syau
 *
 */
public interface DatabaseReadSingle2MultiEvent_i {
	
	/**
	 * Return the Reading result
	 * 
	 * @param key    Client Key for the reading operation
	 * @param values Value from the address
	 */
	void update(String key, String [] values);
}
