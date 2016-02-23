package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

import java.util.HashMap;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class UITaskDictionary implements UITask_i {
	
	private String strUIScreen = "UIScreen";
	private String strUIPath = "UIPath";
	
	public String getUiScreen() { return (String)this.attributeMap.get(strUIScreen); }
	public int getTaskUiScreen() { return Integer.parseInt((String)this.attributeMap.get(strUIScreen)); }
	public void setTaskUiScreen(int uiScreen) { this.attributeMap.put(strUIScreen, Integer.toString(uiScreen)); }
	public void setUiScreen(String uiScreen) { this.attributeMap.put(strUIScreen, uiScreen); }
	public String getUiPath() { return (String)this.attributeMap.get(strUIPath); }
	public void setUiPath(String uiPath) { this.attributeMap.put(strUIPath, uiPath); }
	
	public UITaskDictionary(UITask_i uiTask_i) {
		setUiScreen(uiTask_i.getUiScreen());
		setUiPath(uiTask_i.getUiPath());
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
