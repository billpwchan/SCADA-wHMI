package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class NavigationMenuButton extends Button {
	
	private static Logger logger = Logger.getLogger(NavigationMenuButton.class.getName());
	
	private UITaskLaunch taskLaunch = null;
	public UITaskLaunch getTaskLaunch() { return taskLaunch; }
	public void setTaskLaunch( UITaskLaunch taskLaunch ) { this.taskLaunch = new UITaskLaunch(taskLaunch); }
	public NavigationMenuButton( String string ){
		super(string);
	}
	private boolean hightLight = false;
	/**
	 * set the button as hight light
	 * @return
	 */
	public boolean isHightLight() { return hightLight; };
	
	private String backgroundImage = "";
	/**
	 * get the button is hight light or not
	 * @param hightLight
	 */
	public void setHightLight ( boolean hightLight ) {
		
		logger.log(Level.FINE, "setHightLight Begin hightLight["+hightLight+"]");

		if ( false ) {
			
			String RGB_BTN_SEL = "rgb(246, 230, 139)";
			String RGB_BTN_BG = "#F1F1F1";
			String IMG_NONE = "none";
			
				this.hightLight = hightLight;
			if ( hightLight ) {
				String backgroundImage = this.getElement().getStyle().getBackgroundImage();
			
				if ( null != backgroundImage && 0 != backgroundImage.compareTo(IMG_NONE) )
					this.backgroundImage = backgroundImage;
			
				this.getElement().getStyle().setBackgroundColor(RGB_BTN_SEL);
				this.getElement().getStyle().setBackgroundImage(IMG_NONE);
			} else {
				this.getElement().getStyle().setBackgroundColor(RGB_BTN_BG);
				if ( 0 != backgroundImage.compareTo(IMG_NONE) || 0 != backgroundImage.compareTo("") )
					this.getElement().getStyle().setBackgroundImage(backgroundImage);
			}
		} else {
		
			int level = this.taskLaunch.getTaskLevel();
		
			this.hightLight = hightLight;
			
			String styleName = "project-gwt-button-navigation-"+level+"-selected";
			
			logger.log(Level.FINE, "setHightLight addStyleName["+styleName+"] hightLight["+hightLight+"]");

			if ( hightLight ) {
				this.addStyleName(styleName);
			} else {
				this.removeStyleName(styleName);
			}

		}
		
		logger.log(Level.FINE, "setHightLight End");
	}
	
}
