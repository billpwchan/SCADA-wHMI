package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;

public interface UIWidgetEventOnClickHandler extends UIWidgetEvent {
	void onClickHandler(ClickEvent event);
}
