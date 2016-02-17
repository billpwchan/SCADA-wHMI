package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

import com.google.gwt.event.shared.GwtEvent;

public class DialogMsgEvent extends GwtEvent<DialogMsgEventHandler> {
	
	public static Type<DialogMsgEventHandler> TYPE = new Type<DialogMsgEventHandler>();

	@Override
	public Type<DialogMsgEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(DialogMsgEventHandler handler) {
	    handler.onEventBusDialogMsgChanged(this);
	}
	
	public enum DialogMsgEventType {
		DialogMsgEventType_YES, DialogMsgEventType_NO 
	}
	
	private DialogMsgEventType dialogMsgEventType;
	public DialogMsgEventType getDialogMsgEventType() { return dialogMsgEventType; }
	public void setDialogMsgEventType(DialogMsgEventType dialogMsgEventType) { this.dialogMsgEventType = dialogMsgEventType; }

	public DialogMsgEvent ( DialogMsgEventType dialogMsgEventType ) {
		this.setDialogMsgEventType(dialogMsgEventType);
	}

}
