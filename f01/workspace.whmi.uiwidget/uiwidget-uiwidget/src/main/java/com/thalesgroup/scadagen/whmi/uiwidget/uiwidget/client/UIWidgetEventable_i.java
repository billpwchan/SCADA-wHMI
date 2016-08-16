package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author syau
 * UIEvent API
 */
public interface UIWidgetEventable_i extends UIWidgetView_i {
	
	/**
	 * @param handlerRegistration
	 */
	void addHandlerRegistration(HandlerRegistration handlerRegistration);
	/**
	 * 
	 */
	void removeHandlerRegistrations();
	/**
	 * 
	 */
	void terminate();
}
