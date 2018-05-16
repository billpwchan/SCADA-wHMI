package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel;

import com.google.gwt.user.client.ui.Button;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.Control;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIButtonToggle extends Button {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Control control = null;
	public Control getControl() { return control; }
	public void setControl( Control control ) { this.control = new Control(control); }
	public UIButtonToggle( String string ){
		super(string);
	}
	
	private String strStyleName = "project-gwt-button-inspector-control-selected";
	public void setHightLightStyleName(String strStyleName) { this.strStyleName = strStyleName; }
	
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
		logger.begin(function);
		logger.debug(function, "setHightLight Begin hightLight[{}]", hightLight);

		this.hightLight = hightLight;
		
		logger.debug(function, "setHightLight strStyleName[{}] hightLight[{}]", strStyleName, hightLight);

		if ( hightLight ) {
			this.addStyleName(strStyleName);
		} else {
			this.removeStyleName(strStyleName);
		}
		
		logger.end(function);
	}
	
}
