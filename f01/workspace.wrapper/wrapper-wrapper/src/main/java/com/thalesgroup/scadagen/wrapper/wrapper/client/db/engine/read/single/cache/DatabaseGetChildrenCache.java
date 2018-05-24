package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches for the Get Children 
 * 
 * @author t0096643
 *
 */
public class DatabaseGetChildrenCache {
	
	private Map<String, Map<String, String[]>> caches = null;
	
	private static DatabaseGetChildrenCache instance = null;
	private DatabaseGetChildrenCache() {
		caches = new HashMap<String, Map<String, String[]>>();
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
	
	public Map<String, Map<String, String[]>> getCaches() {
		return caches;
		
	}
}
