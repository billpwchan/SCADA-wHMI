package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff;

public class DatabaseGroupPollingDiffSingleton extends DatabaseGroupPollingDiff {

	private static DatabaseGroupPollingDiffSingleton instance = null;
	private DatabaseGroupPollingDiffSingleton() {}
	public static DatabaseGroupPollingDiffSingleton getInstance() { 
		if ( null == instance ) instance = new DatabaseGroupPollingDiffSingleton();
		return instance;
	}
	
}
