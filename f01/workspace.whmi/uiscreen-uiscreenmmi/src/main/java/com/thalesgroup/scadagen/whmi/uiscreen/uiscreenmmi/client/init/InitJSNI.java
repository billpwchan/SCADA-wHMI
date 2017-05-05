package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi.CallGWTByJS;

public class InitJSNI {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitJSNI.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void init() {
		String function = "init";
		logger.begin(className, function);
		try {
			CallGWTByJS.exportCallGWTByJS();
		} catch ( Exception ex ) {
			logger.warn(className, function, "CallGWTByJS.exportCallGWTByJS() Exception["+ex.toString()+"]");
		}
		logger.end(className, function);
	}
}
