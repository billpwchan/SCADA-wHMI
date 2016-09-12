package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetFilter_i {

	public enum ParameterName {
		  SimpleEventBus("SimpleEventBus")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}

	public enum FilterParameter {
		  FilterListCfgId("FilterListCfgId")
		, FilterColumn0("FilterColumn0")
		, FilterValueSet0("FilterValueSet0")
		;
		private final String text;
		private FilterParameter(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			FilterParameter[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}

	public enum FilterViewEvent {
		  AddFilter("AddFilter")
		, RemoveFilter("RemoveFilter")
		;
		private final String text;
		private FilterViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
