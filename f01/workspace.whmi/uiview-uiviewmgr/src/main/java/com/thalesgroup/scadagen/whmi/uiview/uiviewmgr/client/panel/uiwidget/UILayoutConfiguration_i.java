package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UILayoutConfiguration_i {
	public enum UILayoutConfigurationParameter {
		  Parameter1("Parameter1")
		, Parameter2("Parameter2")
		, Parameter3("Parameter3")
		, Parameter4("Parameter4")
		, Parameter5("Parameter5")
		, Parameter6("Parameter6")
		, Parameter7("Parameter7")
		, Parameter8("Parameter8")
		;
		private final String text;
		private UILayoutConfigurationParameter(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UILayoutConfigurationParameter[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
