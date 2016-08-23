package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;

public class UITaskTitle extends UITaskDictionary {
	public UITaskTitle() {
		super();
	}
	public UITaskTitle(UITaskTitle uiTaskTitle) {
		super(uiTaskTitle);
		setTitle(uiTaskTitle.getTitle());
	}
	
	private final String strTitle = "Title";
	public String getTitle() { return (String) getValue(strTitle); }
	public void setTitle(String title) { setValue(strTitle, title); }
}
