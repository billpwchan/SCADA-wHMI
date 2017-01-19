package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

public interface UILayoutSummary_i {
	
	public enum WidgetAttribute{
		  SimpleEventBus("SimpleEventBus")
		;
		private final String text;
		private WidgetAttribute (final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}

	public enum ParameterName {
		  EventBusName("EventBusName")
		, EventBusScope("EventBusScope")
		, InitDelayMS("InitDelayMS")
		, ScsEnvIds("ScsEnvIds")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ParameterValue {
		  Global("Global")
		;
		private final String text;
		private ParameterValue(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
