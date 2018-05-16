package com.thalesgroup.scadagen.whmi.config.configenv.client.logger;

public interface LoggerConfigLoader_i {

	public final static String STR_SPLITER				= "\\|";
	public final static String STR_COMMA				= ",";
	
	public final static String CACHE_NAME_DICTIONARYIES = "UIJson";
	public final static String FILE_NAME_ATTRIBUTE 		= "uilogger.json";
	
	/**
	 * @author t0096643
	 *
	 *	Attribute name in the "uilogger.json" Configuration File
	 */
	public enum Attribute {
		loggercorename("loggercorename")
		, loggerlevel("loggerlevel")
		, loggername("loggername")
		, loggercategory("loggercategory")
		, loggerisfullclassname("loggerisfullclassname")
		, loggercurrentlevel("loggercurrentlevel")
		, loggerfilter("loggerfilter")
		;
		
		private final String text;
		private Attribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
