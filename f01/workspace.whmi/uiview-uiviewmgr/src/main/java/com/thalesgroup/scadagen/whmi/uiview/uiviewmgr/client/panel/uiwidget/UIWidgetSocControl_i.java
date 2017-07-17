package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocControl_i {	
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus")
		, TargetDataGrid_A("TargetDataGrid_A")
		, TargetDataGridColumn_A("TargetDataGridColumn_A")
		, TargetDataGridColumn_A2("TargetDataGridColumn_A2")
		, TargetDataGridColumn_A3("TargetDataGridColumn_A3")
		, TargetDataGrid_B("TargetDataGrid_B")
		, TargetDataGridColumn_B("TargetDataGridColumn_B")
		, TargetDataGridColumn_B2("TargetDataGridColumn_B2")
		, TargetDataGridColumn_B3("TargetDataGridColumn_B3")
		, StartElement("StartElement")
		, StopElement("StopElement")
		, RetryElement("RetryElement")
		, SkipElement("SkipElement")
		, Name_resrvReserveReqID("Name_resrvReserveReqID")
		, Name_resrvUnreserveReqID("Name_resrvUnreserveReqID")
		, Name_resrvReservedID("Name_resrvReservedID")
		, ReserveAttributeName("ReserveAttributeName")
		, ReserveAttributeType("ReserveAttributeType")
		, ReservedValueStr("ReservedValueStr")
		, UnreservedValueStr("UnreservedValueStr")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum SocControlEvent {
		  ReadGrcStepStatus("ReadGrcStepStatus");
		private final String text;
		private SocControlEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum CtlBrcStatus {
		NotExecuted(1)
		,Completed(2)
		,Failed(3)
		,Skipped(4);
		
		private int value;
		private CtlBrcStatus(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
}
