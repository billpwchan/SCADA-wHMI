package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionOls_i {
	public enum UIEventActionOlsAction {
		  DeleteData("DeleteData")
		, SubscribeOlsList("SubscribeOlsList")
		, UnsubscribeOlsList("UnsubscribeOlsList")
		, ReadData("ReadData")
		, InsertData("InsertData")
		, UpdateData("UpdateData")
		;
		private final String text;
		private UIEventActionOlsAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIEventActionOlsAction[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
