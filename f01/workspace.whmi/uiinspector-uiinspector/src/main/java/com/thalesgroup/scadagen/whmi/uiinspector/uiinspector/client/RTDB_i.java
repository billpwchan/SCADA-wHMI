package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

public interface RTDB_i {
	
	public final String aciPrefix	= "aci";
	public final String dciPrefix	= "dci";
	public final String sciPrefix	= "sci";
	
	public final String aioPrefix	= "aio";
	public final String dioPrefix	= "dio";
	public final String sioPrefix	= "sio";
	
	public final String strCSSStatusGreen		= "project-gwt-inlinelabel-inspector-info-status-green";
	public final String strCSSStatusRed			= "project-gwt-inlinelabel-inspector-info-status-red";
	public final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector-info-status-blue";
	public final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector-info-status-grey";

	// Static Attribute
	final String strLabel				= ".label";
	final String strValueTable			= ":dal.valueTable";
	final String strHmiOrder			= ".hmiOrder";
	
	// Dynamic Attribute
	final String strValue				= ".value";
	final String strValidity			= ".validity"; // 0=invalid, 1=valid
	final String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
	final String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus
	
	final int intMO = 2, intAI = 8, intSS = 512;
}
