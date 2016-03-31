package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public interface DictionaryMgrEvent {
	
	public void dictionaryMgrEventReady(Dictionary dictionary);
	public void dictionaryMgrEventFailed(String xmlFile);
	
}
