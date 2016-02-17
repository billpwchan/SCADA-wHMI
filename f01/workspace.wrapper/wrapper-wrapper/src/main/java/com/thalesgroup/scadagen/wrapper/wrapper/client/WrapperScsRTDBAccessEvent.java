package com.thalesgroup.scadagen.wrapper.wrapper.client;

public interface WrapperScsRTDBAccessEvent {
	void setReadResult(String key, String[] values, int errorCode, String errorMessage);
}
