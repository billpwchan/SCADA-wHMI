package com.thalesgroup.scadagen.whmi.config.config.shared;

import java.util.Set;

public interface Dictionary_i {

	public void setAttribute(Object key, Object value);
	public Object getAttribute(Object key);
	public Set<Object> getAttributeKeys();

	public void addValue(Object key, Object value);
	public Object getValue(Object key);
	public Set<Object> getValueKeys();

	public void addValue(Object key);
	public Object[] getValues();
	
	public void setData(String value);
	public String getData();
}
