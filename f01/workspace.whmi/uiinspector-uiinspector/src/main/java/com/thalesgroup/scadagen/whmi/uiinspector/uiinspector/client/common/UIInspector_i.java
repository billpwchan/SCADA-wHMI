package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetView_i;

public interface UIInspector_i extends UIWidgetView_i {
	
	final String strUIInspector = "UIInspectorPanel";
	
	final String INSPECTOR = "inspector";
	
	void setParent(String scsEnvId, String parent);
	void connect();
	void disconnect();
	void close();

}
