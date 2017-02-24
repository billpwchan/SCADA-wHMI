package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

public class DatabasePollingSingleton extends DatabasePolling {

	private static DatabasePollingSingleton instance = null;
	private DatabasePollingSingleton() {}
	public static DatabasePollingSingleton getInstance() { 
		if ( null == instance ) instance = new DatabasePollingSingleton();
		return instance;
	}
	
}
