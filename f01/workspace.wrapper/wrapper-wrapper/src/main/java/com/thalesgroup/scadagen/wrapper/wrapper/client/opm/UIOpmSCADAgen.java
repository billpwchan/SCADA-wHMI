package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.AuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.IAuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIOpmSCADAgen implements UIOpm_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIOpmSCADAgen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}
	
	private String hostName = null;
	private String ipAddress = null;
	private String [] profileNames = null;
	@Override
	public void init() {
		//hostName
		//ipAddress
	}
	
	@Override
	public boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4) {
		String function = "checkAccess";
		logger.begin(className, function);
		
		logger.debug(className, function, "opmName1[{}] opmValue1[{}]", opmName1, opmValue1);
		logger.debug(className, function, "opmName2[{}] opmValue2[{}]", opmName2, opmValue2);
		logger.debug(className, function, "opmName3[{}] opmValue3[{}]", opmName3, opmValue3);
		logger.debug(className, function, "opmName4[{}] opmValue4[{}]", opmName4, opmValue4);
		
		boolean result = false;
		
		if ( 
				   opmName1 != null && ! opmName1.isEmpty() 
				&& opmValue1 != null && !opmValue1.isEmpty()
				
				&& opmName2 != null && !opmName2.isEmpty()
				&& opmValue2 != null && !opmValue2.isEmpty()
				
				&& opmName3 != null && !opmName3.isEmpty()
				&& opmValue3 != null && !opmValue3.isEmpty()
				
				&& opmName4 != null && !opmName4.isEmpty()
				&& opmValue4 != null && !opmValue4.isEmpty()
		) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter( opmName1, opmValue1 );
			dto.addParameter( opmName2, opmValue2 );
			dto.addParameter( opmName3, opmValue3 );
			dto.addParameter( opmName4, opmValue4 );
		
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
			
			// TO: remove the non target role in here
		
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			result = checker.checkOperationIsPermitted( operatorOpmInfo, dto );
		} else {
			logger.debug( "checkAccess args null, or empty - " 
				+ "  "+opmName1+"=" + opmValue1 
				+ ", "+opmName2+"=" + opmValue2 
				+ ", "+opmName3+"=" + opmValue3 
				+ ", "+opmName4+"=" + opmValue4
				+ " - checkAccess return 'false'" );
		}
		
		return result;
	}
	
	@Override
	public boolean checkAccess(String functionValue, String locationValue, String actionValue, String modeValue) {
		final String function = "function";
		logger.begin(className, function);
		logger.info(className, function, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{functionValue, locationValue, actionValue, modeValue});
		
		boolean result = false;
		
		result = checkAccess(
								  FUNCTION, functionValue
								, LOCATION, locationValue
								, ACTION, actionValue
								, MODE, modeValue
							);
		
		logger.end(className, function);
		return result;
	}

	@Override
	public boolean checkAccessWithHom(String functionValue, String locationValue, String actionValue, String modeValue, String key) {
		final String function = "checkAccess";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}] key[{}]", new Object[]{function, locationValue, actionValue, modeValue, key});
		
		boolean result = false;
		
		result = checkAccess(
				  functionValue
				, locationValue
				, actionValue
				, modeValue
				);
		
		logger.end(className, function);
		return result;
	}
	
	@Override
	public boolean checkAccessWithHostName(String functionValue, String locationValue, String actionValue, String modeValue) {
		final String function = "checkAccessWithHostName";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}]", new Object[]{function, locationValue, actionValue, modeValue});
		
		boolean result = false;

		result = checkAccessWithHom(
				  functionValue
				, locationValue
				, actionValue
				, modeValue
				, getCurrentHostName()
				);
		
		logger.end(className, function);
		return result;
	}
	
	@Override
	public boolean checkAccessWithProfileName(String functionValue, String locationValue, String actionValue, String modeValue) {
		final String function = "checkAccessWithProfileName";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}]", new Object[]{function, locationValue, actionValue, modeValue});
		
		boolean result = false;

		result = checkAccessWithHom(
				  functionValue
				, locationValue
				, actionValue
				, modeValue
				, getCurrentProfile()
				);
		
		logger.end(className, function);
		return result;
	}
	
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		logger.begin(className, function);
		
		logger.debug(className, function, "operator[{}]", operator);

		new SpringChangePassword().changePassword(operator, oldPass, newPass, uiWrapperRpcEvent_i); 
		
		logger.end(className, function);
		
	}
	@Override
	public String getCurrentOperator() {
		String function = "getCurrentOperator";
		logger.begin(className, function);
		String operatorId = null;
		operatorId = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		logger.debug(className, function, "operatorId[{}]", operatorId);
		return operatorId;
	}
	@Override
	public String getCurrentProfile() {
		String function = "getCurrentProfile";
		logger.begin(className, function);
		String profile = null;
		if ( null == profileNames ) {
			getCurrentProfiles();
		}
		if ( null != profileNames ) {
			if ( profileNames.length > 0 ) {
				profile = profileNames[0];
			}
		}
		logger.debug(className, function, "profile[{}]", profile);
		logger.end(className, function);
		return profile;
	}
	
	@Override
	public String[] getCurrentProfiles() {
		String function = "getCurrentProfiles";
		logger.begin(className, function);
		
		if ( null == profileNames ) {
			List<String> roleIds = null;
			Map<String, RoleDto> roles = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getRoleId();
			if ( null != roles ) {
				Set<String> keys = roles.keySet();
				for ( String key : keys ) {
					logger.debug(className, function, "key[{}]", key);
					
					String roleId = roles.get(key).getId();
					logger.debug(className, function, "roleId[{}]", roleId);
					
					if ( null == roleIds ) roleIds = new LinkedList<String>();
					
					roleIds.add(roleId);
				}
			} else {
				logger.warn(className, function, "roleId IS NULL");
			}
			
			profileNames = roleIds.toArray(new String[0]);
		}
		
		logger.debug(className, function, "profileNames[{}]", profileNames);

		logger.end(className, function);
		return profileNames;
	}
	
	@Override
	public String getCurrentHostName() {
		String function = "getCurrentHostName";
		logger.begin(className, function);
		hostName = ConfigProvider.getInstance().getClientData().getClientWorkstationProperties().getIpAddress();
		logger.debug(className, function, "hostName[{}]", hostName);
		logger.end(className, function);
		return hostName;
	}
	@Override
	public String getCurrentIPAddress() {
		String function = "getCurrentIPAddress";
		logger.begin(className, function);
		ipAddress = ConfigProvider.getInstance().getClientData().getClientWorkstationProperties().getIpAddress();
		logger.debug(className, "getCurrentIPAddress", "ipAddress[{}]", ipAddress);
		logger.end(className, function);
		return ipAddress;
	}
	@Override
	public String getCurrentHOMValue(String hvid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getConfigHOMMask(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean createOperator(String operator) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeOperator(String operator) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean addOperatorProfile(String operator, String profile) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean login(String operator, String password) {
		String function = "login";
		logger.begin(className, function);
		String SPRING_SEC_CHECK_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		new SpringLogin(SPRING_SEC_CHECK_URL, user_name, pass_name).login(operator, password);
		
		logger.end(className, function);
		return true;
	}
	@Override
	public boolean logout() {
		String function = "logout";
		logger.begin(className, function);
		
		String SPRING_SEC_LOGOUT_URL = "j_spring_security_logout";
		
		new SpringLogout(SPRING_SEC_LOGOUT_URL).logout();
		
		logger.end(className, function);
		return true;
	}

	
}
