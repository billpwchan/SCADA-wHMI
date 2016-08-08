package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;

public interface UIWidgetEventOnValueChangeHandler extends UIWidgetEvent {
	void setUIWidgetEventOnValueChangeHandler(ValueChangeEvent<String> event);
}
