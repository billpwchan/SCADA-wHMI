package com.thalesgroup.scadagen.whmi.config.configenv.client;

public interface WebConfigMgrEvent {
	void failed();
	void updated(String value);
}
