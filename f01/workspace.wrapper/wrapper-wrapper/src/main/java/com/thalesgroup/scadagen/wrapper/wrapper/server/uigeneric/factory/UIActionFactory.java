package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm;

public class UIActionFactory {
	
	private final Logger logger = LoggerFactory.getLogger(UIActionFactory.class);
	
	private static final String className = UIActionFactory.class.getSimpleName();
	
	public void init() {
		logger.debug("Begin");
		UIActionMgr uiActionMgr = UIActionMgr.getInstance();
		uiActionMgr.addUIActionFactory(className, new UIActionFactory_i() {
			
			@Override
			public UIAction_i getUIAction(String key) {
				UIAction_i uiAction_i = null;
				logger.debug("key[{}]", key);
				if ( null != key ) {
					String strUIActionOpm = UIActionOpm.class.getSimpleName();
					if ( strUIActionOpm.equalsIgnoreCase(key) ) {
						uiAction_i = new UIActionOpm();
					}
				} else {
					logger.debug("key IS NULL");
				}
				if ( null == uiAction_i ) { logger.debug("key[{}] uiAction_i IS NULL", key); }
				return uiAction_i;
			}
		});
		logger.debug("End");
	}
}
