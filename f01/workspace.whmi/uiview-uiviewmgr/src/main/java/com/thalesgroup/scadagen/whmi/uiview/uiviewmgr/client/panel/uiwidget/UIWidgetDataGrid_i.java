package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDataGrid_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, DataGrid("DataGrid")
		
		, DataGridColumnsType("DataGridColumnsType")
		, DataGridColumnsHeaderString("DataGridColumnsHeaderString")
		, DataGridColumnsHeaderTranslation("DataGridColumnsHeaderTranslation")
		, DataGridColumnsLabel("DataGridColumnsLabel")
		, DataGridColumnsWidth("DataGridColumnsWidth")
		, DataGridColumnsTranslation("DataGridColumnsTranslation")
		, DataGridColumnsSort("DataGridColumnsSort")
		, DataGridOptsXMLFile("DataGridOptsXMLFile")
		, DataGridPagerName("DataGridPagerName")
		, DataGridPageSize("DataGridPageSize")
		, DataGridFastForwardRows("DataGridFastForwardRows")
		, DataGridEmptyLabel("DataGridEmptyLabel")
		, DataGridCssFlagColumn("DataGridCssFlagColumn")
		
		, TargetDataGrid_A("TargetDataGrid_A")
		, TargetDataGridColumn_A("TargetDataGridColumn_A")
		, TargetDataGridColumn_A2("TargetDataGridColumn_A2")
		, TargetDataGridColumn_A3("TargetDataGridColumn_A3")
		, TargetDataGrid_B("TargetDataGrid_B")
		, TargetDataGridColumn_B("TargetDataGridColumn_B")
		, TargetDataGridColumn_B2("TargetDataGridColumn_B2")
		
		, MaxDelayAfterSuccess("MaxDelayAfterSuccess")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum DataGridEvent {
		  RowSelected("RowSelected")
		, UpdateRowCssClass("UpdateRowCssClass")
		, ValueChange("ValueChange")
		, ReloadFromDataSource("ReloadFromDataSource")
		, ResetColumnData("ResetColumnData")
		, ReloadColumnData("ReloadColumnData")
		, ColumnFilterChange("ColumnFilterChange")
		, DisableCheckBox("DisableCheckBox")
		;
		private final String text;
		private DataGridEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
