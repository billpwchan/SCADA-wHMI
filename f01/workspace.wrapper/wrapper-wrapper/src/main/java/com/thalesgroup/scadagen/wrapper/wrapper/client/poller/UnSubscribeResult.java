package com.thalesgroup.scadagen.wrapper.wrapper.client.poller;

import com.thalesgroup.scadagen.wrapper.wrapper.client.Result;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.NotifyResult_i;

public class UnSubscribeResult implements Result, NotifyResult_i {
	private String key_ = null;
	private int errorCode_ = 0;
	private String errorMessage_ = null;
	
	public void setUnSubscribeResult(String key, int errorCode, String errorMessage) {
		key_ = key;
		errorCode_ = errorCode;
		errorMessage_ = errorMessage;
	}
	
	public String getKey() { return key_; }
	public int getErrorCode() { return errorCode_; }
	public String getErrorMessage() { return errorMessage_; }
	

	@Override
	public void update() {

	}

	@Override
	public void notifyError() {
		
	}
}
