package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi;

public class DatabaseMultiReadingSingleton extends DatabaseMultiReading {
	
	private static DatabaseMultiReadingSingleton instance = null;
	private DatabaseMultiReadingSingleton() {}
	public static DatabaseMultiReadingSingleton getInstance() {
		if ( null == instance ) instance = new DatabaseMultiReadingSingleton();
		return instance;
	}
	
}
