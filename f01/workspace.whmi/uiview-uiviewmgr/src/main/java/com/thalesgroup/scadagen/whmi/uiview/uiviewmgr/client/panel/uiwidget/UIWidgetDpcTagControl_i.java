package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetDpcTagControl_i {
	
	public final static int TAG_UNKNOW      = 0;
	public final static int TAG_DEACTIVATED = 1;
	public final static int TAG_ACTIVATED   = 2;
	
	public enum ParameterName {
		  ColumnAlias("ColumnAlias")
		, ColumnStatus("ColumnStatus")
		, ColumnServiceOwner("ColumnServiceOwner")
		, ColumnOpSource("ColumnOpSource")
		
		, ValueSet("ValueSet")
		, ValueUnSet("ValueUnSet")
		, ValueOpSourceEmpty("ValueOpSourceEmpty")
		
		, UICpApi("UICpApi")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
