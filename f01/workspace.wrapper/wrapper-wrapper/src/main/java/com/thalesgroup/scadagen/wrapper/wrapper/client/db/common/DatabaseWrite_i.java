package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

public interface DatabaseWrite_i extends Connectable_i {
	
	void addWriteDateValueRequest(String key, String scsEnvId, String dbAddress, long second, long usecond);
	
	void addWriteIntValueRequest(String key, String scsEnvId, String dbAddress, int value);
	
	void addWriteFloatValueRequest(String key, String scsEnvId, String dbAddress, float value);
	
	void addWriteStringValueRequest(String key, String scsEnvId, String dbAddress, String value);
	
	void addWriteValueRequest(String key, String scsEnvId, String dbAddress, String value);
}
