package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

/**
 * @author syau
 *
 *	Interface define the ControlPriority API
 */
public interface UIControlPriority_i {

	/**
	 * JSON "value" attribute field name
	 */
	public final static String FIELD_VALUE	= "value";
	/**
	 * JSON "code" attribute field name
	 */
	public final static String FIELD_CODE	= "code";
	
	/* 
	 * Check Reservation LEVEL Return Code
	 */
	public final static int LEVEL_ERROR					= 0; // Availability checking return error
	public final static int LEVEL_HIGHER				= 1; // Compare Level with higher level 
	public final static int LEVEL_EQUAL					= 2; // Compare Level with same level
	public final static int LEVEL_IS_ITSELF				= 3; // Compare Level with itself level
	public final static int LEVEL_EMPTY					= 4; // Compare Level with empty level
	public final static int LEVEL_LOWER					= 5; // Compare Level with lower level
	
	/* 
	 * Check Reservation Availability Return Code
	 */
	public final static int AVAILABILITY_ERROR					= 0; // Availability checking return error
	public final static int AVAILABILITY_DENIED					= 1; // Reserved by other with higher level 
	public final static int AVAILABILITY_EQUAL					= 2; // Reserved by other at same level
	public final static int AVAILABILITY_RESERVED_BYSELF		= 3; // Reserved by self
	public final static int AVAILABILITY_EMPTY					= 4; // Non-reserved status
	public final static int AVAILABILITY_ALLOW_WITH_OVERRIDE	= 5; // Reserved by other
	/*
	 * Check Reservation Availability Return Code Mapping in String Value
	 */
	public final static String STR_AVAILABILITY_ERROR					= "AVAILABILITY_ERROR";
	public final static String STR_AVAILABILITY_DENIED					= "AVAILABILITY_DENIED";
	public final static String STR_AVAILABILITY_EQUAL					= "AVAILABILITY_EQUAL";
	public final static String STR_AVAILABILITY_RESERVED_BYSELF			= "AVAILABILITY_RESERVED_BYSELF";
	public final static String STR_AVAILABILITY_EMPTY					= "AVAILABILITY_EMPTY";
	public final static String STR_AVAILABILITY_ALLOW_WITH_OVERRIDE		= "AVAILABILITY_ALLOW_WITH_OVERRIDE";
	
	/*
	 * Check Request Reservation Return Code Mapping in String Value
	 */
	public final static int REQUEST_ERROR_UNKNOW							= 0;
	public final static int REQUEST_ERROR_CHECKING							= 1;
	public final static int REQUEST_REQUESTED								= 2;
	public final static int REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW			= 2;
	public final static int REQUEST_REJECTED_AT_LOWER_LEVEL					= 3;
	public final static int REQUEST_REJECTED_AT_SAME_LEVEL					= 4;
	/*
	 * Check Request Reservation Return Code Mapping in String Value
	 */
	public final static String STR_REQUEST_ERROR_UNKNOW						= "REQUEST_ERROR_UNKNOW";
	public final static String STR_REQUEST_ERROR_CHECKING					= "REQUEST_ERROR_CHECKING";
	public final static String STR_REQUEST_REQUESTED						= "REQUEST_REQUESTED";
	public final static String STR_REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW		= "REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW";
	public final static String STR_REQUEST_REJECTED_AT_LOWER_LEVEL			= "REQUEST_REJECTED_AT_LOWER_LEVEL";
	public final static String STR_REQUEST_REJECTED_AT_SAME_LEVEL			= "REQUEST_REJECTED_AT_SAME_LEVEL";
	
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static int WITHDRAW_REQUESTED								= 0;
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static String STR_WITHDRAW_REQUESTED						= "WITHDRAW_REQUESTED";
	
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static int GETCURRENT_READ_INVALID							= 0;
	public final static int GETCURRENT_VALID								= 1;
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static String STR_GETCURRENT_READ_INVALID					= "GETCURRENT_READ_INVALID";
	public final static String STR_GETCURRENT_VALID							= "GETCURRENT_VALID";
	
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static int FORCE_WITHDRAW_INVALID							= 0;
	public final static int FORCE_WITHDRAW_VALID							= 1;
	/*
	 * Check Withdraw Request Return Code Mapping in String Value
	 */
	public final static String STR_FORCE_WITHDRAW_INVALID					= "FORCE_WITHDRAW_INVALID";
	public final static String STR_FORCE_WITHDRAW_VALID						= "FORCE_WITHDRAW_VALID";
	
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
	 * @param callBack Return JSON String, JSONNumber attribute "value" contain the requester, "code" contain the return code
	 */
	void requestReservation(String scsEnvId, String dbAddress, final UIControlPriorityCallback callBack);
	
	/** Make a Withdraw Reservation Request on a Equipment
	 * @param scsEnvId Target Equipment ScsEnvId
	 * @param dbAddress Target Equipment DbAddress
	 * @param callBack Return JSON String, JSONNumber attributes "value" contain the withdrawer, "code" contain the return code
	 */
	void withdrawReservation(String scsEnvId, String dbAddress, final UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current Reservation By
	 * 
	 * @param scsEnvId	Target ScsEnvId
	 * @param dbAddress	Target DbAddress
	 * @param callback	Return JSON String, JSONString Attribute "value" contain the current reserved by, "code" contain the return code
	 */
	void getCurrentReservationBy(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current Reservation Level
	 * 
	 * @param identity	Target identity to compare
	 * @param callback	Return JSON String, JSONNumber Attribute "value" contain the current reserved status, ref to AVAILABILITY_* code.
	 */
	void checkReservationLevel(final String identity, final UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current Reservation Status
	 * 
	 * @param scsEnvId	Target Equipment ScsEnvId
	 * @param dbAddress	Target Equipment DbAddress
	 * @param callback	Return JSON String, JSONNumber Attribute "value" contain the current reserved status, ref to AVAILABILITY_* code.
	 */
	void checkReservationAvailability(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack);
	
	/**
	 * Get the Current User Identity
	 * 
	 * @return Current User Identity
	 */
	String getUsrIdentity();
	
	/**
	 * Init the UIControlPriority instance
	 */
	void init();
}
