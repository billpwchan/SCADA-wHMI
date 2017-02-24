package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single;

public class DatabaseGetChildrenSingleton extends DatabaseGetChildren {
	
	private static DatabaseGetChildrenSingleton instance = null;
	private DatabaseGetChildrenSingleton() {}
	public static DatabaseGetChildrenSingleton getInstance() {
		if ( null == instance ) instance = new DatabaseGetChildrenSingleton();
		return instance;
	}
	
}
