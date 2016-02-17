package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

public interface NavigationMgrEvent {
	
	public void isReady(int level, String header);
	
	public void setMenu(int initLevel, String initHeader, String launchHeader, boolean executeTask);
		
}
