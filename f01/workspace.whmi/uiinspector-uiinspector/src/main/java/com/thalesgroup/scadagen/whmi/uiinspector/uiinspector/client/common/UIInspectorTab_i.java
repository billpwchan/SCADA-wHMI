package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;

public interface UIInspectorTab_i {
	
	void setUINameCard(UINameCard uiNameCard);
	void init();
	Panel getMainPanel();
	
	void buildWidgets(int numOfPointForEachPage);

	void setMessageBoxEvent(MessageBoxEvent messageBoxEvent);
	void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent);
	
	void setParent(String scsEnvId, String parent);
	void setAddresses(String[] addresses);
	String[] getAddresses();
	
	void setRight(HashMap<String, String> rights);
	void applyRight();
	
	void connect();
	void disconnect();
	
	void setDatabase(Database database);
	
}
