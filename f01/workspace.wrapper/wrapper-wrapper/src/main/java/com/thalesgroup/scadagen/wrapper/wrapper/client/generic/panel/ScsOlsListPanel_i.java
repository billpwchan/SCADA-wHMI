package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

public interface ScsOlsListPanel_i {
	public enum GDGAttribute {
		  GDGAttributeString1("GDGAttributeString1")
		, GDGAttributeString2("GDGAttributeString2")
		, GDGAttributeString3("GDGAttributeString3")
		, GDGAttributeString4("GDGAttributeString4")
		, GDGAttributeString5("GDGAttributeString5")
		, GDGAttributeString6("GDGAttributeString6")
		, GDGAttributeString7("GDGAttributeString7")
		, GDGAttributeString8("GDGAttributeString8")
		, GDGAttributeString9("GDGAttributeString9")
		, GDGAttributeString10("GDGAttributeString10")
		, GDGAttributeString11("GDGAttributeString11")
		, GDGAttributeString12("GDGAttributeString12")
		, GDGAttributeString13("GDGAttributeString13")
		, GDGAttributeString14("GDGAttributeString14")
		, GDGAttributeString15("GDGAttributeString15")
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
		, OlsCssPrefix("OlsCssPrefix")
		, EventBus("EventBus")
		, PagerName("PagerName")
		, EnableRowExport("EnableRowExport")
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
		, SCADAgenPager("SCADAgenPager")
		
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
