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
	public boolean checkAccess(String function, String location, String action, String mode) {
		
		logger.begin(className, function);
		
		logger.info(className, function, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{function, location, action, mode});
		
		boolean result = false;
		
//		result = ClientSideRightsChecker.checkAccess(mode, action, function, location);
		
		if( mode != null && !mode.isEmpty() && action != null 
		&& function != null && !function.isEmpty() && location != null && !location.isEmpty() ) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter( MODE    , mode     );
			dto.addParameter( ACTION  , action   );
			dto.addParameter( FUNCTION, function );
			dto.addParameter( LOCATION, location );
		
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
		
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			result = checker.checkOperationIsPermitted( operatorOpmInfo, dto );
		} else {
			logger.debug( "checkAccess args null, or empty - mode=" + mode + 
				", action=" + action + ", function=" + function + ", location=" + location + " - checkAccess return 'false'" );
		}
		
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
