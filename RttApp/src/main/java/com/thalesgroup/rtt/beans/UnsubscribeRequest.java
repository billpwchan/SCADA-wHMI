package com.thalesgroup.rtt.beans;

public class UnsubscribeRequest {
	
	private String callerId;
	
	private String hvId;
	
	private String env;
	
	public UnsubscribeRequest(){};
	
	public UnsubscribeRequest(String callerId, String hvId, String env) {
		this.callerId = callerId;
		this.hvId = hvId;
		this.env = env;
	}
	
	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getHvId() {
		return hvId;
	}

	public void setHvId(String hvId) {
		this.hvId = hvId;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

}