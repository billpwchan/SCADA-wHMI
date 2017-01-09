package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIOpmSCS implements UIOpm_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIOpmSCS.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCS();
		return instance;
	}
	private UIOpmSCS () {}

	// mode : always using mode 1
	
	// action : 
	// M monitor
	// A ack
	// C control
	// D display alarm
	
	// function : integer equal to system cat
	
	// location : integer equal to location cat
	public boolean checkAccess(String function, String location, String action, String mode) {
		logger.begin(className, function);
		
		logger.info(className, function, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{function, location, mode, action});
		
		boolean result = false;
		
//		result = ClientSideRightsChecker.checkAccess(mode, action, function, location);

		return result;
	}
	@Override
	public boolean checkAccess(String opmName1, String opmValue1, String opmName2, String opmValue2, String opmName3,
			String opmValue3, String opmName4, String opmValue4) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void changePassword(String operator, String oldPassword, String newPassword, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getOperator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getProfile() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean login(String operator, String oldPassword) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean logout() {
		// TODO Auto-generated method stub
		return false;
	}

}
