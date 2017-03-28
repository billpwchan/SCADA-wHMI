package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Map;

public interface WebConfigMgrEvent {
	void failed();
	void updated(Map<String, String> value);
}
