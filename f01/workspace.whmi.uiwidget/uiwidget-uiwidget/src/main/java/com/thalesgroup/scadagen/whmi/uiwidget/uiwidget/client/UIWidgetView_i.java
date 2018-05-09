package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

/**
 * @author syau
 * View API
 * Step:
 * 1. setUINameCard
 * 2. setXMLFile
 * 3. init
 * 4. getMainPanel
 */
public interface UIWidgetView_i {
	
	void setElement(String element);
	String getElementName();
	void setUINameCard(UINameCard uiNameCard);
	UINameCard getUINameCard();
	void setCtrlHandler(String ctrlHandler);
	void setViewXMLFile(String viewXMLFile);
	String getViewXMLFile();
	void setOptsXMLFile(String optsXMLFile);
	String getOptsXMLFile();
	void init();
	void terminate();
	Panel getMainPanel();

}
