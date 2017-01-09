package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionBusFire_i {
	public enum UIEventActionBusFireAction {
		;
		private final String text;
		private UIEventActionBusFireAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
