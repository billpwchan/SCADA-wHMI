package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

public interface DatabaseSingleRead_i extends Connectable_i {

	void addGetChildrenRequest(String key, String scsEnvId, String dbAddress, DatabaseReadEvent_i databaseEvent);
	
}
