package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

public interface Equipment_i {
	String getStringValue(String key);
	void setStringValue(String key, String value);
	
	Number getNumberValue(String key);
	void setNumberValue(String key, Number value);
	
	boolean getBooleanValue(String key);
	void setBooleanValue(String key, boolean value);
	
	void setValue(String key, Object value);
	String getValue(String key);

	String[] getFields();

}
