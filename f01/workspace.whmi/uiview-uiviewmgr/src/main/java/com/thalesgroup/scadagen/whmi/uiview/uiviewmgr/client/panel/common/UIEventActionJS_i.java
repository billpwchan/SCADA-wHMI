package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionJS_i {
	public enum UIEventActionJSAction {
		CallJSByGWT("CallJSByGWT")
		;
		private final String text;
		private UIEventActionJSAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}	
}
