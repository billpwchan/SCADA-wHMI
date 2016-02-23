package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

public class UITaskTitle extends UITaskDictionary {
	public UITaskTitle() {
		super();
	}
	public UITaskTitle(UITaskTitle uiTaskTitle) {
		super(uiTaskTitle);
		setTitle(uiTaskTitle.getTitle());
		setStation(uiTaskTitle.getStation());
	}
	
	private String strTitle = "Title";
	private String strStation = "Station";
	public String getTitle() { return (String) getValue(strTitle); }
	public void setTitle(String title) { setValue(strTitle, title); }
	
	public String getStation() { return (String) getValue(strStation); }
	public void setStation(String station) { setValue(strStation, station); }
}
