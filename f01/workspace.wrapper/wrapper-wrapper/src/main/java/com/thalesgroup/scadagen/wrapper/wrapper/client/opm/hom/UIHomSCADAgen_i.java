package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom;

public interface UIHomSCADAgen_i {
	/*
	 * UIOpm and UIDatabase API Name
	 */
	public final String UIOPM_NAME		= "UIOpmSCADAgen";
	public final String DB_READ_NAME  	= "DatabaseMultiReading";
	
	/*
	 * Configuration Folder Name File Name
	 */
	public final static String CACHE_NAME_DICTIONARYIES = "UIJson";
	public final static String FILE_NAME_ATTRIBUTE 		= "hom.json";
	public final static String FILE_NAME_LEVEL 			= "homlevel.json";
	
	/**
	 * @author syau
	 *
	 *	Attribute name in the "controlpriority.json" Configuration File
	 */
	public enum Attribute {
		   UIOpmName("UIOpmName")
		,  DatabaseMultiReadKey("DatabaseMultiReadKey")
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
