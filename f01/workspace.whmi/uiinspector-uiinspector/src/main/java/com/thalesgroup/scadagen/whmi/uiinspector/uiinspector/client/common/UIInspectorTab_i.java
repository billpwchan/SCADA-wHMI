package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIInspectorTab_i {
	
	void setUINameCard(UINameCard uiNameCard);
	void init(String xml);
	ComplexPanel getMainPanel();
	
	void setParent(String scsEnvId, String parent);
	void setAddresses(String[] addresses);
	String[] getAddresses();
	void setMessageBoxEvent(MessageBoxEvent messageBoxEvent);
	void buildWidgets();
	void connect();
	void disconnect();
}
