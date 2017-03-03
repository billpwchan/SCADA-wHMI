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
	
	public enum BrcTableIndex {
		number(0)
		, brctype(1)
		, label(2)
		, inhibflag(3)
		, exestatus(4)
		, succns(5)
		, succdelay(6)
		, failns(7)
		, faildelay(8)
		, enveqp(9)
		, eqp(10)
		, cmdname(11)
		, cmdval(12)
		, cmdlabel(13)
		, cmdtype(14)
		, maxretry(15)
		, bpretcond(16)
		, bpinitcond(17)
		, grcname(18)
		, sndbehavr(19);
		
		private final int value;
		private BrcTableIndex(final int value) { this.value = value; }
		public int getValue() { return this.value; }
		@Override
		public String toString() { return Integer.toString(this.value); }
	}
}
