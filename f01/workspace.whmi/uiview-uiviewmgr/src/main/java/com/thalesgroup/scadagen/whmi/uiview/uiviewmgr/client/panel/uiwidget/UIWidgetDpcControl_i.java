package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDpcControl_i {
	public enum ParameterName {
		  ColumnAlias("ColumnAlias")
		, ColumnStatus("ColumnStatus")
		, ColumnServiceOwner("ColumnServiceOwner")
		, ValueSet("ValueSet")
		, ValueUnSet("ValueUnSet")
		, PeriodMillis("PeriodMillis")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
