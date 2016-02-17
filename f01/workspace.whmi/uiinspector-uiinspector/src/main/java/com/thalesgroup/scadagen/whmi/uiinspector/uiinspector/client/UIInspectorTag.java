package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIInspectorTag {

	public VerticalPanel getMainPanel(){

		VerticalPanel basePanel = new VerticalPanel();
		basePanel.add(new InlineLabel("UIInspectorTag"));
	
		return basePanel;
	}
}
