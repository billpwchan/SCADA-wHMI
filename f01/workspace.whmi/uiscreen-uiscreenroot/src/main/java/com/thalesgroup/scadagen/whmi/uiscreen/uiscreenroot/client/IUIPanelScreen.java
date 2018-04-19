package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

public interface IUIPanelScreen {
	public enum ParameterName {
		  SINGLE_SCREEN_VIEWS("singleScreenViews")
		, SINGLE_SCREEN_CTRLS("singleScreenCtrls")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
