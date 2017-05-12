package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIActionEventType_i {
	
	public enum UIActionEventType {
		
		  event("event")

		, alm("alm")
		, ctl("ctl")
		, dbm("dbm")
		, dialogmsg("dialogmsg")
		, dpc("dpc")
		, grc("grc")
		, opm("opm")
		, uitask("uitask")
		, widget("widget")
		, js("js")
		, tsc("tsc")
		
		;
		private final String text;
		private UIActionEventType(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIActionEventType[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
