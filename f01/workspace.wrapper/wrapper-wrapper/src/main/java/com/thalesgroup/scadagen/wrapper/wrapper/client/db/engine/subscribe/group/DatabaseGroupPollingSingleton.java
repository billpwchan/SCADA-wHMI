package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group;

public class DatabaseGroupPollingSingleton extends DatabaseGroupPolling {

	private static DatabaseGroupPollingSingleton instance = null;
	private DatabaseGroupPollingSingleton() {}
	public static DatabaseGroupPollingSingleton getInstance() { 
		if ( null == instance ) instance = new DatabaseGroupPollingSingleton();
		return instance;
	}
	
}
