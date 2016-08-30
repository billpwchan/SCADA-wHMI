package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBoxEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetView_i;

public interface UIInspector_i extends UIWidgetView_i {
	
	void setParent(String scsEnvId, String parent);
	void setPeriod(String period);
	void connect();
	void disconnect();
	
	void setUIPanelInspectorEvent(UIPanelInspectorDialogBoxEvent uiPanelInspectorDialogBoxEvent);

}
