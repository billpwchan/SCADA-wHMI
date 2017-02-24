package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

public interface DatabaseMultiRead_i extends Connectable_i {
	
	void addMultiReadValueRequest(String key, String scsEnvId, String[] dbAddresses, DatabaseReadEvent_i databaseEvent);
}
