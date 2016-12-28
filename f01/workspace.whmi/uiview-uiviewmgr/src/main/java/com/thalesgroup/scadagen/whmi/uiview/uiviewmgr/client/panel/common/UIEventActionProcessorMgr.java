package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIEventActionProcessorMgr {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessorMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private static UIEventActionProcessorMgr instance = null;
	public static UIEventActionProcessorMgr getInstance() { 
		if ( null == instance ) 
			instance = new UIEventActionProcessorMgr();
		return instance; }
	private UIEventActionProcessorMgr() {}
	public UIEventActionProcessor_i getUIEventActionProcessorMgr(String key) {
		final String function = "getUIEventActionExecute";
		logger.info(className, function, "key[{}]", key);
		UIEventActionProcessor_i uiEventActionProcessor_i = null;
		if ( key.equals("UIEventActionProcessor") ) {
			uiEventActionProcessor_i = new UIEventActionProcessor();
		}
		return uiEventActionProcessor_i;
	}
}
