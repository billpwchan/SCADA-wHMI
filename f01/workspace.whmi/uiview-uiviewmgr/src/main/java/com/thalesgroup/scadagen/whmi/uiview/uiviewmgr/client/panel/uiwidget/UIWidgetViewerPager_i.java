package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetViewerPager_i {
	public enum ParameterName {
		  HasPreviousPage("HasPreviousPage")
		, HasFastBackwardPage("HasFastBackwardPage")
		, HasFastForwardPage("HasFastForwardPage")
		, HasNextPage("HasNextPage")
		
		, PagerValueChanged_PageStart("PagerValueChanged_PageStart")
		, PagerValueChanged_EndIndex("PagerValueChanged_EndIndex")
		, PagerValueChanged_Exact("PagerValueChanged_Exact")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
