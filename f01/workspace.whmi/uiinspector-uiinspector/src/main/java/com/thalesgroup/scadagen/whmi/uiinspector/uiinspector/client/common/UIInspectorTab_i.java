package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIInspectorTab_i {
	
	void setUINameCard(UINameCard uiNameCard);
	void init();
	Panel getMainPanel();
	
	void buildWidgets();

	void setMessageBoxEvent(MessageBoxEvent messageBoxEvent);
	void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent);
	
	void setParent(String scsEnvId, String parent);
	void setAddresses(String[] addresses);
	String[] getAddresses();
	
	void connect();
	void disconnect();
	
}
