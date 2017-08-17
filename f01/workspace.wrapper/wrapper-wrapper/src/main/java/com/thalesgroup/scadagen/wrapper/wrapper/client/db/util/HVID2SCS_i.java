package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

public class HVID2SCS_i {
	public enum Pattern {
		  HVID_ALIAS("HVID_ALIAS")
		, ENV_ALIAS("ENV_ALIAS")
		;
		private final String text;
		private Pattern(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			Pattern[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
