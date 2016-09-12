package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIActionEventExceute {
	private final String className = UIWidgetUtil.getClassSimpleName(UIActionEventExceute.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	public UIActionEventExceute ( String logPrefix ) {
		this.logPrefix = "-> "+logPrefix+" ";
	}
	public void executeActionSet(SimpleEventBus eventBus, HashMap<String, UIEventAction> uiEventActionOperations, String actionSetKey, HashMap<String, UIEventAction> uiEventActions) {
		final String function = "executeActionSet";
		
		logger.begin(className, function);
		
		if ( null == eventBus ) {
			logger.warn(className, function, logPrefix+"eventBus IS null");
			return;
		}
		
		if ( null == uiEventActionOperations ) {
			logger.warn(className, function, logPrefix+"uiEventActionOperations IS null");
			return;
		}
		
		if ( null == actionSetKey ) {
			logger.warn(className, function, logPrefix+"actionSetKey IS null");
			return;
		}
		
		if ( null == uiEventActions ) {
			logger.warn(className, function, logPrefix+"uiEventActions IS null");
			return;
		}
		
		logger.warn(className, function, logPrefix+"getting actionSetKey[{}]", actionSetKey);
		UIEventAction uiEventAction = uiEventActionOperations.get(actionSetKey);
		if ( null != uiEventAction ) {
			for ( Entry<String, Object> entry : uiEventAction.getParameters() ) {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				if ( null != value ) {
					logger.info(className, function, logPrefix+"key[{}] value[{}]", key, value);
					UIEventAction action1 = uiEventActions.get(value);
					if ( null != action1 ) {
						logger.info(className, function, "fireEventFromSource");
						eventBus.fireEventFromSource(action1, this);
					} else {
						logger.warn(className, function, logPrefix+"action1[{}] IS null", value);
					}
				}
			}
		} else {
			logger.warn(className, function, logPrefix+"actionSetKey[{}] uiEventAction IS null", actionSetKey);
		}

		logger.end(className, function);
	}
}
