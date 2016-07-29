package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.user.client.ui.Button;

public class UIButtonToggle extends Button {
	
	private static Logger logger = Logger.getLogger(UIButtonToggle.class.getName());
	
	private Control control = null;
	public Control getControl() { return control; }
	public void setControl( Control control ) { this.control = new Control(control); }
	public UIButtonToggle( String string ){
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

		this.hightLight = hightLight;
		
		String styleName = "project-gwt-button-inspector-control-selected";
		
		logger.log(Level.FINE, "setHightLight addStyleName["+styleName+"] hightLight["+hightLight+"]");

		if ( hightLight ) {
			this.addStyleName(styleName);
		} else {
			this.removeStyleName(styleName);
		}
		
		logger.log(Level.FINE, "setHightLight End");
	}
	
}
