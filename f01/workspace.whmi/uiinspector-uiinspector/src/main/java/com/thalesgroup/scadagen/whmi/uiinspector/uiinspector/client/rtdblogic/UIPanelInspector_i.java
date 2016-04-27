package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public interface UIPanelInspector_i {
	ComplexPanel getMainPanel(UINameCard uiNameCard);
	void setConnection(String scsEnvId, String dbaddress);
	void readyToReadChildrenData();
	void readyToReadStaticData();
	void readyToSubscribeDynamicData();
	void removeConnection();
}
