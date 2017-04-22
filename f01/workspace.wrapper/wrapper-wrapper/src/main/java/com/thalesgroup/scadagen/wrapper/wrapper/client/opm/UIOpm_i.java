package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.Map;

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

	boolean checkAccess(Map<String, String> parameter);
	
	boolean checkHom(int hdvValue, String key);
	
	public interface CheckAccessWithHOMEvent_i {
		void result(boolean result);
	}
	
	void checkAccessWithHom(String function, String location, String action, String mode, int hdvValue, String key, CheckAccessWithHOMEvent_i resultEvent);

	void checkAccessWithHom(String function, String location, String action, String mode, String scsEnvId, String alias, String key, CheckAccessWithHOMEvent_i resultEvent);
	
	void checkAccessWithHostName(String functionValue, String locationValue, String actionValue, String modeValue, String scsEnvId, String alias, CheckAccessWithHOMEvent_i resultEvent);
	
	void checkAccessWithProfileName(String functionValue, String locationValue, String actionValue, String modeValue, String scsEnvId, String alias, CheckAccessWithHOMEvent_i resultEvent);
	
	void changePassword(String operator, String oldPassword, String newPassword, UIWrapperRpcEvent_i uiWrapperRpcEvent_i);
	
	String getCurrentOperator();
	
	String getCurrentProfile();
	String[] getCurrentProfiles();
	void setCurrentProfile();
	
	boolean isHOMAction(String action);
	boolean isByPassValue(int value);
	
	int getConfigHOMMask(String key);
	
	String getCurrentHostName();
	String getCurrentIPAddress();
	
	interface GetCurrentHOMValueEvent_i {
		void update(final String dbaddress, final int value);
	}
	void getCurrentHOMValue(final String scsEnvId, final String alias, final GetCurrentHOMValueEvent_i event);
	
//	void createOperator(String operator);
//	void removeOperator(String operator);
//	
//	void addProfileToOperator(String operator, String profile);
//	void removeProfileFromOperatior(String operator, String profile);
	
	void login(String operator, String password);
	void logout();

	

	
}
