package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction;

public interface UIWidgetVerifyUIEventActionControl_i {
	public enum SubjectAttribute {
		  SubjectAttribute1("SubjectAttribute1")
		, SubjectAttribute2("SubjectAttribute2")
		, SubjectAttribute3("SubjectAttribute3")
		;
		private final String text;
		private SubjectAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			SubjectAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
