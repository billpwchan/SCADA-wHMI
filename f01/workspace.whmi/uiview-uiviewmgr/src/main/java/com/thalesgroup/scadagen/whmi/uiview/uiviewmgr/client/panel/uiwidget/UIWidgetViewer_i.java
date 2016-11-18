package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetViewer_i {
	// Event Send from View
	public enum ViewerViewEvent {
		  FilterAdded("FilterAdded")
		, FilterRemoved("FilterRemoved")
		, RowSelected("RowSelected")
		, CounterValueChanged("CounterValueChanged")
		, AckVisible("AckVisible")
		, AckVisibleSelected("AckVisibleSelected")
		;
		private final String text;
		private ViewerViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
