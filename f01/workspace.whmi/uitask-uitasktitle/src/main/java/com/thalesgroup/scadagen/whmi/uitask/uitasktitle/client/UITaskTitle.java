package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

//public class UITaskTitle implements UITask_i {
//	private String uiScreen = "";
//	private String uiPath = "";
//	private String station = "";
//	private String title = "";
//	public String getUiScreen() {
//		return this.uiScreen;
//	}
//	public int getTaskUiScreen() {
//		return Integer.parseInt(this.uiScreen);
//	}
//	public void setTaskUiScreen(int uiScreen) {
//		this.uiScreen = Integer.toString(uiScreen);
//	}
//	public void setUiScreen(String uiScreen) {
//		this.uiScreen = uiScreen;
//	}
//	public String getUiPath() {
//		return uiPath;
//	}
//	public void setUiPath(String uiPath) {
//		this.uiPath = uiPath;
//	}
//	
//	
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	
//	public String getStation() {
//		return this.station;
//	}
//	public void setStation(String station){
//		this.station = station;
//	}
//
//}
public class UITaskTitle extends UITaskDictionary {
	public UITaskTitle() {
		super();
	}
	public String getTitle() { return (String) getValue("title"); }
	public void setTitle(String title) { setValue("title", title); }
	
	public String getStation() { return (String) getValue("station"); }
	public void setStation(String station) { setValue("station", station); }
}
