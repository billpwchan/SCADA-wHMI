package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIActionEventAttribute_i {
	
	public enum UIActionEventType {
		  widget("widget")
		, event("event")
		, logic("logic")
		, ctl("ctl")
		, dpc("dpc")
		, alm("alm")
		, actionsetkey("actionsetkey")
		, actionkey("actionkey")
		, actionsetinit("actionsetinit")
		, actionset("actionset")
		, action("action")
		;
		private final String text;
		private UIActionEventType(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventType[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum UIActionEventLogicAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		;
		private final String text;
		private UIActionEventLogicAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventLogicAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum UIActionEventAttribute {
		  Operation("Operation")
		, OperationAction("OperationAction")
		, OperationType("OperationType")
		, OperationTarget("OperationTarget")
		;
		private final String text;
		private UIActionEventAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum WidgetExecuteAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		, OperationString6("OperationString6")
		, OperationString7("OperationString7")
		;
		private final String text;
		private WidgetExecuteAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			WidgetExecuteAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum EventExecuteAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		;
		private final String text;
		private EventExecuteAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			EventExecuteAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ActionAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		;
		private final String text;
		private ActionAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ActionAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ActionObjectAttribute {
		  OperationObject1("OperationObject1")
		, OperationObject2("OperationObject2")
		, OperationObject3("OperationObject3")
		, OperationObject4("OperationObject4")
		, OperationObject5("OperationObject5")
		;
		private final String text;
		private ActionObjectAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ActionObjectAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ActionSetAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		;
		private final String text;
		private ActionSetAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ActionSetAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
