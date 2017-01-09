package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionDpc_i {
	public enum UIEventActionDpcAction {
		  SendChangeValueStatus("SendChangeValueStatus")
		, SendChangeEqpTag("SendChangeEqpTag")
		;
		private final String text;
		private UIEventActionDpcAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
