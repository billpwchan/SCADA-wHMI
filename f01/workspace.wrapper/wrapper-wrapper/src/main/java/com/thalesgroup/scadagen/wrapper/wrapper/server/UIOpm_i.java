package com.thalesgroup.scadagen.wrapper.wrapper.server;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;

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

}
