package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the async call reading operation result in Multi Pair value set
 * 
 * @author syau
 *
 */
public interface Multi2MultiResponsible_i {
	
	/**
	 * Implement the Build Respond
	 * 
	 * @param key         Client Key for the reading operation
	 * @param dbAddresses Database addressed to read
	 * @param values      Value from the address
	 */
	void buildRespond(String key, String[] dbAddresses, String[] values);
}
