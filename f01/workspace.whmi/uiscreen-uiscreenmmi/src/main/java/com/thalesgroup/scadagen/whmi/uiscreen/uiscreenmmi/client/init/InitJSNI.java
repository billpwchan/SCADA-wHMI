package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi.CallGWTByJS;

public class InitJSNI {
	
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(InitJSNI.class.getName());
	
	public static void init() {
		String function = "init";
		logger.begin(function);
		try {
			CallGWTByJS.exportCallGWTByJS();
		} catch ( Exception ex ) {
			logger.warn(function, "CallGWTByJS.exportCallGWTByJS() Exception["+ex.toString()+"]");
		}
		logger.end(function);
	}
}
