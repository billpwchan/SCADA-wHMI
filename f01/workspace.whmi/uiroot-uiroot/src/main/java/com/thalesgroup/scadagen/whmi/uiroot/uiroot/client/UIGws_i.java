package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

public class UIGws_i {
	
	public enum Parameters {
		  uiDict("uiDict")
		, uiProp("uiProp")
		, uiJson("uiJson")
		, uiCtrl("uiCtrl")
		, uiView("uiView")
		, uiOpts("uiOpts")
		, uiElem("uiElem")
		;
		private final String text;
		private Parameters(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			Parameters[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum ParameterName {
		  UIOpmSCADAgenKey("UIOpmSCADAgenKey")
		, DatabaseReadingSingletonKey("DatabaseReadingSingletonKey")
		, DatabaseSubscribeSingletonKey("DatabaseSubscribeSingletonKey")
		, DatabaseSubscribeSingletonPeriodMillisKey("DatabaseSubscribeSingletonPeriodMillisKey")
		, DatabaseWritingSingletonKey("DatabaseWritingSingletonKey")

		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
