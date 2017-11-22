package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

/**
 * Implementation the Database Get Children Operation with Proxy (Caches) and Singleton
 * 
 * @author syau
 *
 */
public class DatabaseGetChildrenProxySingleton extends DatabaseGetChildrenProxy {
	
	private static DatabaseGetChildrenProxySingleton instance = null;
	private DatabaseGetChildrenProxySingleton() {}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseGetChildrenProxySingleton getInstance() {
		if  ( null == instance ) instance = new DatabaseGetChildrenProxySingleton();
		return instance;
	}
	
}
