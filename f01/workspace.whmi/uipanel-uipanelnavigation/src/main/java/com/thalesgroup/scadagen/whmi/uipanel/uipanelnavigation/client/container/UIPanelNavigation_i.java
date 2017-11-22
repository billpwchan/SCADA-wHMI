package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

public interface UIPanelNavigation_i {
	public enum ParameterName {
		  MenuLevel("MenuLevel")
		, MenuType("MenuType")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
