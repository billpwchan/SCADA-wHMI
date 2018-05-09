package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionOpm_i {
	
	public enum UIEventActionOpmAction {
		  OpmLogin("OpmLogin")
		, OpmLogout("OpmLogout")
		, OpmChangePassword("OpmChangePassword")
		, OpmReloadPage("OpmReloadPage")
		;
		private final String text;
		private UIEventActionOpmAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
