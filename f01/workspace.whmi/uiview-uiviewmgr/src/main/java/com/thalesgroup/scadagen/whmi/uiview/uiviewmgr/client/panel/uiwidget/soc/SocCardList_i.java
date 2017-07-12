package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

public interface SocCardList_i {
	public enum SocCardListParameter {
		ColumnValueFilters("ColumnValueFilters")
		, GrcPointAttributes("GrcPointAttributes")
		, ColLblVals("ColLblVals")
		, CheckCardOPM("CheckCardOPM")
		, CheckOPMScope("CheckOPMScope")
		, CheckOPMMode("CheckOPMMode");

		private final String text;
		private SocCardListParameter(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
