package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm;

public class UIActionFactory {
	
	private final UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	private final String className = this.getClass().getSimpleName();
	
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
