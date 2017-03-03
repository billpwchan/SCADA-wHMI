package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDioBtnsControl_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		
		, ColumnAlias("ColumnAlias")
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
		
		, IsAliasAndAlias2("IsAliasAndAlias2")
		, ColumnAlias2("ColumnAlias2")
		, SubstituteFrom("SubstituteFrom")
		, SubstituteTo("SubstituteTo")
		
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
