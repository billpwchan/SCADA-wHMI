package com.thalesgroup.rtt.beans;

import java.util.List;

//stores json contents of "response" from DbmComponent
public class SubscriptionResponse {
	private List<String> dbvalue;

	public List<String> getDbvalue() {
		return dbvalue;
	}
	public void setDbvalue(List<String> dbvalue) {
		this.dbvalue = dbvalue;
	}

}
