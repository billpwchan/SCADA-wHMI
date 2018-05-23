package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

/**
 * Interface for the Database Multi Reading
 * 
 * @author t0096643
 *
 */
public interface DatabaseMultiRead_i extends Connectable_i {

	/**
	 * Method to implement Add Multi Read Value Request
	 * 
	 * @param key           Client Key for the reading operation
	 * @param scsEnvId      ScsEnvId to connect
	 * @param dbAddresses   Database addressed to read
	 * @param databaseEvent Callback for result
	 */
	void addMultiReadValueRequest(String key, String scsEnvId, String[] dbAddresses, DatabasePairEvent_i databaseEvent);
}