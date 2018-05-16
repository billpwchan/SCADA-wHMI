package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class InitUIGenericMgrFactorys {
	
	private final static String name = InitUIGenericMgrFactorys.class.getName();
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(InitUIGenericMgrFactorys.class.getName());
	
	public static void init() {
		String function = "init";
		logger.begin(function);
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGenericMgr.clearUIGenericMgrFactorys();
		uiGenericMgr.addUIGenericMgrFactory(name, new UIGenericMgrFactory() {
	
			@Override
			public UIGeneric getUIGeneric(String key) {
				final String function = "getUIGeneric";
				logger.info(function, "key[{}]", key);
				
				UIGeneric uiGeneric = null;
				
				if ( UILayoutGeneric.class.getSimpleName()
						.equals(key) ) {
					uiGeneric = new UILayoutGeneric();
				} 
				else
				if ( UIWidgetGeneric.class.getSimpleName()
						.equals(key) ) {
					uiGeneric = new UIWidgetGeneric();
				}
				
				if ( null == uiGeneric ) logger.warn(function, "key[{}] uiGeneric IS NULL", key);
				
				return uiGeneric;
			}
		});
		
		logger.end(function);
	}
}
