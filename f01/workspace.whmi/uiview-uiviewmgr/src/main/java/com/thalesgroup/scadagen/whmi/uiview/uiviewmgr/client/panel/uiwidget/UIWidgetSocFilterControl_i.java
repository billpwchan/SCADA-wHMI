package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocFilterControl_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, TargetDataGrid("TargetDataGrid")
		, TargetDataGridColumn1("TargetDataGridColumn1")
		, TargetDataGridColumn2("TargetDataGridColumn2")
		, TargetDataGridColumn3("TargetDataGridColumn3")
		, TargetDataGridColumn4("TargetDataGridColumn4")
		, TargetDataGridColumn5("TargetDataGridColumn5")
		, TargetDataGridColumn6("TargetDataGridColumn6")
		, TargetDataGridColumn7("TargetDataGridColumn7")
		, TargetDataGridColumn8("TargetDataGridColumn8")
		, TargetDataGridColumn1Value("TargetDataGridColumn1Value")
		, TargetDataGridColumn2Value("TargetDataGridColumn2Value")
		, TargetDataGridColumn3Value("TargetDataGridColumn3Value")
		, TargetDataGridColumn4Value("TargetDataGridColumn4Value")
		, TargetDataGridColumn5Value("TargetDataGridColumn5Value")
		, TargetDataGridColumn6Value("TargetDataGridColumn6Value")
		, TargetDataGridColumn7Value("TargetDataGridColumn7Value")
		, TargetDataGridColumn8Value("TargetDataGridColumn8Value")
		, TargetDataGridColumn1Status("TargetDataGridColumn1Status")
		, TargetDataGridColumn2Status("TargetDataGridColumn2Status")
		, TargetDataGridColumn3Status("TargetDataGridColumn3Status")
		, TargetDataGridColumn4Status("TargetDataGridColumn4Status")
		, TargetDataGridColumn5Status("TargetDataGridColumn5Status")
		, TargetDataGridColumn6Status("TargetDataGridColumn6Status")
		, TargetDataGridColumn7Status("TargetDataGridColumn7Status")
		, TargetDataGridColumn8Status("TargetDataGridColumn8Status")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
