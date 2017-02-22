package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocControl_i {	
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, TargetDataGrid_A("TargetDataGrid_A")
		, TargetDataGridColumn_A("TargetDataGridColumn_A")
		, TargetDataGridColumn_A2("TargetDataGridColumn_A2")
		, TargetDataGrid_B("TargetDataGrid_B")
		, TargetDataGridColumn_B("TargetDataGridColumn_B")
		, TargetDataGridColumn_B2("TargetDataGridColumn_B2")
		, TargetDataGridColumn_B3("TargetDataGridColumn_B3")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum CtlBrcStatus {
		NotExecuted(1)
		,Completed(2)
		,Failed(3)
		,Skipped(4);
		
		private int value;
		private CtlBrcStatus(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
}
