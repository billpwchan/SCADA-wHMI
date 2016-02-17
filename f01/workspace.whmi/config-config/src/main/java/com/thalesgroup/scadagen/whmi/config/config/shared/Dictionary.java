package com.thalesgroup.scadagen.whmi.config.config.shared;

import java.util.HashMap;
import java.util.Set;

public class Dictionary implements Config_i, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3830180475332229852L;
	public Dictionary() {}
	
	private HashMap<String, Object> attributeMap = new HashMap<String, Object>();
	public void setAttribute(String key, Object value) { this.attributeMap.put(key, value); }
	public Object getAttribute(String key) { return this.attributeMap.get(key); }
	public Set<String> getAttributeKeys() { return this.attributeMap.keySet(); }
	
	private HashMap<String, Object> contentMap = new HashMap<String, Object>();
	public void setValue(String key, Object value) { this.contentMap.put(key, value); }
	public Object getValue(String key) { return this.contentMap.get(key); }
	public Set<String> getValueKeys() { return this.contentMap.keySet(); }
}
