package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionAlm_i {
	public enum UIEventActionAlmAction {
		  NotifyExternalAlarm("NotifyExternalAlarm")
		, NotifyExternalEvent("NotifyExternalEvent")
		;
		private final String text;
		private UIEventActionAlmAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
