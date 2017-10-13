package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.alarm;

public interface AckAlarm_i {
	
	public final static String STR_OLS_MGR = "OlsMgr";
	public final static String STR_OLS_MGR_KEY = "AckAlarm";
	
	public final static String STR_ALM_SERVER = "AlmServer";
	public final static String STR_LIST_NAME = "AlarmList";
	public final static String STR_ALARM_FIELDS = "AlarmId";
	
	public final static String STR_KEYWORD = "{}";
	public final static String STR_FILTER_MSG_LIKE = "( Message LIKE \"{}\" )";
	public final static String STR_FILTER_CONCAT = " OR ";
	public final static String STR_FILTER = "( ( {} ) AND  (AcknowledgeRequired == 1) )";
}
