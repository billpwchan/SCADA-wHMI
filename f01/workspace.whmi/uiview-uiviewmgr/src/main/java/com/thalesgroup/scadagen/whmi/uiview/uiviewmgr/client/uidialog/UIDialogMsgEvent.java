package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog;

public interface UIDialogMsgEvent {
	enum UIDialogMsgEventType {
		MSG_OPT_1("MSG_OPT_1")
		, MSG_OPT_2("MSG_OPT_2")
		;
		private final String text;
		private UIDialogMsgEventType(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	public void uiDialogMsgMgrEvent(UIDialogMsgEventType uiDialogMsgMgrEventType);
}
