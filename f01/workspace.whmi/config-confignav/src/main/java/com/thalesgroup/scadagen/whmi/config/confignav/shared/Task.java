package com.thalesgroup.scadagen.whmi.config.confignav.shared;

import java.util.HashMap;

public class Task implements java.io.Serializable {
	
	private static final long serialVersionUID = -1367068029420283265L;
	
	public Task() { }
	
	private HashMap<String, String> parameters = new HashMap<String, String>();
	
	public String getParameter(String key) { 
		String value = parameters.get(key);
		return (null!=value?value:"");
	}
	
	public void setParameter(String key, String value) { 
		parameters.put(key, value); 
	}
}
