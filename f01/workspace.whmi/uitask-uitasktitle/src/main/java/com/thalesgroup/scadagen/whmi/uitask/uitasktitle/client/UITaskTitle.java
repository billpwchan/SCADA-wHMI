package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

public class UITaskTitle extends UITaskDictionary {
	public UITaskTitle() {
		super();
	}
	public UITaskTitle(UITaskTitle uiTaskTitle) {
		super(uiTaskTitle);
		setTitle(uiTaskTitle.getTitle());
	}
	
	private String strTitle = "Title";
	public String getTitle() { return (String) getValue(strTitle); }
	public void setTitle(String title) { setValue(strTitle, title); }
}
