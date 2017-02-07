package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDataGrid_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, DataGrid("DataGrid")
		
		, DataGridColumnsType("DataGridColumnsType")
		, DataGridColumnsLabel("DataGridColumnsLabel")
		, DataGridColumnsWidth("DataGridColumnsWidth")
		, DataGridOptsXMLFile("DataGridOptsXMLFile")
		
		, TargetDataGrid_A("TargetDataGrid_A")
		, TargetDataGridColumn_A("TargetDataGridColumn_A")
		, TargetDataGridColumn_A2("TargetDataGridColumn_A2")
		, TargetDataGrid_B("TargetDataGrid_B")
		, TargetDataGridColumn_B("TargetDataGridColumn_B")
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
