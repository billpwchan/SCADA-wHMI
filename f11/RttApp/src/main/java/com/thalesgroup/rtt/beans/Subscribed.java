package com.thalesgroup.rtt.beans;

import java.util.List;
import java.util.Set;

public class Subscribed {
	private List<SubscriptionRequest> allSubReqs;
//	private Set<String> allArchives;
	
	public List<SubscriptionRequest> getAllSubReqs() {
		return allSubReqs;
	}
	public void setAllSubReqs(List<SubscriptionRequest> allSubReqs) {
		this.allSubReqs = allSubReqs;
	}
}
