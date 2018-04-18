package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

public interface IUIPanelScreen {
	public enum ParameterName {
		  EventBusName("EventBusName")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
