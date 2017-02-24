package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.cache;

import java.util.HashMap;

public class DatabaseGetChildrenCache {
	
	private HashMap<String, HashMap<String, String[]>> caches = null;
	
	private static DatabaseGetChildrenCache instance = null;
	private DatabaseGetChildrenCache() {
		caches = new HashMap<String, HashMap<String, String[]>>();
	}
	public static DatabaseGetChildrenCache getInstance() {
		if ( null == instance ) instance = new DatabaseGetChildrenCache();
		return instance;
	}
	
	public HashMap<String, HashMap<String, String[]>> getCaches() {
		return caches;
		
	}
}
