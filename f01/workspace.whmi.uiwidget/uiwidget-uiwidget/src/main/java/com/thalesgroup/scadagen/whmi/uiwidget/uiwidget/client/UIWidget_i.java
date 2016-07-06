package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIWidget_i {
	
	void setParameter(String key, String value);
	void setUINameCard(UINameCard uiNameCard);
	void init(String xmlFile);
	ComplexPanel getMainPanel();

	Widget getWidget(String widget);
	String getWidgetElement(Widget widget);
	
	void setValue(String name);
	void setValue(String name, String value);
	
	void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	
	String getWidgetStatus(String element);
	void setWidgetStatus(String element, String up);
}
