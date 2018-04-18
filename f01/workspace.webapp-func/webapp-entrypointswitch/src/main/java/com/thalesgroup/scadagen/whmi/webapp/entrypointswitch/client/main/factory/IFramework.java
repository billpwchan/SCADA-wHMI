package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.IEntryPoint;

public interface IFramework {
	
	public final String XML_MODE		= ConfigurationType.XMLFile.toString();
	public final String XML_MODULE		= null;
	public final String XML_FOLDER		= IEntryPoint.STR_XML_FOLDER;
	public final String XML_EXTENSION	= ".xml";
	public final String XML_TAG			= "header";
	
	public enum FrameworkName {
		  SCADAgen				("SCADAgen")
		, SCADAgenStandalone	("SCADAgenStandalone")
		, FAS					("FAS")
		, COCC					("COCC")
		, SCSTraining			("SCSTraining")
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
			final FrameworkName[] enums = values();
			final String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum PropertiesName {
		  uiDict("uiDict")
		, uiProp("uiProp")
		, uiJson("uiJson")
		, uiCtrl("uiCtrl")
		, uiView("uiView")
		, uiOpts("uiOpts")
		, uiElem("uiElem")
		
		, uiMenu("uiMenu")
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
			final PropertiesName[] enums = values();
			final String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	void launch(Map<String, Object> params);
}
