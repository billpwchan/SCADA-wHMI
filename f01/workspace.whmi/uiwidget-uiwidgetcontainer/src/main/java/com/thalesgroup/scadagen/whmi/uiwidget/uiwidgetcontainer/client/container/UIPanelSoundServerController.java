package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperSoundServerPanel;

public class UIPanelSoundServerController implements UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UIPanelSoundServerController.class.getName());
	
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		
	};

	private ComplexPanel root = null;
	@Override
	public void init(String xmlFile) {
		logger.log(Level.FINE, "init Begin");
		WrapperSoundServerPanel wrapperSoundServerPanel = new WrapperSoundServerPanel();
		root = new HorizontalPanel();
		root.add(wrapperSoundServerPanel.getMainPanel());
		root.addStyleName("project-gwt-panel-uipanelsoundservercontroller");
		logger.log(Level.FINE, "init Begin");
	}

	@Override
	public ComplexPanel getMainPanel() {

		return root;
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

	@Override
	public void setParameter(String name, String value) {
		// TODO Auto-generated method stub
		
	}

}
