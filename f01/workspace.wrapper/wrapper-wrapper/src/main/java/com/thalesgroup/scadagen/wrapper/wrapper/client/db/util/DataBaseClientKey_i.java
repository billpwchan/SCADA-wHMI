package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

public interface DataBaseClientKey_i {
	
	public enum ClientKeyIndex {
		  API(0)
		, Widget(1)
		, Stability(2)
		, Screen(3)
		, Env(4)
		, Address(5)
		;
		private final int value;
		ClientKeyIndex(final int value) { this.value = value; }
		public int getValue() { return this.value; }
		public static String[] toStrings() {
			ClientKeyIndex[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	
	public enum API {
		  multiReadValue("multiReadValue")
		, GetChildren("GetChildren")
		;
		
		private final String text;
		private API(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			API[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum Stability {
		  STATIC("static")
		, DYNAMIC("dynamic")
		;
		
		private final String text;
		private Stability(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			Stability[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
