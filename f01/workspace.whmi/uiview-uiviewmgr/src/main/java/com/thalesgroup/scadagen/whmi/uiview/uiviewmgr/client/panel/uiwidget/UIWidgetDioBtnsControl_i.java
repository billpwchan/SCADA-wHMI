package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDioBtnsControl_i {
	public enum ParameterName {
		  ColumnAlias("ColumnAlias")
		, ColumnServiceOwner("ColumnServiceOwner")
		
		, IsPolling("IsPolling")
		, SubScribeMethod1("SubScribeMethod1")
		, MultiReadMethod1("MultiReadMethod1")
		, MultiReadMethod2("MultiReadMethod2")
		, DotValueTable("DotValueTable")
		, DotInitCondGL("DotInitCondGL")
		
		, InitCondGLValid("InitCondGLValid")
		
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
