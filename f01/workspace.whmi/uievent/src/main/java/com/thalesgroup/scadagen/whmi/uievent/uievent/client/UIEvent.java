package com.thalesgroup.scadagen.whmi.uievent.uievent.client;

import com.google.gwt.event.shared.GwtEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class UIEvent extends GwtEvent<UIEventHandler> { 
	
	public static Type<UIEventHandler> TYPE = new Type<UIEventHandler>();

	@Override
	public Type<UIEventHandler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(UIEventHandler handler) {
		handler.onEvenBusUIChanged(this);
	}
	
	private UITask_i taskProvide = null;
	public UIEvent(UITask_i taskProvide) { this.taskProvide = taskProvide; }
	public UITask_i getTaskProvide() { return this.taskProvide; }
	public void setTaskProvide(UITask_i taskProvide) { this.taskProvide = taskProvide; }

}
