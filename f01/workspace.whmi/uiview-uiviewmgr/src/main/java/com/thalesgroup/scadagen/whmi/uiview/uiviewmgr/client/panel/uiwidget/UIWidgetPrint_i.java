package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetPrint_i {
	public enum PrintViewEvent {
		Print("Print")
		;
		private final String text;
		private PrintViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
