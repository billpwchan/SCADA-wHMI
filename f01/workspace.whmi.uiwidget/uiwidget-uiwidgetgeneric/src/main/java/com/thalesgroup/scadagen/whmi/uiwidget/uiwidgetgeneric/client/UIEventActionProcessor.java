package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Map;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;

public class UIEventActionProcessor extends UIEventActionProcessorCore implements UIEventActionProcessor_i {

	private static final String className = UIEventActionProcessor.class.getSimpleName();
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(UIEventActionProcessor.class.getName());
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public boolean executeActionSetInit() {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet(init, null, null);
		logger.end(className, function);
		return bContinue;
	}

	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSet(final String actionsetkey, final int delayMillis, final Map<String, Map<String, Object>> override) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "delayMillis[{}]", delayMillis);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet(actionsetkey, override);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}

}