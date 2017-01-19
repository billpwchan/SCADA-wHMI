package com.thalesgroup.scadagen.wrapper.wrapper.client;

public interface MultiReadResult {
	void setReadResult(String key, String[] value, int errorCode, String errorMessage);
}
