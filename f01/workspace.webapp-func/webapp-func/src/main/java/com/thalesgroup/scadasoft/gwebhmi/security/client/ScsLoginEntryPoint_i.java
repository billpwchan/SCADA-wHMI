package com.thalesgroup.scadasoft.gwebhmi.security.client;

public interface ScsLoginEntryPoint_i {
	public enum FrameworkName {
		  SCADAgen("SCADAgen")
		, FAS("FAS")
		, COCC("COCC")
		, SCSTraining("SCSTraining")
		;
		private final String text;
		private FrameworkName(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			FrameworkName[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum PropertiesName {
		  project("project")
		  
		, framework("framework")
		, dictionary("dictionary")
		, property("property")
		, json("json")
		
		, uiCtrl("uiCtrl")
		, uiView("uiView")
		, uiOpts("uiOpts")
		, element("element")
		
		, disableDefaultContextMenu("disableDefaultContextMenu")
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
