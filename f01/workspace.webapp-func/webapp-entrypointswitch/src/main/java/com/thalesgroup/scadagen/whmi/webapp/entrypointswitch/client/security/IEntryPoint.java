package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security;

public interface IEntryPoint {
	
	public final static String STR_XML_FOLDER = "UIEntryPoint" + "\\" + ScsLoginEntryPoint.class.getSimpleName();
	
	public enum PropertiesName {
		  uiProj("uiProj")
		, uiFrmw("uiFrmw")
		;
		private final String text;
		private PropertiesName(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			PropertiesName[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
}
