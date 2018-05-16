package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperSoundServerPanel;

public class UIPanelSoundServerController extends UIWidget_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(function);
		WrapperSoundServerPanel wrapperSoundServerPanel = new WrapperSoundServerPanel();
		rootPanel = new HorizontalPanel();
		rootPanel.add(wrapperSoundServerPanel.getMainPanel());
		rootPanel.addStyleName("project-gwt-panel-uipanelsoundservercontroller");
		logger.end(function);
	}

}
