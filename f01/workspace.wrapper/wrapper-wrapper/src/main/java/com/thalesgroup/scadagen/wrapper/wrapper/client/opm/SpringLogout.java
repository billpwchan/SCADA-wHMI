package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.google.gwt.user.client.Window;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class SpringLogout {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SpringLogout.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String actionUrl = null;
	
	public SpringLogout(String actionUrl) {
		String function = className;
		
		logger.begin(className, function);
		
		logger.info(className, function, "actionUrl[{}]", actionUrl);
		
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
