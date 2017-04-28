package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container.widget;

import com.google.gwt.user.client.ui.Button;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;


public class UIButtonNavigation extends Button {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIButtonNavigation.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UITaskLaunch taskLaunch = null;
	public UITaskLaunch getTaskLaunch() { return taskLaunch; }
	public void setTaskLaunch( UITaskLaunch taskLaunch ) { this.taskLaunch = new UITaskLaunch(taskLaunch); }
	public UIButtonNavigation( String string ){
		super(string);
	}
	private boolean hightLight = false;
	/**
	 * set the button as hight light
	 * @return
	 */
	public boolean isHightLight() { return hightLight; };
	
	/**
	 * get the button is hight light or not
	 * @param hightLight
	 */
	public void setHightLight ( boolean hightLight ) {
		final String function = "setHightLight";
		
		logger.begin(className, function);
		
		logger.info(className, function, "setHightLight Begin hightLight[{}]",hightLight);

		int level = this.taskLaunch.getTaskLevel();
	
		this.hightLight = hightLight;
		
		String styleName = "project-gwt-button-navigation-"+level+"-selected";
		
		logger.info(className, function, "setHightLight addStyleName[{}] hightLight[{}]", styleName, hightLight);

		if ( hightLight ) {
			this.addStyleName(styleName);
		} else {
			this.removeStyleName(styleName);
		}

		logger.end(className, function);
	}
	
}
