package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetCSSSelect_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, CSSElementName0("CSSElementName0")
		, CSSValueApplyToElement0("CSSValueApplyToElement0")
		, CSSValueRemoveFromElement0("CSSValueRemoveFromElement0")
		, CSSElementName1("CSSElementName1")
		, CSSValueApplyToElement1("CSSValueApplyToElement1")
		, CSSValueRemoveFromElement1("CSSValueRemoveFromElement1")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	// Send from CSSApply
	public enum CSSSelectEvent {
		CSSApply("CSSApply")
		, CSSRemove("CSSRemove")
		;
		private final String text;
		private CSSSelectEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Receive by CSS Apply
	public enum CSSSelectViewEvent {
		SetDefaultCSS("SetDefaultCSS")
		;
		private final String text;
		private CSSSelectViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
