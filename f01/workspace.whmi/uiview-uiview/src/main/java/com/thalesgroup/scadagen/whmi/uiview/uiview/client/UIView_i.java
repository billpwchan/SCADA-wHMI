package com.thalesgroup.scadagen.whmi.uiview.uiview.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public interface UIView_i {
	DockLayoutPanel getMainPanel(UINameCard uiNameCard);
	void setTaskProvide(UITask_i taskProvide);
	void addHandlerRegistration(HandlerRegistration handlerRegistration);
	void removeHandlerRegistrations();
}
