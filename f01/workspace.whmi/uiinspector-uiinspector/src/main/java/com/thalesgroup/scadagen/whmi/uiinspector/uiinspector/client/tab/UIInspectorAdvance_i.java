package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

public interface UIInspectorAdvance_i {
	
	public final static String INSPECTOR = "inspector";
	
	public enum Attribute {
		moApplyWithoutReset("moApplyWithoutReset")
		;
		private final String text;
		private Attribute(final String text) { this.text = text; }
	//	public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public final static int CHKBOX_INDEX_AI = 0;
	public final static int CHKBOX_INDEX_SS = 1;
	public final static int CHKBOX_INDEX_MO = 2;
	public final static int CHKBOX_INDEX_TOTAL = 3;
}
