package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;

public interface UIEventActionProcessor_i extends UIEventActionProcessorCore_i {
	
	boolean executeActionSetInit();
	
	void executeActionSetInit(int delayMillis, HashMap<String, HashMap<String, Object>> override);
	void executeActionSetInit(int delayMillis, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);

	boolean executeActionSetEnvUp();
	
	void executeActionSetEnvUp(int delayMillis, HashMap<String, HashMap<String, Object>> override);
	void executeActionSetEnvUp(int delayMillis, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeActionSetEnvDown();
	
	void executeActionSetEnvDown(int delayMillis, HashMap<String, HashMap<String, Object>> override);
	void executeActionSetEnvDown(int delayMillis, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeActionSetTerminate();
	
	void executeActionSetTerminate(int delayMillis, HashMap<String, HashMap<String, Object>> override);
	void executeActionSetTerminate(int delayMillis, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);

}
