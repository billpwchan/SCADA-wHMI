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
		eventbusname("eventbusname")
		, eventbusscope("eventbusscope")
		, initdelayms("initdelayms")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	public enum ViewWidget {
		UIWidgetViewer("UIWidgetViewer")
		, UIWidgetAction("UIWidgetAction")
		, UIWidgetCtlControl("UIWidgetCtlControl")
		, UIWidgetDpcControl("UIWidgetDpcControl")
		, UIWidgetFilter("UIWidgetFilter")
		, UIWidgetPrint("UIWidgetPrint")
		, ScsOlsListPanel("ScsOlsListPanel")
		, UIWidgetCSSFilter("UIWidgetCSSFilter")
		, UIWidgetCSSSelect("UIWidgetCSSSelect")
		;
		private final String text;
		private ViewWidget(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	public enum SummaryViewEvent {
		 FireUIEventAction("FireUIEventAction")
		;
		private final String text;
		private SummaryViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
