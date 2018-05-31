package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class UIActionMgr {
	
	private final UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	private Map<String, UIActionFactory_i> uiActionFactorys = new HashMap<String, UIActionFactory_i>();
	public void addUIActionFactory(String className, UIActionFactory_i uiActionFactory) { this.uiActionFactorys.put(className, uiActionFactory); }
	public void cleanUIActionFactory() { this.uiActionFactorys.clear(); };
	
	private UIActionMgr() {}
	private static UIActionMgr instance = null;
	public static UIActionMgr getInstance() {
		if ( null == instance ) {
			instance = new UIActionMgr();
			new UIActionFactory().init();
		}
		return instance;
	}
	
	public UIAction_i getUIAction(String key) {
		logger.debug("Begin");
		UIAction_i uiAction_i = null;
		for ( String className : uiActionFactorys.keySet() ) {
			UIActionFactory_i uiActionFactory = uiActionFactorys.get(className);
			if ( null != uiActionFactory ) {
				uiAction_i = uiActionFactory.getUIAction(key);
			} else {
				logger.warn("uiActionFactory IS NULL");
			}
		}
		if ( null == uiAction_i ) { logger.warn("uiAction_i IS NULL"); }
		logger.debug("End");
		return uiAction_i;
	}
	
}
