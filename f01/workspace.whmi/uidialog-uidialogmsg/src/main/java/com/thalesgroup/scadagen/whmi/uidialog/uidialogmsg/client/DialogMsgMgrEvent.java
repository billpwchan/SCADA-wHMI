package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

public interface DialogMsgMgrEvent {
	enum DialogMsgMgrEventType{
		MSG_YES, MSG_NO
	}
	public void dialogMsgMgrEvent(DialogMsgMgrEventType dialogMsgMgrEventType);
}
