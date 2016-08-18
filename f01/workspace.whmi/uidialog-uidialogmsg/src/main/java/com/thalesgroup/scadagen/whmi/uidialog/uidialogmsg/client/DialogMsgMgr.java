package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

public class DialogMsgMgr {
	private DialogMsgMgr() {}
	private static DialogMsgMgr instance = null;
	public static DialogMsgMgr getInstance() {
		if ( null == instance ) { 
			instance = new DialogMsgMgr();
		}
		return instance;
	}
	public UIDialog_i getDialog(String dialog) {
		UIDialog_i uiDialog = null;
		if ( 0 == dialog.compareTo("UIDialogMsg") ) {
			uiDialog = new UIDialogMsg();
		}
		return uiDialog;
	}
}
