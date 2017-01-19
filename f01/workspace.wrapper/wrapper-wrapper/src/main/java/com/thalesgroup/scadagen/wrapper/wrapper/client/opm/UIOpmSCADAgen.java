package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

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
	
	@Override
	public boolean checkAccess(String opmName1, String opmValue1, String opmName2, String opmValue2, String opmName3, String opmValue3, String opmName4, String opmValue4) {
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
	
//	@Override
//	public boolean checkAccess(String opmName1, String opmValue1, String opmName2, String opmValue2, String opmName3, String opmValue3) {
//		String function = "checkAccess";
//		logger.begin(className, function);
//		logger.debug(className, function, "opmName1[{}] opmValue1[{}]", opmName1, opmValue1);
//		logger.debug(className, function, "opmName2[{}] opmValue2[{}]", opmName2, opmValue2);
//		logger.debug(className, function, "opmName3[{}] opmValue3[{}]", opmName3, opmValue3);
//
//		boolean result = false;
//		
//		result = checkAccess(
//				  opmName1, opmValue1
//				, opmName2, opmValue2
//				, opmName3, opmValue3
//				, MODE, DefaultModeValue);
//
//		return result;
//	}

//	@Override
//	public boolean checkAccess(String functionValue, String locationValue, String actionValue) {
//		String function = "checkAccess";
//		logger.begin(className, function);
//		logger.info(className, function, "functionValue[{}] locationValue[{}] actionValue[{}]", new Object[]{functionValue, locationValue, actionValue});
//		
//		boolean result = false;
//		
//		result = checkAccess(functionValue, locationValue, actionValue, DefaultModeValue);
//		
//		logger.end(className, function);
//		return result;
//	}
	@Override
	public boolean checkAccess(String functionValue, String locationValue, String actionValue, String modeValue) {
		logger.begin(className, functionValue);
		logger.info(className, functionValue, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{functionValue, locationValue, actionValue, modeValue});
		
		boolean result = false;
		
		result = checkAccess(
								  FUNCTION, functionValue
								, LOCATION, locationValue
								, ACTION, actionValue
								, MODE, modeValue
								);
		
		logger.end(className, functionValue);
		return result;
	}
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		logger.begin(className, function);
		
		logger.info(className, function, "operator[{}]", operator);

		new SpringChangePassword().changePassword(oldPass, newPass, uiWrapperRpcEvent_i); 
		
		logger.end(className, function);
		
	}
	@Override
	public String getOperator() {
		String function = "getOperator";
		logger.begin(className, function);
		String operatorId = null;
		operatorId = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		logger.info(className, function, "operatorId[{}]", operatorId);
		return operatorId;
	}
	@Override
	public String getProfile() {
		String function = "getProfile";
		logger.begin(className, function);
		String roleId = null;
		Map<String, RoleDto> roles = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getRoleId();
		if ( null != roles ) {
			Set<String> keys = roles.keySet();
			for ( String key : keys ) {
				logger.info(className, function, "key[{}]", key);
				roleId = roles.get(key).getId();
				logger.info(className, function, "roleId[{}]", roleId);
				break;
			}
		} else {
			logger.warn(className, function, "roleId IS NULL");
		}
		logger.info(className, function, "roleId[{}]", roleId);
		return roleId;
	}
	@Override
	public String getWorkstation() {
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
