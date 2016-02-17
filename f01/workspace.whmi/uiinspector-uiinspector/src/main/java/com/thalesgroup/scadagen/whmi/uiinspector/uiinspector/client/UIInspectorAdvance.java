package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIInspectorAdvance {

	public VerticalPanel getMainPanel(){

		VerticalPanel basePanel = new VerticalPanel();
		basePanel.add(new InlineLabel("UIInspectorAdvance"));
	
		return basePanel;
	}
}
