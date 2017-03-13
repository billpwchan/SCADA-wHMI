package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public class UIEventActionCtrl_i {
	public enum UIEventActionCtrlAction {
		  SendIntControl("SendIntControl")
		, SendFloatControl("SendFloatControl")
		, SendStringControl("SendStringControl")
		;
		private final String text;
		private UIEventActionCtrlAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
