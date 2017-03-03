package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDioBtnsControl_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, ColumnAlias("ColumnAlias")
		, ColumnStatus("ColumnStatus")
		, ColumnServiceOwner("ColumnServiceOwner")
		
		, IsPolling("IsPolling")
		, DatabaseGroupPollingDiffSingleton("DatabaseGroupPollingDiffSingleton")
		, DatabaseMultiReadingProxySingleton("DatabaseMultiReadingProxySingleton")
		, DatabaseMultiReading("DatabaseMultiReading")
		, DotValueTable("DotValueTable")
		, DotInitCondGL("DotInitCondGL")
		
		, RowInValueTable("RowInValueTable")
		, DovnameCol("DovnameCol")
		, LabelCol("LabelCol")
		, ValueCol("ValueCol")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
