package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIActionEventAttribute_i {
	
	public enum UIActionEventType {
		
		  event("event")
		, logic("logic")
		
		, alm("alm")
		, ctl("ctl")
		, dbm("dbm")
		, dialogmsg("dialogmsg")
		, dpc("dpc")
		, grc("grc")
		, opm("opm")
		, uitask("uitask")
		, widget("widget")
		
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

	
	public enum UIActionEventTargetAttribute {
		  OperationElement("OperationElement")
		;
		private final String text;
		private UIActionEventTargetAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventTargetAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	public enum UIActionEventSenderAttribute {
		  SenderOperation("SenderOperation")
		;
		private final String text;
		private UIActionEventSenderAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventSenderAttribute[] enums = values();
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

	public enum ActionAttribute {
		  OperationString1("OperationString1")
		, OperationString2("OperationString2")
		, OperationString3("OperationString3")
		, OperationString4("OperationString4")
		, OperationString5("OperationString5")
		, OperationString6("OperationString6")
		, OperationString7("OperationString7")
		, OperationString8("OperationString8")
		, OperationString9("OperationString9")
		, OperationString10("OperationString10")
		, OperationString11("OperationString11")
		, OperationString12("OperationString12")
		, OperationString13("OperationString13")
		, OperationString14("OperationString14")
		, OperationString15("OperationString15")
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

}
