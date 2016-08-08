package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;

public interface UIWidgetEventOnKeyPressHandler extends UIWidgetEvent {
	void onKeyPressHandler(KeyPressEvent event);
}
