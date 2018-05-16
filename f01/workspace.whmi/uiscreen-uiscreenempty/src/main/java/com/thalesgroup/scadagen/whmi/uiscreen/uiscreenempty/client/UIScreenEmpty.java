
package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIScreenEmpty extends UIWidget_i {

	private final String className_ = this.getClass().getSimpleName();
	private UILogger_i logger_ = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	@Override
	public void init() {
		final String f = "init";
		logger_.begin(f);

		rootPanel = new FlowPanel();
		rootPanel.addStyleName("project-"+className_+"-root");

		logger_.end(f);
	}

}
