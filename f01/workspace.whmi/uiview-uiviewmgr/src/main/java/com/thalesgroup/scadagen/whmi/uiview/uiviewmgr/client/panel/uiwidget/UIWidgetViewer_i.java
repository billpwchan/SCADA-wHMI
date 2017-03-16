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
		
		, HasPreviousPage("HasPreviousPage")
		, HasNextPage("HasNextPage")
		, HasFastForwardPage("HasFastForwardPage")
		, HasFastBackwardPage("HasFastBackwardPage")
		
		, CreateText("CreateText")
		
		, FirstPageSelected("FirstPageSelected")
		, PreviousPageSelected("PreviousPageSelected")
		, NextPageSelected("NextPageSelected")
		, LastPageSelected("LastPageSelected")
		, FastForwardPageSelected("FastForwardPageSelected")
		, FastBackwardPageSelected("FastBackwardPageSelected")
		
		;
		private final String text;
		private ViewerViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ParameterName {
		  ScsOlsListElement("ScsOlsListElement")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
