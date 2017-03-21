package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;

public interface UIEventActionProcessor_i extends UIEventActionProcessorCore_i {
	
	final String init = "init";
	boolean executeActionSetInit();
	
	void executeActionSet(final String actionsetkey, final int delayMillis, final HashMap<String, HashMap<String, Object>> override);
	
}
