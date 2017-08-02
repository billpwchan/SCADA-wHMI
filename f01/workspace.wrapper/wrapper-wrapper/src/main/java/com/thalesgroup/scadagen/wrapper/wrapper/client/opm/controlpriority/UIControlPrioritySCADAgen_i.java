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
	public final static int MIN_LEVEL 	= 0;
	public final static int MAX_LEVEL 	= 9999;
	
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
	
	/* 
	 * Get Reservation By Return Code
	 */
	public final static int AVAILABILITY_ERROR					= 0;
	public final static int AVAILABILITY_DENIED					= 1;
	public final static int AVAILABILITY_EQUAL					= 2;
	public final static int AVAILABILITY_ALLOW					= 3;
	public final static int AVAILABILITY_ALLOW_WITH_OVERRIDE	= 4;
	
	/*
	 * Get Reservation By Return Code Mapping in String Value
	 */
	public final static String STR_AVAILABILITY_ERROR					= "AVAILABILITY_ERROR";
	public final static String STR_AVAILABILITY_DENIED					= "AVAILABILITY_DENIED";
	public final static String STR_AVAILABILITY_EQUAL					= "AVAILABILITY_EQUAL";
	public final static String STR_AVAILABILITY_ALLOW					= "AVAILABILITY_ALLOW";
	public final static String STR_AVAILABILITY_ALLOW_WITH_OVERRIDE		= "AVAILABILITY_ALLOW_WITH_OVERRIDE";
	
	public final static String FIELD_VALUE = "value";
	
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
