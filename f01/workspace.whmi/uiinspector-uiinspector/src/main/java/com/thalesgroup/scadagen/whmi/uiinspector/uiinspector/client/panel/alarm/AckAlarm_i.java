package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.alarm;

public interface AckAlarm_i {
	
	public final String STR_OLS_MGR = "OlsMgr";
	public final String STR_OLS_MGR_KEY = "AckAlarm";
	
	public final String STR_ALM_SERVER = "AlmServer";
	public final String STR_LIST_NAME = "AlarmList";
	public final String STR_ALARM_FIELDS = "AlarmId";
	
	public final String STR_KEYWORD = "{}";
	public final String STR_FILTER_MSG_LIKE = "( Message LIKE \"{}\" )";
	public final String STR_FILTER_CONCAT = " OR ";
	public final String STR_FILTER = "( ( {} ) AND  (AcknowledgeRequired == 1) )";
}
