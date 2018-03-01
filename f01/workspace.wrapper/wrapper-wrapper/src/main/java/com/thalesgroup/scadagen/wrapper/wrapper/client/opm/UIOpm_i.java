package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.Map;

import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i;

/**
 * UIOpm_i is the interface of the WHMI OPM API, it define the Function Signature of each OPM function.
 * 
 * @author syau
 *
 */
public interface UIOpm_i {
	
	/**
	 * String value for the "MODE", using in the checkAccess API.
	 */
	public static final String MODE = "mode";
	
	/**
	 * String value for the "ACTION", using in the checkAccess API.
	 */
	public static final String ACTION = "action";
	
	/**
	 * String value for the "FUNCTION", using in the checkAccess API.
	 */
	public static final String FUNCTION = "function";
	
	/**
	 * String value for the "LOCATION", using in the checkAccess API.
	 */
	public static final String LOCATION = "location";

	/**
	 * Initial the UIOpmSCADAgen Object.
	 * <ui>
	 * <li>Reading Configuration(Database Connection Method…etc)
	 * <li>Access to Server to retrieve value(IPAddress, HostName…etc)
	 * </ui>
	 */
	void init();

	/**
	 * Verify the access right for the input parameters.
	 * 
	 * @param opmName1  Parameter 1 name for the opm rule checking.
	 * @param opmValue1 Parameter 1 value for the opm rule checking.
	 * @param opmName2  Parameter 2 name for the opm rule checking.
	 * @param opmValue2 Parameter 2 value for the opm rule checking.
	 * @param opmName3  Parameter 3 name for the opm rule checking.
	 * @param opmValue3 Parameter 3 value for the opm rule checking.
	 * @param opmName4  Parameter 4 name for the opm rule checking.
	 * @param opmValue4 Parameter 4 value for the opm rule checking.
	 * @return          Return value from the MWT checkOperationIsPermitted.
	 */
	boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4
			);
	
	/**
	 * Verify the access right for the input parameter.
	 * 
	 * @param function Function value for the opm rule checking.
	 * @param location Location value for the opm rule checking.
	 * @param action   Action value for the opm rule checking.
	 * @param mode     Mode value for the opm rule checking.
	 * @return         Return value from the MWT checkOperationIsPermitted.
	 */
	boolean checkAccess(String function, String location, String action, String mode);

	/**
	 * Verify the access right for the input parameter(s).
	 * 
	 * @param parameter Key and Value in pair.
	 * @return          Return value from the MWT checkOperationIsPermitted.
	 */
	boolean checkAccess(Map<String, String> parameter);
	
	/**
	 * Verify the access right for the input parameter with HOM., using ProfileName as a key for "HOMLevels".
	 * <p>
	 * Check access right with HOM, According configuration in in "hom.json" , 
	 * Reading hdv value from RTDB (By HVID with "DBAttribute"), make a AND operation with predefined value "HOMLevels".
	 * 
	 * @param hdvValue Currently HOM value for checking.
	 * @param identity Key of the HOM Configuration.
	 * @return Return  Value of the hdvValue & HOMMask result.
	 */
	boolean checkHom(final int hdv, final String identity);
	
	/**
	 * Interface for the CheckAccessWithHOM Callback
	 * 
	 * @author syau
	 *
	 */
	public interface CheckAccessWithHOMEvent_i {
		/**
		 * Return the CheckAccessWithHOM result
		 * 
		 * @param result Return ture if the CheckAccessWithHOM is valid, otherwise return false
		 */
		void result(boolean result);
	}
	
	/**
	 * Verify the access right for the input parameter with HOM.
	 * Check access right with HOM, According configuration in in "hom.json" and "homLevel.json", 
	 * HOMIdentity defined in HOMIdentityType.
	 * Reading hdv value from RTDB (By HVID with "DBAttribute"), make a AND operation with predefined value "HOMLevels".
	 * 
	 * @param function    Function value for the opm rule checking.
	 * @param location    Location value for the opm rule checking.
	 * @param action      Action value for the opm rule checking.
	 * @param mode        Mode value for the opm rule checking.
	 * @param scsEnvId    RTDB ScsEnvId to read the hdvValue.
	 * @param dbAddress       RTDB Alias to read the hdvValue.
	 * @param resultEvent Asynchronous callback for the check result.
	 */
	void checkAccessWithHom(String function, String location, String action, String mode,
			String scsEnvId, String dbAddress, CheckAccessWithHOMEvent_i resultEvent);
	
	/**
	 * Verify the access right for the input parameter with HOM, HHV Value and "HOMLevels" key is a parameters.
	 * Check access right with HOM, According configuration in in "hom.json", 
	 * Reading hdv value from RTDB (By HVID with "DBAttribute"), make a AND operation with predefined value "HOMLevels".
	 * 
	 * @param function    Function value for the opm rule checking.
	 * @param location    Location value for the opm rule checking.
	 * @param action      Action value for the opm rule checking.
	 * @param mode        Mode value for the opm rule checking.
	 * @param hdv         hdvValue for the AND Operation.
	 * @param identity         Key for the HOMLevels.
	 * @param resultEvent Asynchronous callback for the check result.
	 */
	void checkAccessWithHom(String function, String location, String action, String mode, int hdv, String identity, CheckAccessWithHOMEvent_i resultEvent);

	/**
	 * Change the operator password, oldPassword must be equal to operator current password.
	 * 
	 * @param operator            Operator name to change password.
	 * @param oldPassword         Old password, Authentication purpose.
	 * @param newPassword         New password to apply.
	 * @param uiWrapperRpcEvent_i Return the JSON data structure to description action result.
	 */
	void changePassword(String operator, String oldPassword, String newPassword, UIWrapperRpcEvent_i uiWrapperRpcEvent_i);
	
	/**
	 * Return current session operator. Reset in initial call.
	 * 
	 * @return Current session operator.
	 */
	String getCurrentOperator();
	
	/**
	 * Return current session first profile. Reset in initial call.
	 * 
	 * @return Current Profile name.
	 */
	String getCurrentProfile();
	
	/**
	 * Return current session profile(s). Reset in initial call.
	 * 
	 * @return Profile list.
	 */
	String[] getCurrentProfiles();

	/**
	 * Get the current client Host Name
	 * 
	 * @return Current Host Name
	 */
	String getCurrentHostName();
	
	/**
	 * Get the current client IP Address
	 * 
	 * @return Current Client IP Address
	 */
	String getCurrentIPAddress();
	
	/**
	 * Get the current client IP Address
	 * 
	 * @param cb    Callback for the Async result:  Current Client IP Address
	 */
	void getCurrentIPAddress(GetCurrentIpAddressCallback_i cb);
	
	
	/**
	 * Get the current client Env Address
	 * 
	 * @return Current Client Env Address
	 */
	String getCurrentEnv();
	
	/**
	 * Set the current client Env Address
	 * 
	 * @param env
	 */
	void setCurrentEnv(String env);
	
	/**
	 * Get the current client Alias Address
	 * 
	 * @return Current Client Alias Address
	 */
	String getCurrentAlias();
	
	/**
	 * Set the current client Alias Address
	 * 
	 * @param alias
	 */
	void setCurrentAlias(String alias);

	/**
	 * Check the action value is HOM action
	 * 
	 * @param action Action key to check
	 * @return Return ture if the action is HOM Action, otherwise return false
	 */
	boolean isHOMAction(String action);
	
	/**
	 * Check the action value is By Pass Value
	 * @param value
	 * @return Return true if the action is by pass value, otherwise return false
	 */
	boolean isBypassValue(int value);
	
	/**
	 * @author syau
	 *
	 */
	interface GetCurrentHOMValueEvent_i {
		void update(final String dbaddress, final int value);
	}
	/**
	 * Get Current HOM value from the RTDB
	 * 
	 * @param scsEnvId  RTDB ScsEnvId to read the hdv.
	 * @param dbAddress RTDB Alias to read the hdv.
	 * @param event     Return the Integer value
	 */
	void getCurrentHOMValue(final String scsEnvId, final String dbAddress, final GetCurrentHOMValueEvent_i event);

	/**
	 * Log-in into spring framework, Authentication bean setting in the applicationContext-security.xml configuration file. 
	 * 
	 * @param operator Operator name for authentication.
	 * @param password Password for authentication 
	 */
	void login(String operator, String password);
	
	/**
	 * Log-out the current spring framework session, bean setting in the applicationContext-security.xml configuration file.
	 */
	void logout();

}
