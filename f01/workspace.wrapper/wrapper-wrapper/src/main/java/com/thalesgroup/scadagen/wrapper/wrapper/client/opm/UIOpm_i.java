package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

public interface UIOpm_i {
	
	public static final String MODE = "mode";
	public static final String ACTION = "action";
	public static final String FUNCTION = "function";
	public static final String LOCATION = "location";
	
	public static final String M = "M";
	public static final String D = "D";
	public static final String A = "A";
	public static final String C = "C";
	
	boolean checkAccess(String function, String location, String action, String mode);
	
	void changePassword(String operator, String oldPassword, String newPassword, UIWrapperRpcEvent_i uiWrapperRpcEvent_i);
	
	boolean createOperator(String operator);
	boolean removeOperator(String operator);
	boolean addOperatorProfile(String operator, String profile);
	
	boolean login(String operator, String oldPassword);
	boolean logout();
}
