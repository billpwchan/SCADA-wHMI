package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIInspectorTab_i {
	ComplexPanel getMainPanel(UINameCard uiNameCard);
	void setParent(String parent);
	void setAddresses(String scsEnvId, String[] addresses, String period);
	void setMessageBoxEvent(MessageBoxEvent messageBoxEvent);
	void buildWidgets();
	void connect();
	void disconnect();
}
