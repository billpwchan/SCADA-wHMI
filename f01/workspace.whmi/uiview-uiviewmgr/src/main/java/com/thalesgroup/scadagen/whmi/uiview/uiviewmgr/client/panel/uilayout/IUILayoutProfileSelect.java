package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout;

public class IUILayoutProfileSelect {
	public enum PropertiesName {
		  OpmApi("OpmApi")
		, DelayMillis("DelayMillis")
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
