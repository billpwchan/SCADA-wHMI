package com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;

public class UITaskHistory extends UITaskDictionary {
	public UITaskHistory() {
		super();
	}
	public UITaskHistory(UITaskHistory uiTaskHistory) {
		super(uiTaskHistory);

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
