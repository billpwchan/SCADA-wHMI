package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the Database Building the Single Pair value set
 * 
 * @author syau
 *
 */
public interface SinglePairResponsible_i {
	
	/**
	 * Implement the Build Respond in Single Pair value set
	 * 
	 * @param key        Client Key for the reading operation
	 * @param dbAddress  Database addressed to read
	 * @param values     Value from the address
	 */
	void buildRespond(String key, String dbAddress, String[] values);
}
