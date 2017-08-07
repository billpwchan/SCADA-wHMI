package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionSimultaneousLogin_i {
	public enum UIEventActionSimultaneousLoginAction {
		  SimultaneousLogin("SimultaneousLogin")
		, SimultaneousLogout("SimultaneousLogout")
		, JSSessionStart("JSSessionStart")
		, JSSessionEnd("JSSessionEnd")
		;
		private final String text;
		private UIEventActionSimultaneousLoginAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
