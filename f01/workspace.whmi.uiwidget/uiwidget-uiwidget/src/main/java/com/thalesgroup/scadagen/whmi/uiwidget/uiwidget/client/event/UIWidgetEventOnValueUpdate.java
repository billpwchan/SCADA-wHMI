package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;

public interface UIWidgetEventOnValueUpdate extends UIWidgetEvent {
	void onValueChange(String name, String value);
}
