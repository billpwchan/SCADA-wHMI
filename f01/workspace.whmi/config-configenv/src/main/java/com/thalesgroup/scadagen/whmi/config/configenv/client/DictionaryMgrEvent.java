package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public interface DictionaryMgrEvent {
	
	public void ready(Dictionary dictionary);
	public void failed(String xmlFile);
	
}
