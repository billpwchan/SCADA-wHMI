package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

public interface DatabaseMultiRead_i extends Connectable_i {
	
//	void addMultiReadValueRequest(String key, String scsEnvId, String[] dbAddresses, DatabaseReadEvent_i databaseEvent);
	
	void addMultiReadValueRequest(String key, String scsEnvId, String[] dbAddresses, DatabasePairEvent_i databaseEvent);
}
