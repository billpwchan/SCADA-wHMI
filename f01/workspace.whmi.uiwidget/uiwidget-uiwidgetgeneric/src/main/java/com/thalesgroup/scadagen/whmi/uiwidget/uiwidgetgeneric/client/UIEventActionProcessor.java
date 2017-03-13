package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;

public class UIEventActionProcessor extends UIEventActionProcessorCore implements UIEventActionProcessor_i {

	private static final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessorCore.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	final String init = "init";
	final String envup = "envup";
	final String envdown = "envdown";
	final String terminate = "terminate";
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public boolean executeActionSetInit() {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet("init", null, null);
		logger.end(className, function);
		return bContinue;
	}

	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetInit(int delayMillis, final HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		executeActionSetInit(delayMillis, override, null);
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetInit(int delayMillis, final HashMap<String, HashMap<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet("init_delay", override, executeActionHandler);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}
	
	@Override
	public boolean executeActionSetEnvUp() {
		final String function = prefix+" executeActionSetEnvUp";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet("envup", null, null);
		logger.end(className, function);
		return bContinue;
	}

	@Override
	public void executeActionSetEnvUp(int delayMillis, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSetEnvUp";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		executeActionSetEnvUp(delayMillis, override, null);
		logger.end(className, function);
	}

	@Override
	public void executeActionSetEnvUp(int delayMillis, final HashMap<String, HashMap<String, Object>> override,
			final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSetEnvUp";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet("envup_delay", override, executeActionHandler);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}

	@Override
	public boolean executeActionSetEnvDown() {
		final String function = prefix+" executeActionSetEnvDown";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet("envdown", null, null);
		logger.end(className, function);
		return bContinue;
	}

	@Override
	public void executeActionSetEnvDown(int delayMillis, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSetEnvDown";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		executeActionSetEnvDown(delayMillis, override, null);
		logger.end(className, function);
	}

	@Override
	public void executeActionSetEnvDown(int delayMillis, final HashMap<String, HashMap<String, Object>> override,
			final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSetEnvDown";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet("envdown_delay", override, executeActionHandler);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public boolean executeActionSetTerminate() {
		final String function = prefix+" executeActionSetTerminate";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet("terminate", null, null);
		logger.end(className, function);
		return bContinue;
	}

	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetTerminate(int delayMillis, final HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSetTerminate";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		executeActionSetTerminate(delayMillis, override, null);
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetTerminate(int delayMillis, final HashMap<String, HashMap<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSetTerminate";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet("terminate_delay", override, executeActionHandler);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}

}