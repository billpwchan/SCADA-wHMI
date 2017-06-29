package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

/**
 * Implementation the Database Writing Operation
 * 
 * @author syau
 *
 */
public interface DatabaseWrite_i extends Connectable_i {
	
	/**
	 * Write Date Value Request 
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param key       Client Key for the writing operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbAddress DBAddress for the target RTDB Address
	 * @param second    Second value to write to target RTDB Address
	 * @param usecond   USecond alue to write to target RTDB Address
	 */
	void addWriteDateValueRequest(String key, String scsEnvId, String dbAddress, long second, long usecond);
	
	/**
	 * Write Int Value Request 
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param key       Client Key for the writing operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbAddress DBAddress for the target RTDB Address
	 * @param value     Value to write to target RTDB Address
	 */
	void addWriteIntValueRequest(String key, String scsEnvId, String dbAddress, int value);
	
	/**
	 * Write Float Value Request 
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param key       Client Key for the writing operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbAddress DBAddress for the target RTDB Address
	 * @param value     Value to write to target RTDB Address
	 */
	void addWriteFloatValueRequest(String key, String scsEnvId, String dbAddress, float value);
	
	/**
	 * Write String Value Request 
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param key       Client Key for the writing operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbAddress DBAddress for the target RTDB Address
	 * @param value     Value to write to target RTDB Address
	 */
	void addWriteStringValueRequest(String key, String scsEnvId, String dbAddress, String value);
	
	/**
	 * Write Value Request 
	 * High Level function base on the SCADAgen Database Wrapper
	 * 
	 * @param key       Client Key for the writing operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbAddress DBAddress for the target RTDB Address
	 * @param value     Value to write to target RTDB Address
	 */
	void addWriteValueRequest(String key, String scsEnvId, String dbAddress, String value);
}
