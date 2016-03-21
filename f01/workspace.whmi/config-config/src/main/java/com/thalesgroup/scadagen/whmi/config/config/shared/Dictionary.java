package com.thalesgroup.scadagen.whmi.config.config.shared;

import java.util.HashMap;
import java.util.Set;

public class Dictionary implements Config_i, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3830180475332229852L;
	public Dictionary() {}
	public Dictionary(Dictionary dictionary) {
		for ( Object o : dictionary.getAttributeKeys() ) { this.setAttribute(o, dictionary.getAttribute(o)); }
		for ( Object o : dictionary.getValueKeys() ) { this.setValue(o, dictionary.getValue(o)); }
	}
	
	private HashMap<Object, Object> attributeMap = new HashMap<Object, Object>();
	public void setAttribute(Object key, Object value) { this.attributeMap.put(key, value); }
	public Object getAttribute(Object key) { return this.attributeMap.get(key); }
	public Set<Object> getAttributeKeys() { return this.attributeMap.keySet(); }
	
	private HashMap<Object, Object> valueMap = new HashMap<Object, Object>();
	public void setValue(Object key, Object value) { this.valueMap.put(key, value); }
	public Object getValue(Object key) { return this.valueMap.get(key); }
	public Set<Object> getValueKeys() { return this.valueMap.keySet(); }
}
