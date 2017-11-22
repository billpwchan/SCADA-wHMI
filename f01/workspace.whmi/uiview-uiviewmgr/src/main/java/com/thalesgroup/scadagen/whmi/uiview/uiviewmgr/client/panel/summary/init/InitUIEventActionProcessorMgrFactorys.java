package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessor;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;

public class InitUIEventActionProcessorMgrFactorys {
	
	private final static String name = InitUIEventActionProcessorMgrFactorys.class.getName();
	private final static String className = UIWidgetUtil.getClassSimpleName(name);
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public static void init() {
		String function = "init";
		logger.begin(className, function);
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessorMgr.removeUIEventActionProcessorMgrFactory(name);
		uiEventActionProcessorMgr.addUIEventActionProcessorMgrFactory(name, new UIEventActionProcessorMgrFactory() {
			
			@Override
			public UIEventActionProcessor_i getUIEventActionProcessor(String key) {
				final String function = "getUIEventActionProcessor";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionProcessor_i uiEventActionProcessor_i = null;
				
				if ( UIWidgetUtil.getClassSimpleName(UIEventActionProcessor.class.getName())
						.equals(key) ) {
					uiEventActionProcessor_i = new UIEventActionProcessor();
				}
				
				if ( null == uiEventActionProcessor_i ) logger.warn(className, function, "key[{}] uiEventActionProcessor_i IS NULL", key);
				
				return uiEventActionProcessor_i;
			}
		});
		
		logger.end(className, function);
	}
}
