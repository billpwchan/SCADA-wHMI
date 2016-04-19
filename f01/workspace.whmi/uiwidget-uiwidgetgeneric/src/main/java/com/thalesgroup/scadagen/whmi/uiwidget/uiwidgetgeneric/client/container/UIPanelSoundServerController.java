package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperSoundServerPanel;

public class UIPanelSoundServerController implements UIWidget_i {

	@Override
	public void init(String xmlFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		WrapperSoundServerPanel wrapperSoundServerPanel = new WrapperSoundServerPanel();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(wrapperSoundServerPanel.getMainPanel());
		horizontalPanel.addStyleName("project-gwt-panel-uipanelsoundservercontroller");
		return horizontalPanel;
	}

	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}

}
