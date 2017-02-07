package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

public interface SocCardDetail_i {
	public enum SocCardDetailParameter {
		BrcTableIndexes("BrcTableIndexes")
		, ExtraRelativePoints("ExtraRelativePoints")
		;
		private final String text;
		private SocCardDetailParameter(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
