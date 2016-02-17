package com.thalesgroup.scadagen.whmi.uievent.uievent.client;

import com.google.gwt.event.shared.EventHandler;

public interface UIEventHandler extends EventHandler {
	void onEvenBusUIChanged(UIEvent uiEvent);
}
