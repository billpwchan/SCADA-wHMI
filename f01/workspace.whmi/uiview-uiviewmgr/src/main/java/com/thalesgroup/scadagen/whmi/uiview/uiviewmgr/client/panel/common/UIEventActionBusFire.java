package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIEventActionBusFire extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionBusFire.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	@Override
	public boolean executeAction( UIEventAction action, HashMap<String, HashMap<String, Object>> override ) {
		final String function = "executeAction";
		
		boolean bContinue = true;
		
		logger.begin(className, function);
		
		if ( null == simpleEventBus ) {
			logger.warn(className, function, logPrefix+"simpleEventBus IS null");
			return bContinue;
		}
		
		if ( null == action ) {
			logger.warn(className, function, logPrefix+"action IS null");
			return bContinue;
		}
		
		logger.info(className, function, "fireEventFromSource");
		simpleEventBus.fireEventFromSource(action, this);

		logger.end(className, function);
		return bContinue;
	}
//	public void executeActionSet( HashMap<String, UIEventAction> uiEventActionOperations, String actionSetKey, HashMap<String, UIEventAction> uiEventActions) {
//		final String function = "executeActionSet";
//		
//		logger.begin(className, function);
//		
//		if ( null == eventBus ) {
//			logger.warn(className, function, logPrefix+"eventBus IS null");
//			return;
//		}
//		
//		if ( null == uiEventActionOperations ) {
//			logger.warn(className, function, logPrefix+"uiEventActionOperations IS null");
//			return;
//		}
//		
//		if ( null == actionSetKey ) {
//			logger.warn(className, function, logPrefix+"actionSetKey IS null");
//			return;
//		}
//		
//		if ( null == uiEventActions ) {
//			logger.warn(className, function, logPrefix+"uiEventActions IS null");
//			return;
//		}
//		
//		logger.warn(className, function, logPrefix+"getting actionSetKey[{}]", actionSetKey);
//		UIEventAction uiEventAction = uiEventActionOperations.get(actionSetKey);
//		if ( null != uiEventAction ) {
//			for ( Entry<String, Object> entry : uiEventAction.getParameters() ) {
//				String key = entry.getKey();
//				String value = (String) entry.getValue();
//				if ( null != value ) {
//					logger.info(className, function, logPrefix+"key[{}] value[{}]", key, value);
//					UIEventAction action1 = uiEventActions.get(value);
//					if ( null != action1 ) {
//						logger.info(className, function, "fireEventFromSource");
//						eventBus.fireEventFromSource(action1, this);
//					} else {
//						logger.warn(className, function, logPrefix+"action1[{}] IS null", value);
//					}
//				}
//			}
//		} else {
//			logger.warn(className, function, logPrefix+"actionSetKey[{}] uiEventAction IS null", actionSetKey);
//		}
//
//		logger.end(className, function);
//	}
}
