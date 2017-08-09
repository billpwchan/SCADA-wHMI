package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

/**
 * @author syau
 *
 * Interface for the UIControlPrioritySCADAgen for define the constant values
 */
public interface UIControlPrioritySCADAgen_i {
	
	/*
	 * Min and Max Level Value
	 */
	public final static int LEVEL_MIN 			= 0;
	public final static int LEVEL_MAX 			= 999;
	public final static int LEVEL_NOT_DEFINED 	= -1;
	
	/*
	 * UIOpm and UIDatabase API Name
	 */
	public final String UIOPM_NAME		= "UIOpmSCADAgen";
	public final String DB_WRITE_NAME	= "DatabaseWritingSingleton";
	public final String DB_READ_NAME  	= "DatabaseMultiReading";
	
	/*
	 * Configuration Folder Name File Name
	 */
	public final static String CACHE_NAME_DICTIONARYIES = "UIJson";
	public final static String FILE_NAME_ATTRIBUTE 		= "controlpriority.json";
	public final static String FILE_NAME_LEVEL 			= "controlprioritylevel.json";
	
	/**
	 * @author syau
	 *
	 *	Attribute name in the "controlprioritylevel.json" Configuration File
	 */
	public enum AttributeLevel {
		  Level("Level")
		, Key("Key")
		, Value("Value")
		;
		
		private final String text;
		private AttributeLevel(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	/**
	 * @author syau
	 *
	 *	Attribute name in the "controlpriority.json" Configuration File
	 */
	public enum Attribute {
		  UIOpmName("UIOpmName")
		, UsrIdentityType("UsrIdentityType")
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
	
	/**
	 * @author syau
	 *
	 *	Keywords for the Self Identity Attribute
	 */
	public enum UsrIdentity {
		  Operator("Operator")
		, Profile("Profile")
		;
		
		private final String text;
		private UsrIdentity(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	final static String RESRV_RESERVEREQID_DEFAULT_VALUE		= ".reservReserveReqID";
	final static String RESRV_UNRESERCEREQID_DEFAULT_VALUE		= ".resrvUnreserveReqID";
	final static String RESRV_RESERVEID_DEFAULT_VALUE			= ".resrvReserveID";
	
	
	/**
	 * @author syau
	 *
	 * Attribute name for the RTDB Reservation
	 */
	public enum DbAttribute {
		  ResrvReserveReqID("ResrvReserveReqID")
		, ResrvUnreserveReqID("ResrvUnreserveReqID")
		, ResrvReserveID("ResrvReserveID")
		;
		
		private final String text;
		private DbAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
