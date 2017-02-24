package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.write;

public class DatabaseWritingSingleton extends DatabaseWriting {

	private static DatabaseWritingSingleton instance = null;
	private DatabaseWritingSingleton() {}
	public static DatabaseWritingSingleton getInstance() { 
		if ( null == instance ) instance = new DatabaseWritingSingleton();
		return instance;
	}
	
}
