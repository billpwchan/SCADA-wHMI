package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public interface UIPanelViewProvide_i {
	void setTaskProvide(UITask_i taskProvide);
	void addHandlerRegistration(HandlerRegistration handlerRegistration);
	void removeHandlerRegistrations();
}
