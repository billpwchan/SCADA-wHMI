package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

/**
 * Interface for the async call reading operation result in Pair value set
 * 
 * @author t0096643
 *
 */
public interface DatabasePairEvent_i {
	
	/**
	 * Return the Reading result
	 * 
	 * @param key         Client Key for the reading operation
	 * @param dbAddresses Database addressed to read
	 * @param values      Value from the address
	 */
	void update(final String key, final String [] dbAddresses, final String [] values);
}
