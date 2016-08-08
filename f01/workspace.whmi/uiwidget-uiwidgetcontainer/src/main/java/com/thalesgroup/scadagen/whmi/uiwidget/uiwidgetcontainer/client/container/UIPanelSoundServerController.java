package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperSoundServerPanel;

public class UIPanelSoundServerController extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(UIPanelSoundServerController.class.getName());

	@Override
	public void init() {
		logger.log(Level.FINE, "init Begin");
		WrapperSoundServerPanel wrapperSoundServerPanel = new WrapperSoundServerPanel();
		rootPanel = new HorizontalPanel();
		rootPanel.add(wrapperSoundServerPanel.getMainPanel());
		rootPanel.addStyleName("project-gwt-panel-uipanelsoundservercontroller");
		logger.log(Level.FINE, "init Begin");
	}

}
