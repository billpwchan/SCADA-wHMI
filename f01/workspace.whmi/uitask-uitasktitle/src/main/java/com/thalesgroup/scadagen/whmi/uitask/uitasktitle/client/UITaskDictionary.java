package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

import java.util.HashMap;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class UITaskDictionary implements UITask_i {
	
//	private String uiScreen = "";
//	private String uiPath = "";
//	private String station = "";
//	private String title = "";
	public String getUiScreen() {
		//return this.uiScreen;
		return (String)this.attributeMap.get("UIScreen");
	}
	public int getTaskUiScreen() {
//		return Integer.parseInt(this.uiScreen);
		return Integer.parseInt((String)this.attributeMap.get("UIScreen"));
	}
	public void setTaskUiScreen(int uiScreen) {
//		this.uiScreen = Integer.toString(uiScreen);
		this.attributeMap.put("UIScreen", Integer.toString(uiScreen));
	}
	public void setUiScreen(String uiScreen) {
//		this.uiScreen = uiScreen;
		this.attributeMap.put("UIScreen", uiScreen);
	}
	public String getUiPath() {
//		return uiPath;
		return (String)this.attributeMap.get("UIPath");
	}
	public void setUiPath(String uiPath) {
//		this.uiPath = uiPath;
		this.attributeMap.put("UIPath", uiPath);
	}
	
	
	public UITaskDictionary() {}
	
	private HashMap<String, Object> attributeMap = new HashMap<String, Object>();
	public void setAttribute(String key, Object value) { this.attributeMap.put(key, value); }
	public Object getAttribute(String key) { return this.attributeMap.get(key); }
	public Set<String> getAttributeKeys() { return this.attributeMap.keySet(); }
	
	private HashMap<String, Object> contentMap = new HashMap<String, Object>();
	public void setValue(String key, Object value) { this.contentMap.put(key, value); }
	public Object getValue(String key) { return this.contentMap.get(key); }
	public Set<String> getValueKeys() { return this.contentMap.keySet(); }
}
