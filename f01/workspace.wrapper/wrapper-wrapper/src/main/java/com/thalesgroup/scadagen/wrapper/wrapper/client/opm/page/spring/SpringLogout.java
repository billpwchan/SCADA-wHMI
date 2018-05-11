package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.spring;

import com.google.gwt.user.client.Window;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class SpringLogout {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private String actionUrl = null;
	
	public SpringLogout(String actionUrl) {
		String function = className;
		logger.begin(className, function);
		
		logger.debug(className, function, "actionUrl[{}]", actionUrl);
		
		this.actionUrl = actionUrl;
		
        logger.end(className, function);
	}

	public void logout() {
		String function = "logout";
		logger.begin(className, function);
		
		Window.Location.replace(actionUrl);
		
		logger.end(className, function);
	}
	
}
