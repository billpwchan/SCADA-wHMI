package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public interface UIWidgetMgrFactory {
	UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, HashMap<String, Object> options);
}
