package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm;

public class UIActionMgr {
	
    private final Logger logger = LoggerFactory.getLogger(UIActionOpm.class);
	
	private HashMap<String, UIActionFactory_i> uiActionFactorys = new HashMap<String, UIActionFactory_i>();
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
