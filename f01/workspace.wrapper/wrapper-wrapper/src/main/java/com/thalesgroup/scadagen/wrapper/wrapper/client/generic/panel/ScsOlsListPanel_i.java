package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

public interface ScsOlsListPanel_i {
	public enum GDGAttribute {
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
		private GDGAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			GDGAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ParameterName {
		  MwtEventBusName("MwtEventBusName")
		, MwtEventBusScope("MwtEventBusScope")
		, ListConfigId("ListConfigId")
		, MenuEnableCallImage("MenuEnableCallImage")
		, SelectionMode("SelectionMode")
		, ColorMode("ColorMode")
		, EventBus("EventBus")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ParameterName[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum ParameterValue {
		  Multiple("Multiple")
		, Single("Single")  
		, Global("Global")
		, True("True")
		, ColorMode("ColorMode")
		;
		private final String text;
		private ParameterValue(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ParameterValue[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
