package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

/**
 * @author syau
 *
 *	Interface define the ControlPriority API
 */
public interface UIControlPriority_i {
	
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
	 * @param scsEnvId	Target Equipment ScsEnvId
	 * @param dbAddress	Target Equipment DbAddress
	 */
	void requestReservation(String scsEnvId, String dbAddress);
	
	/**
	 * Make a Withdraw Reservation Request on a Equipment
	 * 
	 * @param scsEnvId	Target Equipment ScsEnvId
	 * @param dbAddress	Target Equipment DbAddress
	 */
	void withdrawReservation(String scsEnvId, String dbAddress);
	
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
	 * 					3: AVAILABILITY_ALLOW
	 * 					4: AVAILABILITY_ALLOW_WITH_OVERRIDE
	 */
	void checkReservationAvailability(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack);
	
	/**
	 * Init the UIControlPriority instance
	 */
	void init();
}
