package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIView_i
{
	public enum WidgetParameterName {
		SimpleEventBus("SimpleEventBus")
		, MwtEventBus("MwtEventBus")
		, ScsEnvIds("ScsEnvIds")
		;
		private final String text;
		private WidgetParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	public enum ViewOperation {
		  Operation("Operation")
		, Operation1("Operation1")
		, Operation2("Operation2")
		, Operation3("Operation3")
		, Operation4("Operation4")
		, Operation5("Operation5")
		, Operation6("Operation6")
		, Operation7("Operation7")
		, Operation8("Operation8")
		, Operation9("Operation9")
		, Operation10("Operation10")
		, Operation11("Operation11")
		;
		private final String text;
		private ViewOperation(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ViewOperation[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	public enum ViewAttribute {
		  OperationTarget("OperationTarget")
		, Operation("Operation")
		, OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		, OperationObject1("OperationObject1")
		, OperationObject2("OperationObject2")
		, OperationObject3("OperationObject3")
		, OperationObject4("OperationObject4")
		, OperationObject5("OperationObject5")
		;
		private final String text;
		private ViewAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ViewAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}

}
