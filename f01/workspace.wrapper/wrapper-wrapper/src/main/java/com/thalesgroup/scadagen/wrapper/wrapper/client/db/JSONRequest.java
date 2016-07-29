package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

class JSONRequest {
	String api;
	String clientKey;
	String scsEnvId;
	String dbaddress;
	String[] dbaddresses;
	public JSONRequest (String api, String clientKey, String scsEnvId, String dbaddress) {
		this.api = api;
		this.clientKey = clientKey;
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;
	}
	public JSONRequest (String api, String clientKey, String scsEnvId, String[] dbaddresses) {
		this.api = api;
		this.clientKey = clientKey;
		this.scsEnvId = scsEnvId;
		this.dbaddresses = dbaddresses;
	}
}
