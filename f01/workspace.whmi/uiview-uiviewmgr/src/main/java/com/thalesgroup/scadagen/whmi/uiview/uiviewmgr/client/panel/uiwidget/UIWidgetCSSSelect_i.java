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
		
		, CSSElementName2("CSSElementName2")
		, CSSValueApplyToElement2("CSSValueApplyToElement2")
		, CSSValueRemoveFromElement2("CSSValueRemoveFromElement2")
		
		, CSSElementName3("CSSElementName3")
		, CSSValueApplyToElement3("CSSValueApplyToElement3")
		, CSSValueRemoveFromElement3("CSSValueRemoveFromElement3")
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
		,  CSSSelect0("CSSSelect0")
		, CSSSelect1("CSSSelect1")
		, CSSSelect2("CSSSelect2")
		, CSSSelect3("CSSSelect3")
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
