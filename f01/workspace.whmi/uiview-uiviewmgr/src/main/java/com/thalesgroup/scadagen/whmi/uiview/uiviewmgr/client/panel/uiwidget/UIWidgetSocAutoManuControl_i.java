package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocAutoManuControl_i {
	// Event Send from AutoManu
	public enum AutoManuEvent {
		  RadioBoxSelected("RadioBoxSelected")
		;
		private final String text;
		private AutoManuEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
