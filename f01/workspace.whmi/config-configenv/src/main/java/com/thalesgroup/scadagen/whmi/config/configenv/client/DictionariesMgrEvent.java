package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public interface DictionariesMgrEvent {
	
	public void dictionariesMgrEventReady(Dictionary dictionary);
	public void dictionariesMgrEventFailed(String xmlFile);
	
}
