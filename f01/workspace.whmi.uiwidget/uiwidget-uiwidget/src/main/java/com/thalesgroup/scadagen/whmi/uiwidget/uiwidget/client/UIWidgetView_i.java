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
	
	void setUINameCard(UINameCard uiNameCard);
	void setViewXMLFile(String viewXMLFile);
	void setOptsXMLFile(String optsXMLFile);
	void init();
	Panel getMainPanel();

}
