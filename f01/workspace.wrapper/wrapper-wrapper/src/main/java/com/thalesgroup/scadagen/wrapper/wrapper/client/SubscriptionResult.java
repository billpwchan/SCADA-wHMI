package com.thalesgroup.scadagen.wrapper.wrapper.client;

public interface SubscriptionResult extends Result {
	void setReadResultSubscription(String key, String[][] values, int errorCode, String errorMessage);
}
