package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

/**
 * @author syau
 *
 *	Interface define the ControlPriority API
 */
public interface UIControlPriority_i {
	
	/* 
	 * Check Reservation Availability Return Code
	 */
	public final static int AVAILABILITY_ERROR					= 0;
	public final static int AVAILABILITY_DENIED					= 1;
	public final static int AVAILABILITY_EQUAL					= 2;
	public final static int AVAILABILITY_RESERVED_BYSELF		= 3;
	public final static int AVAILABILITY_EMPTY					= 4;
	public final static int AVAILABILITY_ALLOW_WITH_OVERRIDE	= 5;
	
	/*
	 * Check Reservation Availability Return Code Mapping in String Value
	 */
	public final static String STR_AVAILABILITY_ERROR					= "AVAILABILITY_ERROR";
	public final static String STR_AVAILABILITY_DENIED					= "AVAILABILITY_DENIED";
	public final static String STR_AVAILABILITY_EQUAL					= "AVAILABILITY_EQUAL";
	public final static String STR_AVAILABILITY_RESERVED_BYSELF			= "AVAILABILITY_RESERVED_BYSELF";
	public final static String STR_AVAILABILITY_EMPTY					= "AVAILABILITY_EMPTY";
	public final static String STR_AVAILABILITY_ALLOW_WITH_OVERRIDE		= "AVAILABILITY_ALLOW_WITH_OVERRIDE";
	
	public final static String FIELD_VALUE = "value";
	
	/**
	 * @author syau
	 *
	 *	Interface for the Async Return value
	 */
	interface UIControlPriorityCallback {
		
		/**
		 * @param json JSON Format String
		 */
		void callBack(String json);
	}
	
	/**
	 * Make a Reservation Request on a Equipment
	 * 
	 * @param scsEnvId scsEnvId	Target Equipment ScsEnvId
	 * @param dbAddress dbAddress Target Equipment DbAddress
	 * @param callBack Return JSON String, JSONNumber Attribute "value" contain the requester
	 */
	void requestReservation(String scsEnvId, String dbAddress, final UIControlPriorityCallback callBack);
	
	/** Make a Withdraw Reservation Request on a Equipment
	 * @param scsEnvId Target Equipment ScsEnvId
	 * @param dbAddress Target Equipment DbAddress
	 * @param callBack Return JSON String, JSONNumber Attribute "value" contain the withdrawer
	 */
	void withdrawReservation(String scsEnvId, String dbAddress, final UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current Reservation By
	 * 
	 * @param scsEnvId	Target ScsEnvId
	 * @param dbAddress	Target DbAddress
	 * @param callback	Return JSON String, JSONString Attribute "value" contain the current reserved by
	 */
	void getCurrentReservationBy(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current Reservation Status
	 * 
	 * @param scsEnvId	Target Equipment ScsEnvId
	 * @param dbAddress	Target Equipment DbAddress
	 * @param callback	Return JSON String, JSONNumber Attribute "value" contain the current reserved status
	 * 					0: AVAILABILITY_ERROR
	 * 					1: AVAILABILITY_DENIED
	 * 					2: AVAILABILITY_EQUAL
	 * 					3: AVAILABILITY_RESERVED_BYSELF
	 * 					4: AVAILABILITY_ALLOW
	 * 					5: AVAILABILITY_ALLOW_WITH_OVERRIDE
	 */
	void checkReservationAvailability(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack);
	
	/**
	 * Init the UIControlPriority instance
	 */
	void init();
}
