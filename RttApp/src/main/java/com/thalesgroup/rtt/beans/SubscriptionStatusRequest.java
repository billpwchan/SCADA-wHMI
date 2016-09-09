package com.thalesgroup.rtt.beans;

public class SubscriptionStatusRequest {
	
	private String callerId;
	
	private String env;
	
	private String subscriptionId;
	
	public SubscriptionStatusRequest() {};
	
	public String getSubscriptionId() {
		return subscriptionId;
	}

	public String getEnv() {
		return env;
	}

	public String getCallerId() {
		return callerId;
	}

}