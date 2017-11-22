package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common;

import java.util.Map;

public interface InitProcess_i {
	
	void process(final Map<String, Object> params, final InitReady_i initReady);
}
