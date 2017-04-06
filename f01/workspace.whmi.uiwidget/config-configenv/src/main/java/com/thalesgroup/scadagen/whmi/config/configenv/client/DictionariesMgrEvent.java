package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

public interface DictionariesMgrEvent {
	
	public void dictionariesMgrEventReady(Dictionary_i dictionary);
	public void dictionariesMgrEventFailed(String xmlFile);
	
}
