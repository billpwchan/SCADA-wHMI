package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the Database Callback for the Read operation value set
 * 
 * @author t0096643
 *
 */
public interface DatabaseReadSingle2SingleResult_i {
	
	/**
	 * Return the Reading result
	 * 
	 * @param key    Client Key for the reading operation
	 * @param values Value from the address
	 */
	void update(String key, String values);
}
