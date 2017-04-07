package com.thalesgroup.scadagen.whmi.config.config.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dictionary implements Dictionary_i, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3830180475332229852L;
	
	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();
	
	public Dictionary() {}
	public Dictionary(Dictionary dictionary) {
		for ( Object o : dictionary.getAttributeKeys() ) { this.setAttribute(o, dictionary.getAttribute(o)); }
		for ( Object o : dictionary.getValueKeys() ) { this.addValue(o, dictionary.getValue(o)); }
	}
	@Override
	public void setAttribute(Object key, Object value) { this.attributeMap.put(key, value); }
	@Override
	public Object getAttribute(Object key) { return this.attributeMap.get(key); }
	@Override
	public Set<Object> getAttributeKeys() { return this.attributeMap.keySet(); }
	
	private Map<Object, Object> valueMap = new HashMap<Object, Object>();
	@Override
	public void addValue(Object key, Object value) { this.valueMap.put(key, value); }
	@Override
	public Object getValue(Object key) { return this.valueMap.get(key); }
	@Override
	public Set<Object> getValueKeys() { return this.valueMap.keySet(); }
	
	private List<Object> valueSet = new ArrayList<Object>();
	@Override
	public void addValue(Object key) { this.valueSet.add(key); }
	@Override
	public Object[] getValues() { return this.valueSet.toArray((new Object[valueSet.size()])); }
	
	
	private String data = null;
	@Override
	public void setData(String data) { this.data = data; }
	@Override
	public String getData() { return data; }
	
}
