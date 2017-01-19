package com.thalesgroup.scadagen.wrapper.wrapper.client.poller;

import com.thalesgroup.scadagen.wrapper.wrapper.client.Result;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.NotifyResult_i;

public class SubscribeResult implements Result, NotifyResult_i {
	private String key_ = null;
	private String subUUID_ = null;
	private String pollerState_ = null;
	private String [] dbaddresses_ = null;
	private String [] values_ = null;
	private int errorCode_ = 0;
	private String errorMessage_ = null;
	
	
	public String getKey() { return key_; }
	public String getSubUUID() { return subUUID_; }
	public String getPollerState() { return pollerState_; }
	public String [] getDbAddresses() { return dbaddresses_; }
	public String [] getValues() { return values_; }
	public int getErrorCode() { return errorCode_; }
	public String getErrorMessage() { return errorMessage_; }
	
	public void setSubscribeResult(String key, String subUUID, String pollerState, String[] dbaddresses,
			String[] values, int errorCode, String errorMessage) {
		key_ = key;
		subUUID_ = subUUID;
		pollerState_ = pollerState;
		dbaddresses_ = dbaddresses;
		values_ = values;
		errorCode_ = errorCode;
		errorMessage_ = errorMessage;
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void notifyError() {
		
	}
}
