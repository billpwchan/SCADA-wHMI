package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionWidget_i {
	public enum UIEventActionWidgetAction {
		  SetWidgetStatus("SetWidgetStatus")
		, SetWidgetValue("SetWidgetValue")
		, SetWidgetTitle("SetWidgetTitle")
		, SetWidgetFocus("SetWidgetFocus")
		, SetWidgetCSS("SetWidgetCSS")
		, AddWidgetCSS("AddWidgetCSS")
		, RemoveWidgetCSS("RemoveWidgetCSS")
		;
		private final String text;
		private UIEventActionWidgetAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
