package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionTaskLaunch_i {
	public enum UIEventActionTaskLaunchAction {
		  UITaskLaunch("UITaskLaunch")
		;
		private final String text;
		private UIEventActionTaskLaunchAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}