package com.thalesgroup.scadagen.wrapper.wrapper.shared.opm;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.action.ChangePasswordAction;

@SuppressWarnings("serial")
public class SCADAgenChangePasswordAction extends ChangePasswordAction {

	private String userId = null;
	
    /**
     * Constructor
     */
    public SCADAgenChangePasswordAction() {
        super();
    }
	
    public String getUserId() {	return this.userId; }
    public void setUserId(String userId) { this.userId = userId;  }
}
