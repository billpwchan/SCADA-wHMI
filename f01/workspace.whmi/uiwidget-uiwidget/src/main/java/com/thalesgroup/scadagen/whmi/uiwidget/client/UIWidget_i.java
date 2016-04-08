package com.thalesgroup.scadagen.whmi.uiwidget.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

public interface UIWidget_i {
	void init(String xmlFile);
	ComplexPanel getMainPanel(UINameCard uiNameCard);
	Widget getWidget(String widget);
	String getWidgetElement(Widget widget);
	void setValue(String name);
	void setValue(String name, String value);
	void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	WidgetStatus getWidgetStatus(String element);
	void setWidgetStatus(String element, WidgetStatus up);
}
