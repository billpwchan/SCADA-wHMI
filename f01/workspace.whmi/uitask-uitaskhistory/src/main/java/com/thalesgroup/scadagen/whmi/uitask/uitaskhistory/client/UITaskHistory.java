package com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class UITaskHistory implements UITask_i {
	private String uiScreen = "";
	private String uiPath = "";
	public String getUiScreen() {
		return this.uiScreen;
	}
	public int getTaskUiScreen() {
		return Integer.parseInt(this.uiScreen);
	}
	public void setTaskUiScreen(int uiScreen) {
		this.uiScreen = Integer.toString(uiScreen);
	}
	public void setUiScreen(String uiScreen) {
		this.uiScreen = uiScreen;
	}
	public String getUiPath() {
		return uiPath;
	}
	public void setUiPath(String uiPath) {
		this.uiPath = uiPath;
	}
	
	/**
	 * Previous, Next
	 */
	public enum TaskType {Previous, Next, PreviousEnable, PreviousDisable, NextEnable, NextDisable}
	private TaskType taskType;
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}


}
