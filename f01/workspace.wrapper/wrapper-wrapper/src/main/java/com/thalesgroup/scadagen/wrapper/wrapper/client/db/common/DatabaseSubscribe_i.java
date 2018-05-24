package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

/**
 * Implementation the Database Subscribe Operation
 * 
 * @author t0096643
 *
 */
public interface DatabaseSubscribe_i extends Connectable_i {
	
	/**
	 * Add Subscribe Request
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param clientKey     Client Key for the writing operation
	 * @param scsEnvId      ScsEnvId for the target connector
	 * @param dbAddresses   DBAddress for the target RTDB Address
	 * @param databaseEvent Callback for result
	 */
	void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbAddresses, DatabasePairEvent_i databaseEvent);
	
	/**
	 * Add UnSubscribe Request
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param clientKey
	 */
	void addUnSubscribeRequest(String clientKey);
	
	/**
	 * Setter for Period Millis for the Subscribe Request
	 * 
	 * @param periodMillis
	 */
	void setPeriodMillis(int periodMillis);
}
