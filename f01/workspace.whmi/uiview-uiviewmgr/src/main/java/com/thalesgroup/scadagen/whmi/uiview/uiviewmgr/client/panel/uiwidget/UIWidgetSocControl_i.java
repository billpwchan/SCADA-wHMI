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
		
		, ReserveAttributeReserveReqID("ReserveAttributeReserveReqID")
		, ReserveAttributeUnreserveReqID("ReserveAttributeUnreserveReqID")
		, ReserveAttributeReservedID("ReserveAttributeReservedID")
		
		, ReserveAttributeName("ReserveAttributeName")
		, ReserveAttributeType("ReserveAttributeType")
		, ReservedValueStr("ReservedValueStr")
		, UnreservedValueStr("UnreservedValueStr")
		
		, MaxReserveRetry("MaxReserveRetry")
		
		, MessageDatetimeFormat("MessageDatetimeFormat")
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
	
	public enum MessageTranslationID {
		E_Reserve_fail_unable_launch_grc("&SCADAGEN_SOC_UIWidgetSocControl_Reserve_fail_unable_launch_grc")
		, E_grc_abort_update_error("&SCADAGEN_SOC_UIWidgetSocControl_grc_abort_update_error")
		, E_grc_result_update_error("&SCADAGEN_SOC_UIWidgetSocControl_grc_result_update_error")
		, E_grc_launch_result_error("&SCADAGEN_SOC_UIWidgetSocControl_grc_launch_result_error")
		, E_grc_prepare_result_error("&SCADAGEN_SOC_UIWidgetSocControl_grc_prepare_result_error")
		, E_grc_step_result_error("&SCADAGEN_SOC_UIWidgetSocControl_grc_step_result_error")
		, E_rtdb_read_error("&SCADAGEN_SOC_UIWidgetSocControl_rtdb_read_error")
		, E_SOC_incompleted_failed_steps("&SCADAGEN_SOC_UIWidgetSocControl_SOC_incompleted_failed_steps")
		, E_SOC_completed_failed_steps("&SCADAGEN_SOC_UIWidgetSocControl_SOC_completed_failed_steps")
		, E_SOC_incompleted_no_failed_steps("&SCADAGEN_SOC_UIWidgetSocControl_SOC_incompleted_no_failed_steps")
		, E_SOC_completed_no_failed_steps("&SCADAGEN_SOC_UIWidgetSocControl_SOC_completed_no_failed_steps")
		;
		private final String text;
		private MessageTranslationID(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
