package com.thalesgroup.scadagen.wrapper.wrapper.client.hilc;

public interface HILCMgr_i {
	
	public void HILCPreparationResult(String clientKey, int errorCode, String errorMessage);
	
	public void HILCConfirmResult(String clientKey, int errorCode, String errorMessage);
}
