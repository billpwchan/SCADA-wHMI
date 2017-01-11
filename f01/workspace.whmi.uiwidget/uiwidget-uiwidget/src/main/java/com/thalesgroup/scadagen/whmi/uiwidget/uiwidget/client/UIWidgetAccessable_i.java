package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

/**
 * @author syau
 * Widget Access API
 */
public interface UIWidgetAccessable_i extends UIWidgetEventable_i {
	
	void setParameter(String key, Object value);
	public Object getParameter(String key);
	public boolean containsParameterKey(String key);

	Widget getWidget(String widget);
	String getWidgetElement(Widget widget);
	
	String [] getUIWidgetElements();
	
	void setWidgetValue(String element, String value);
	String getWidgetValue(String element);
	
	WidgetStatus getWidgetStatus(String element);
	void setWidgetStatus(String element, WidgetStatus status);
	
	void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	UIWidgetEvent getUIWidgetEvent(UIWidgetEvent uiWidgetEvent);
	
	void setCtrlHandler(UIWidgetCtrl_i ctrlHandler);
}
