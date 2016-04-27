package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

import java.util.LinkedHashMap;

public class Point {
	private String address = null;
	private LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
	public String[] getAttributeKeys() { return attributes.keySet().toArray(new String[0]); }
	public Point(String address, String[] keys) { 
		this.address = address;
		for(int i=0;i<keys.length;++i) { attributes.put(keys[i], null); } 
	}
	public String getAddress() { return address; }
	public void setValue(String attribute, String value) { attributes.put(attribute, value); }
	public String getValue(String attribute) { return attributes.get(attribute); }
}
