package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access;

import java.util.Map;

public interface UIAccess_i {

	boolean checkAccess(Map<String, String> parameter);

	boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4);
	
	boolean checkAccess(String functionValue, String locationValue, String actionValue, String modeValue);
	
	void init();
}
