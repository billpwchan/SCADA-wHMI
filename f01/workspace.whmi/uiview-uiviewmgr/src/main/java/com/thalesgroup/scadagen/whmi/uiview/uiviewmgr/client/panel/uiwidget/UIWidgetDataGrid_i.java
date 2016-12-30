package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDataGrid_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, DataGrid("DataGrid")
		, TargetDataGrid("TargetDataGrid")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum DataGridEvent {
		  RowSelected("RowSelected")
		;
		private final String text;
		private DataGridEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
