package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocDelayControl_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, TargetDataGrid("TargetDataGrid")
		, TargetDataGridColumnStep("TargetDataGridColumnStep")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
