package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

public interface UIOpm_i {
	
	public static final String MODE = "mode";
	public static final String ACTION = "action";
	public static final String FUNCTION = "function";
	public static final String LOCATION = "location";

	void init();

	boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4
			);
	
	boolean checkAccess(String function, String location, String action, String mode);

	boolean checkAccessWithHom(String function, String location, String action, String mode, String key);
	
	boolean checkAccessWithHostName(String functionValue, String locationValue, String actionValue, String modeValue);
	
	boolean checkAccessWithProfileName(String functionValue, String locationValue, String actionValue, String modeValue);
	
	void changePassword(String operator, String oldPassword, String newPassword, UIWrapperRpcEvent_i uiWrapperRpcEvent_i);
	
	String getCurrentOperator();
	String getCurrentProfile();
	String[] getCurrentProfiles();
	
	String getCurrentHostName();
	String getCurrentIPAddress();
	int getCurrentHOMValue(String hvid);
	int getConfigHOMMask(String key);
	
	boolean createOperator(String operator);
	boolean removeOperator(String operator);
	boolean addOperatorProfile(String operator, String profile);
	
	boolean login(String operator, String password);
	boolean logout();

	
}
