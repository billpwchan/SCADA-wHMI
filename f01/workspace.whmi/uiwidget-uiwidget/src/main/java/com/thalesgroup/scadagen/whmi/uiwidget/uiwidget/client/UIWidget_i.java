package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIWidget_i {
	void init(String xmlFile);
	ComplexPanel getMainPanel(UINameCard uiNameCard);
	Widget getWidget(String widget);
	String getWidgetElement(Widget widget);
	void setValue(String name);
	void setValue(String name, String value);
	void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	String getWidgetStatus(String element);
	void setWidgetStatus(String element, String up);
}
