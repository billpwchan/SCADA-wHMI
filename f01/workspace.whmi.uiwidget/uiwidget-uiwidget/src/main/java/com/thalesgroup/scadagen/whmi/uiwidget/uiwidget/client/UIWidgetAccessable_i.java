package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

/**
 * @author syau
 * Widget Access API
 */
public interface UIWidgetAccessable_i extends UIWidgetEventable_i {
	
	void setParameter(String key, Object value);

	Widget getWidget(String widget);
	String getWidgetElement(Widget widget);
	
	void setValue(String name);
	void setValue(String name, String value);
	
	void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	UIWidgetEvent getUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	
	WidgetStatus getWidgetStatus(String element);
	void setWidgetStatus(String element, WidgetStatus status);
	
//	String getWidgetStatus(String element);
//	void setWidgetStatus(String element, String up);
}
