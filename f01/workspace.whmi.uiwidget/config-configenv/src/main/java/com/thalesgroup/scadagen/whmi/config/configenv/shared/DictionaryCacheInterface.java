package com.thalesgroup.scadagen.whmi.config.configenv.shared;

public class DictionaryCacheInterface {
	public static final String strConfigurationType	= "ConfigurationType";
	public enum ConfigurationType {
		XMLFile("XMLFile")
		, PropertiesFile("PropertyFile")
		;
		private final String text;
		private ConfigurationType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public static final String strContainerType		= "ContainerType";
	public enum ContainerType {
		Dictionary("Dictionary")
		, Dictionaries("Dictionaries")
		;
		private final String text;
		private ContainerType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum XMLAttribute {
		FileName("FileName")
		, Tag("Tag")
		, DateTime("DateTime")
		;
		private final String text;
		private XMLAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum PropertiesAttribute {
		FileName("FileName")
		, DateTime("DateTime")
		;
		private final String text;
		private PropertiesAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}

	public static final String strCreateDateTimeLabel		= "CreateDateTimeLabel";
	public static final String strDateTimeFormat = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String strFileName		= "FileName";
	
	public static final String Header 			= "header";
	public static final String Option 			= "option";    
	public static final String Key				= "key";
}
