package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;

public class Equipment implements Equipment_i /*, Comparable<Equipment_i>*/ {
	
	private HashMap<String, Object> hashMap = new HashMap<String, Object>();
	
	@Override
	public String getStringValue(String key) {
		return (String)hashMap.get(key);
	}
	@Override
	public void setStringValue(String key, String value) {
		hashMap.put(key, value);
	}
	@Override
	public Number getNumberValue(String key) {
		return (Number)hashMap.get(key);
	}
	@Override
	public void setNumberValue(String key, Number value) {
		hashMap.put(key, value);
	}
	@Override
	public boolean getBooleanValue(String key) {
		return (Boolean)hashMap.get(key);
	}
	@Override
	public void setBooleanValue(String key, boolean value) {
		hashMap.put(key, value);
	}
	
	@Override
	public String getValue(String key) {
		return hashMap.get(key).toString();
	}
	
	@Override
	public void setValue(String key, Object value) {
		hashMap.put(key, value);
	}
	
	@Override
	public String[] getFields() {
		return hashMap.keySet().toArray(new String[0]);
	}
	/**
	 * A simple data type that represents an Address.
	 */
	
	/*		
	public static final ProvidesKey<Equipment> KEY_PROVIDER = new ProvidesKey<Equipment>() {
		@Override
		public Object getKey(Equipment item) {
			return item == null ? null : item.getId();
		}
	};
	
    @Override
    public int compareTo(Equipment o) {
      return (o == null || o.firstName == null) ? -1 : -o.firstName.compareTo(firstName);
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Equipment) {
        return id == ((Equipment) o).id;
      }
      return false;
    }
    */
}
