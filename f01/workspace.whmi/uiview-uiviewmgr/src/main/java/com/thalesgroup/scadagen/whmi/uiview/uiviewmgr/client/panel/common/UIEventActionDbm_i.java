package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionDbm_i {
	public enum UIEventActionDbmAction {
		  WriteIntValue("WriteIntValue")
		, WriteFloatValue("WriteFloatValue")
		, WriteStringValue("WriteStringValue")
		;
		private final String text;
		private UIEventActionDbmAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
