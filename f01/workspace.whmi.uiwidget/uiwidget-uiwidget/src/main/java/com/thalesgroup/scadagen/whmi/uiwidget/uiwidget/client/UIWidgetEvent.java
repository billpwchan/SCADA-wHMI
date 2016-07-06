package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;

public interface UIWidgetEvent {
	void onValueChange(String name, String value);
	void onClickHandler(ClickEvent event);
	void onKeyPressHandler(KeyPressEvent event);
}
