package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

public class UIEventActionBusFire extends UIEventActionExecute_i {
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	@Override
	public boolean executeAction( UIEventAction action, Map<String, Map<String, Object>> override ) {
		final String function = "executeAction";
		
		boolean bContinue = true;
		
		logger.begin(function);
		
		if ( null == simpleEventBus ) {
			logger.warn(function, logPrefix+"simpleEventBus IS null");
			return bContinue;
		}
		
		if ( null == action ) {
			logger.warn(function, logPrefix+"action IS null");
			return bContinue;
		}
		
		logger.info(function, "fireEventFromSource");
		simpleEventBus.fireEventFromSource(action, this);

		logger.end(function);
		return bContinue;
	}

}
