package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import javax.servlet.http.HttpServletRequest;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;

public interface UIOpm_i {
	
	public static final String MODE = "mode";
	public static final String ACTION = "action";
	public static final String FUNCTION = "function";
	public static final String LOCATION = "location";
	
	boolean checkAccess(String function, String location, String action, String mode);
	
	boolean checkAccess(OperatorOpmInfo operatorOpmInfo, String function, String location, String action, String mode);
	
	boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4);

	boolean checkAccess(OperatorOpmInfo operatorOpmInfo
			, String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4);

	String getHostName();
	String getIPAddress();

	String getRemoteHostName(HttpServletRequest httpServletRequest);
	String getRemoteIPAddress(HttpServletRequest httpServletRequest);

}
