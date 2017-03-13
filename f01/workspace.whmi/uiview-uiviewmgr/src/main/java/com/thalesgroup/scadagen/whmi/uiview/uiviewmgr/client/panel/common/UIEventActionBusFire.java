package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

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

}
