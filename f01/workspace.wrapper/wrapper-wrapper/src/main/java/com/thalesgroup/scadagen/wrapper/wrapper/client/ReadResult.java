package com.thalesgroup.scadagen.wrapper.wrapper.client;

public interface ReadResult extends Result {
	 void setReadResult(String clientKey, String[][] values, int errorCode, String errorMessage);
}
