package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;

public interface ConfigMgrEvent {
	
	public void ready(Configs configs);
	public void failed(String xmlFile);
	
}
