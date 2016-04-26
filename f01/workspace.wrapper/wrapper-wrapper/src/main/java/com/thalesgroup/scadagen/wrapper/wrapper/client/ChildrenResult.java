package com.thalesgroup.scadagen.wrapper.wrapper.client;

public interface ChildrenResult extends Result {
	void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage);
}
