package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches for the Multi Reading
 * 
 * @author t0096643
 *
 */
public class DatabaseMultiReadingCache {
	
	private Map<String, Map<String, String>> caches = null;
	
	private static DatabaseMultiReadingCache instance = null;
	private DatabaseMultiReadingCache () {
		caches = new HashMap<String, Map<String, String>>();
	}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseMultiReadingCache getInstance() {
		if ( null == instance ) instance = new DatabaseMultiReadingCache();
		return instance;
	}
	
	public Map<String, Map<String, String>> getCache() {
		return caches;
	}
}
