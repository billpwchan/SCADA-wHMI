package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

import com.google.gwt.event.shared.EventHandler;

public interface DialogMsgEventHandler extends EventHandler {
	
	void onEventBusDialogMsgChanged(DialogMsgEvent dialogMsgEvent);

}
