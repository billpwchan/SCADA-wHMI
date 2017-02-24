package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe;

public class DatabaseSubscriptionSingleton extends DatabaseSubscription {

	private static DatabaseSubscriptionSingleton instance = null;
	private DatabaseSubscriptionSingleton() {}
	public static DatabaseSubscriptionSingleton getInstance() { 
		if ( null == instance ) instance = new DatabaseSubscriptionSingleton();
		return instance;
	}
	
}
