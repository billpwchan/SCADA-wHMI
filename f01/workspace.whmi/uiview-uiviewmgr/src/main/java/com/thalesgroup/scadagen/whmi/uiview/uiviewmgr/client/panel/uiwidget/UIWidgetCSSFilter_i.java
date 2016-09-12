package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetCSSFilter_i {
	
	public enum ViewActionSetKey {
		  OperationSetKey1("OperationSetKey1")
		, OperationSetKey2("OperationSetKey2")
		, OperationSetKey3("OperationSetKey3")
		, OperationSetKey4("OperationSetKey4")
		, OperationSetKey5("OperationSetKey5")
		;
		private final String text;
		private ViewActionSetKey(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ViewActionSetKey[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ViewActionKeyOperation {
		  OperationSetAction1("OperationSetAction1")
		, OperationSetAction2("OperationSetAction2")
		, OperationSetAction3("OperationSetAction3")
		, OperationSetAction4("OperationSetAction4")
		, OperationSetAction5("OperationSetAction5")
		;
		private final String text;
		private ViewActionKeyOperation(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ViewActionKeyOperation[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
}
