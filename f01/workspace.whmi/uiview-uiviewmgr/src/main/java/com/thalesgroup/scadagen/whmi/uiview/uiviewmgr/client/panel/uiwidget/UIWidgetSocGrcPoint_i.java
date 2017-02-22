package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSocGrcPoint_i {
	public enum ParameterName {
		SimpleEventBus("SimpleEventBus");

		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	// Event Send from View
	public enum GrcPointEvent {
		  CurStep("GrcCurStep")
		, CurStatus("GrcCurStatus")
		, StepStatus("StepStatus")
		, DisplayMessage("DisplayMessage")
		;
		private final String text;
		private GrcPointEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
//	public enum GrcState {
//		NotExisting(0)
//		,Ready(1)
//		,Running(2)
//		,Inhibited(3)
//		,WaitForInitCond(4)
//		,Suspended(5);
//		
//		private int value;
//		private GrcState(int value) {
//			this.value = value;
//		}
//		public int getValue() {
//			return value;
//		}
//	}
	
	public enum GrcExecStatus {
		Terminated(1)
		,WaitForRun(2)
		,Initializing(3)
		,Running(4)
		,Waiting(5)
		,Stopped(6)
		,Aborted(7)
		,Suspended(8)
		,Resumed(9);
		
		private int value;
		private GrcExecStatus(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
}
