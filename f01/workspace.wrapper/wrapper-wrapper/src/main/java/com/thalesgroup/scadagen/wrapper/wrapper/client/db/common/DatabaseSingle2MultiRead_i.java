package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

/**
 * Implementation the Database Single Reading Operation
 * 
 * @author t0096643
 *
 */
public interface DatabaseSingle2MultiRead_i extends Connectable_i {

	/**
	 * @param key           Client Key for the reading operation
	 * @param scsEnvId      ScsEnvId for the target connector
	 * @param dbAddress     DBAddress for the target RTDB Address
	 * @param databaseEvent Callback for result
	 */
	void addSingle2MultiRequest(String key, String scsEnvId, String dbAddress, DatabaseReadSingle2MultiEvent_i databaseEvent);
	
}
