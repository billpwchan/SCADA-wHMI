package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

public interface PTW_i {
	enum PTW {
		Operation("operation")
		, OperationDetail1("operationdetail1")
		, OperationDetail2("operationdetail2")
		;
		private final String text;
		private PTW(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	enum PTWOperation {
		Filter("filter")
		, Selection("selection")
		;
		private final String text;
		private PTWOperation(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	enum PTWOperationDetail {
		NAN("nan")
		, Set("set")
		, UnSet("unset")
		;
		private final String text;
		private PTWOperationDetail(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
