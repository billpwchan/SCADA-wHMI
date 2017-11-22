package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;

/**
 * Caches for the Get Children 
 * 
 * @author syau
 *
 */
public class DatabaseGetChildrenCache {
	
	private HashMap<String, HashMap<String, String[]>> caches = null;
	
	private static DatabaseGetChildrenCache instance = null;
	private DatabaseGetChildrenCache() {
		caches = new HashMap<String, HashMap<String, String[]>>();
	}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseGetChildrenCache getInstance() {
		if ( null == instance ) instance = new DatabaseGetChildrenCache();
		return instance;
	}
	
	public HashMap<String, HashMap<String, String[]>> getCaches() {
		return caches;
		
	}
}
