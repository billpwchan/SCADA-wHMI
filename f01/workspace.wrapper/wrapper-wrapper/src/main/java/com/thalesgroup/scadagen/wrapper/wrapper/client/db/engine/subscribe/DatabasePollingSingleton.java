package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;

public class DatabasePollingSingleton extends DatabasePolling implements DatabaseSingleton_i {

	private static DatabasePollingSingleton instance = null;
	private DatabasePollingSingleton() {}
	public static DatabasePollingSingleton getInstance() { 
		if ( null == instance ) instance = new DatabasePollingSingleton();
		return instance;
	}
	
	@Override
	public void connect() {
		
	}
	
	@Override
	public void disconnect() {
		
	}
	
	@Override
	public void connectOnce() {
		super.connect();
	}
	
	@Override
	public void disconnectOnce() {
		super.disconnect();
	}
}
