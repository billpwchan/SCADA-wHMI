package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;

public interface UIWidgetCtrl_i {
	void onActionReceived(UIEventAction uiEventAction);
	void onClick(ClickEvent event);
	void onUIEvent(UIEvent uiEvent);
}
