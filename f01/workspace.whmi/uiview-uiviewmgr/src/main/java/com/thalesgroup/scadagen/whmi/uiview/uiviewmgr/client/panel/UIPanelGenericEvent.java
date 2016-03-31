package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;

public interface UIPanelGenericEvent {
	void onClickHandler(ClickEvent event);
	void onKeyPressHandler(KeyPressEvent event);
}
