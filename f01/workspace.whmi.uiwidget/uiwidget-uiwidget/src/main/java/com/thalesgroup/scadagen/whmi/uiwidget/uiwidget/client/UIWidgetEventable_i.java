package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent_i;

/**
 * @author syau
 * UIEvent API
 */
public interface UIWidgetEventable_i extends UIWidgetView_i {
	void setUIEvent(UIEvent_i uiEvent_i);
}
