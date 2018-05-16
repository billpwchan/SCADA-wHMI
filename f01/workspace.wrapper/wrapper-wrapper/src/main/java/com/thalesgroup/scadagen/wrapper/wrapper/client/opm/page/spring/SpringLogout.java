package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.spring;

import com.google.gwt.user.client.Window;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class SpringLogout {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String actionUrl = null;
	
	public SpringLogout(String actionUrl) {
		String function = className;
		logger.begin(function);
		
		logger.debug(function, "actionUrl[{}]", actionUrl);
		
		this.actionUrl = actionUrl;
		
        logger.end(function);
	}

	public void logout() {
		String function = "logout";
		logger.begin(function);
		
		Window.Location.replace(actionUrl);
		
		logger.end(function);
	}
	
}
