package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class InitUIGenericMgrFactorys {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitUIGenericMgrFactorys.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void init() {
		String function = "init";
		logger.begin(className, function);
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGenericMgr.clearUIGenericMgrFactorys();
		uiGenericMgr.addUIGenericMgrFactory(className, new UIGenericMgrFactory() {
	
			@Override
			public UIGeneric getUIGeneric(String key) {
				final String function = "getUIGeneric";
				logger.info(className, function, "key[{}]", key);
				
				UIGeneric uiGeneric = null;
				
				if ( UIWidgetUtil.getClassSimpleName(UILayoutGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UILayoutGeneric();
				} 
				else
				if ( UIWidgetUtil.getClassSimpleName(UIWidgetGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UIWidgetGeneric();
				}
				
				if ( null == uiGeneric ) logger.warn(className, function, "key[{}] uiGeneric IS NULL", key);
				
				return uiGeneric;
			}
		});
		
		logger.end(className, function);
	}
}
