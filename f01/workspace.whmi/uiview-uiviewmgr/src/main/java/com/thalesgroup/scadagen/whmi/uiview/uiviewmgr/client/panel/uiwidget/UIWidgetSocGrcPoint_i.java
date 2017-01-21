package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocGrcPoint_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, TargetDataGrid("TargetDataGrid")
		, TargetDataGridColumn("TargetDataGridColumn")
		, TargetDataGridColumn2("TargetDataGridColumn2")
		, TargetDataGridColumn3("TargetDataGridColumn3")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum GrcPointEvent {
		  CurStep("GrcCurStep")
		,  CurStatus("GrcCurStatus")
		;
		private final String text;
		private GrcPointEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
