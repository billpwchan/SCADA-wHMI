package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIButtonNavigation extends Button {
	
	private static Logger logger = Logger.getLogger(UIButtonNavigation.class.getName());
	
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
		
		logger.log(Level.FINE, "setHightLight Begin hightLight["+hightLight+"]");

		int level = this.taskLaunch.getTaskLevel();
	
		this.hightLight = hightLight;
		
		String styleName = "project-gwt-button-navigation-"+level+"-selected";
		
		logger.log(Level.FINE, "setHightLight addStyleName["+styleName+"] hightLight["+hightLight+"]");

		if ( hightLight ) {
			this.addStyleName(styleName);
		} else {
			this.removeStyleName(styleName);
		}

		logger.log(Level.FINE, "setHightLight End");
	}
	
}
