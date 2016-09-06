package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIView_i
{
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, MwtEventBus("MwtEventBus")
		
		, ListConfigId("ListConfigId")
		, MenuEnable("MenuEnable")
		, SelectionMode("SelectionMode")
		
		, FilterColumn("FilterColumn")
		, FilterValueSet0("FilterValueSet0")
		, FilterValueSet1("FilterValueSet1")
		
		, ColumnAlias("ColumnAlias")
		, ColumnStatus("ColumnStatus")
		, ColumnServiceOwner("ColumnServiceOwner")
		
		, ValueSet("ValueSet")
		, ValueUnSet("ValueUnSet")
		
		
		, CSSElementName0("CSSElementName0")
		, CSSValueApplyToElement0("CSSValueApplyToElement0")
		, CSSValueRemoveFromElement0("CSSValueRemoveFromElement0")
		
		, CSSElementName1("CSSElementName1")
		, CSSValueApplyToElement1("CSSValueApplyToElement1")
		, CSSValueRemoveFromElement1("CSSValueRemoveFromElement1")
		
		//
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
	
	public enum ViewAttribute {
		Operation("operation")
		, OperationString1("operationstring1")
		, OperationString2("operationstring2")
		, OperationString3("operationstring3")
		, OperationObject1("operationobject1")
		, OperationObject2("operationobject2")
		, OperationObject3("operationobject3")
		;
		private final String text;
		private ViewAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}

	public enum PrintViewEvent {
		Print("Print")
		;
		private final String text;
		private PrintViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum SummaryViewEvent {
		SetDefaultFilter("SetDefaultFilter")
		;
		private final String text;
		private SummaryViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum ViewerViewEvent {
		FilterAdded("FilterAdded")
		, FilterRemoved("FilterRemoved")
		, RowSelected("RowSelected")
		;
		private final String text;
		private ViewerViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum FilterViewEvent {
		AddFilter("AddFilter")
		, RemoveFilter("RemoveFilter")
		;
		private final String text;
		private FilterViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from Control
	public enum ControlViewEvent {
		;
		private final String text;
		private ControlViewEvent(final String text) { this.text = text; }
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
