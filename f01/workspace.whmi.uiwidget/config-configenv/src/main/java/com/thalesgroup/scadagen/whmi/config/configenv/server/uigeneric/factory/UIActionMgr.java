package com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.factory;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIActionMgr {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(UIActionMgr.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private HashMap<String, UIActionFactory> uiActionFactorys = new HashMap<String, UIActionFactory>();
	public void addUIActionFactory(String className, UIActionFactory uiActionFactory) { this.uiActionFactorys.put(className, uiActionFactory); }
	public void cleanUIActionFactory() { this.uiActionFactorys.clear(); };
	
	private UIActionMgr() {}
	private static UIActionMgr instance = null;
	public static UIActionMgr getInstance() {
		if ( null == instance ) instance = new UIActionMgr();
		return instance;
	}
	
	public UIAction_i getUIAction(String key) {
		String function = "getUIAction";
		logger.begin(className, function);
		UIAction_i uiAction_i = null;
		for ( String className : uiActionFactorys.keySet() ) {
			UIActionFactory uiActionFactory = uiActionFactorys.get(className);
			if ( null != uiActionFactory ) {
				uiAction_i = uiActionFactory.getUIAction(key);
			}
		}
		logger.end(className, function);
		return uiAction_i;
	}
	
}
