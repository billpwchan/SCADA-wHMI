package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBoxEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetView_i;

public interface UIInspector_i extends UIWidgetView_i {
	
	final String strUIInspector = "UIInspectorPanel";
	
	void setParent(String scsEnvId, String parent);
	void connect();
	void disconnect();
	void close();
	
	void setUIPanelInspectorEvent(UIPanelInspectorDialogBoxEvent uiPanelInspectorDialogBoxEvent);

}
