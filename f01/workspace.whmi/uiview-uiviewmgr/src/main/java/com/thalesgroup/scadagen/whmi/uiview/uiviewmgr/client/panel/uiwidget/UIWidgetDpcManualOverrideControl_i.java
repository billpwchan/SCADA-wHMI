package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDpcManualOverrideControl_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, ColumnAlias("ColumnAlias")
		, ColumnStatus("ColumnStatus")
		, ColumnServiceOwner("ColumnServiceOwner")
		, ValueSet("ValueSet")
		, ValueUnSet("ValueUnSet")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
