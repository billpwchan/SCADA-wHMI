package com.thalesgroup.scadagen.wrapper.wrapper.client.db.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;

public interface DatabaseSubscribe_i extends Connectable_i {
	
	void addSubscribeRequest(String clientKey, String scsEnvId, String[] dbAddresses, DatabasePairEvent_i databaseEvent);
	
	void addUnSubscribeRequest(String clientKey);
	
	void setPeriodMillis(int periodMillis);
}
