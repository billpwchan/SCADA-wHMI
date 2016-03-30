package com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;

public class UITaskSplit extends UITaskDictionary {
	public UITaskSplit() {
		super();
	}
	public UITaskSplit(UITaskSplit uiTaskSplit) {
		super(uiTaskSplit);
		setTaskType(uiTaskSplit.getTaskType());
	}
	
	/**
	 * Horizontal, Vertical
	 */
	public enum SplitType {Horizontal, Vertical, HorizontalEnable, HorizontalHightLight, HorizontalDisable, VerticalEnable, VerticalDisable, VerticalHightLight}
	private SplitType splitType;
	public SplitType getTaskType() {
		return splitType;
	}
	public void setTaskType(SplitType splitType) {
		this.splitType = splitType;
	}

}
