package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

/**
 * Implementation the Database Get Children Operation with Proxy (Caches) and Singleton
 * 
 * @author t0096643
 *
 */
public class DatabaseGetFullPathProxySingleton extends DatabaseGetFullPathProxy {
	
	private static DatabaseGetFullPathProxySingleton instance = null;
	private DatabaseGetFullPathProxySingleton() {}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseGetFullPathProxySingleton getInstance() {
		if  ( null == instance ) instance = new DatabaseGetFullPathProxySingleton();
		return instance;
	}
	
}
