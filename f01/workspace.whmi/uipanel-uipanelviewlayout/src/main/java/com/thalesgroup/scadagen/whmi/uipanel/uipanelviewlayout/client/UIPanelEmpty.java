package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelEmpty extends UIWidget_i {

	private Logger logger = Logger.getLogger(UIPanelEmpty.class.getName());


	@Override
	public void init() {
		logger.log(Level.FINE, "init Begin");

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);

		logger.log(Level.FINE, "init End");
	}


	LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();

	public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}

	public void removeHandlerRegistrations() {
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}

}
