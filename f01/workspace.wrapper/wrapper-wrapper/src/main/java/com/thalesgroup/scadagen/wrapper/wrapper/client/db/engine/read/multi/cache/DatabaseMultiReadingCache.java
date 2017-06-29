package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.multi.cache;

import java.util.HashMap;

/**
 * Caches for the Multi Reading
 * 
 * @author syau
 *
 */
public class DatabaseMultiReadingCache {
	
	private HashMap<String, HashMap<String, String>> caches = null;
	
	private static DatabaseMultiReadingCache instance = null;
	private DatabaseMultiReadingCache () {
		caches = new HashMap<String, HashMap<String, String>>();
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
	
	public HashMap<String, HashMap<String, String>> getCache() {
		return caches;
	}
}
