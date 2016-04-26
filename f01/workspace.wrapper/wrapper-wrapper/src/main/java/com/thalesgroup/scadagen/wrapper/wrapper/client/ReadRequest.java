package com.thalesgroup.scadagen.wrapper.wrapper.client;

public class ReadRequest {
	String clientKey;
	String scsEnvId;
	String[] dbaddresses;
	String resultName;
	Result result;
	public ReadRequest(String clientKey) {
		this.clientKey = clientKey;
	}
	public ReadRequest(String clientKey, String scsEnvId, String[] dbaddresses, String resultName, Result readResult) {
		this.clientKey = clientKey;
		this.scsEnvId = scsEnvId;
		this.dbaddresses = dbaddresses;
		this.resultName = resultName;
		this.result = readResult;
	}
	public boolean equalToKey(ReadRequest readrequest) {
		if ( null != readrequest && equalToKey(readrequest.clientKey) ) {
			return true;
		}
		return false;
	}
	public boolean equalToKey(String clientKey) {
		if ( null != clientKey && 0 == this.clientKey.compareTo(clientKey) ) {
			return true;
		}
		return false;
	}
}