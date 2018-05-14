package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class InitUIGenericMgrFactorys {

	private final static String name = InitUIGenericMgrFactorys.class.getName();
	private final static String className = InitUIGenericMgrFactorys.class.getSimpleName();
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(name);

	public static void init() {
		String function = "init";
		logger.begin(className, function);

		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGenericMgr.removeUIGenericMgrFactory(name);
		uiGenericMgr.addUIGenericMgrFactory(name, new UIGenericMgrFactory() {

			@Override
			public UIGeneric getUIGeneric(String key) {
				final String function = "getUIGeneric";
				logger.debug(className, function, "key[{}]", key);

				UIGeneric uiGeneric = null;

				if ( 
						UILayoutGeneric.class.getSimpleName().equals(key) ) {

					uiGeneric = new UILayoutGeneric();
				} 
				else if ( 
						UIWidgetGeneric.class.getSimpleName().equals(key) ) {

					uiGeneric = new UIWidgetGeneric();
				}

				if ( null == uiGeneric ) logger.warn(className, function, "key[{}] uiGeneric IS NULL", key);

				return uiGeneric;
			}
		});

		logger.end(className, function);
	}
}
