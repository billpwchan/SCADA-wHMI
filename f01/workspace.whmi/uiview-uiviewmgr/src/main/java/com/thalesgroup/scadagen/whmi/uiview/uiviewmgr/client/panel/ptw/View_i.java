package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

public interface View_i
{
	enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, MwtEventBus("MwtEventBus")
		, ListConfigId("ListConfigId")
		, MenuEnable("MenuEnable")
		, SelectionMode("SelectionMode")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	enum ViewWidget {
		ScsOlsListPanel("ScsOlsListPanel")
		;
		private final String text;
		private ViewWidget(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	enum ViewAttribute {
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

	enum PrintViewEvent {
		Print("Print")
		;
		private final String text;
		private PrintViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	enum SummaryViewEvent {
		SetDefaultFilter("SetDefaultFilter")
		;
		private final String text;
		private SummaryViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	enum ViewerViewEvent {
		FilterAdded("FilterAdded")
		, FilterRemoved("FilterRemoved")
		, RowSelected("RowSelected")
		;
		private final String text;
		private ViewerViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	enum FilterViewEvent {
		AddFilter("AddFilter")
		, RemoveFilter("RemoveFilter")
		;
		private final String text;
		private FilterViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from Control
	enum ControlViewEvent {
		;
		private final String text;
		private ControlViewEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
