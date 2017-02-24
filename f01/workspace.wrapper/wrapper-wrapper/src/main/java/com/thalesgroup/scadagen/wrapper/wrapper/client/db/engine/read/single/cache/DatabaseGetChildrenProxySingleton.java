package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

public class DatabaseGetChildrenProxySingleton extends DatabaseGetChildrenProxy {
	
	private static DatabaseGetChildrenProxySingleton instance = null;
	private DatabaseGetChildrenProxySingleton() {}
	public static DatabaseGetChildrenProxySingleton getInstance() {
		if  ( null == instance ) instance = new DatabaseGetChildrenProxySingleton();
		return instance;
	}
	
}
