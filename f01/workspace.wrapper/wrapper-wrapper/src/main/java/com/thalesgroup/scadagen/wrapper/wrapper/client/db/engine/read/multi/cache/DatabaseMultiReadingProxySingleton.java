package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache;

public class DatabaseMultiReadingProxySingleton extends DatabaseMultiReadingProxy {
	
	private static DatabaseMultiReadingProxySingleton instance = null;
	private DatabaseMultiReadingProxySingleton() {}
	public static DatabaseMultiReadingProxySingleton getInstance() {
		if ( null == instance ) instance = new DatabaseMultiReadingProxySingleton();
		return instance;
	}
	
}
