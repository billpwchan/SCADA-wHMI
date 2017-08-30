package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches for the Get Children 
 * 
 * @author syau
 *
 */
public class DatabaseGetFullPathCache {
	
	private Map<String, Map<String, String>> caches = null;
	
	private static DatabaseGetFullPathCache instance = null;
	private DatabaseGetFullPathCache() {
		caches = new HashMap<String, Map<String, String>>();
	}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseGetFullPathCache getInstance() {
		if ( null == instance ) instance = new DatabaseGetFullPathCache();
		return instance;
	}
	
	public Map<String, Map<String, String>> getCaches() {
		return caches;
		
	}
}
