package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgrEvent.DialogMsgMgrEventType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class DialogMsgCtrl {

	private UINameCard uiNameCard;
	private DialogMsgMgrEvent dialogMsgMgrEvent;
	
	public DialogMsgCtrl(UINameCard uiNameCard, DialogMsgMgrEvent dialogMsgMgrEvent) {
		this.uiNameCard = uiNameCard;
		this.dialogMsgMgrEvent = dialogMsgMgrEvent;
	}
	
	public void setYes(UITask_i taskProvide) {
		dialogMsgMgrEvent.dialogMsgMgrEvent(DialogMsgMgrEventType.MSG_YES);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskProvide));
	}
	
	public void setNo(UITask_i taskProvide) {
		dialogMsgMgrEvent.dialogMsgMgrEvent(DialogMsgMgrEventType.MSG_NO);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskProvide));
	}
	
}
