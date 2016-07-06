package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public interface UIWidgetMgrFactory {
	UIWidget_i getUIWidget(String widget);
}
