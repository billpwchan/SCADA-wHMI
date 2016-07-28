package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIInspector_i {
	
	void setParent(String scsEnvId, String parent);
	void setPeriod(String period);
	void connect();
	void disconnect();
	
	void setUINameCard(UINameCard uiNameCard);
	void init(String xml);
	ComplexPanel getMainPanel();

}
