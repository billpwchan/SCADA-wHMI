package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user;

import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.SetCurrentProfileCallback_i;

public interface UIUser_i {
	
	void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i);
	
	String getCurrentOperator();
	
	String getCurrentProfile();
	
	String setCurrentProfile(final String profile);
	
	String[] getCurrentProfiles();
	
	void setCurrentProfile(final String role, final SetCurrentProfileCallback_i cb);

	void init();
}
