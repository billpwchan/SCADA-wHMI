package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionDialogMsg_i {
	public enum UIEventActionDialogMsgAction {
		  UIDialogMsg("UIDialogMsg")
		;
		private final String text;
		private UIEventActionDialogMsgAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
