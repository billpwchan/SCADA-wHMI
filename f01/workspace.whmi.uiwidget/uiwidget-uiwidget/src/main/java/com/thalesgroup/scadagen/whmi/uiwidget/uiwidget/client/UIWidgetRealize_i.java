package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

public interface UIWidgetRealize_i {
	public enum ParameterName {
		  SimpleEventBus("SimpleEventBus")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ActionSetName {
		  from_uilayoutsummary_init("from_uilayoutsummary_init")
		, from_uilayoutsummary_envup("from_uilayoutsummary_envup")
		, from_uilayoutsummary_envdown("from_uilayoutsummary_envdown")
		, from_uilayoutsummary_terminate("from_uilayoutsummary_terminate")
		;
		private final String text;
		private ActionSetName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
