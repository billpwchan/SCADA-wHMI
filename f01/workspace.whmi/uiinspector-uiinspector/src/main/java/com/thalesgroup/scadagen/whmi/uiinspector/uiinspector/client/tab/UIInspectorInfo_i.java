package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

public interface UIInspectorInfo_i {
	
	public final static String INSPECTOR = "inspector";

	public enum Attribute {
		;
		private final String text;
		private Attribute(final String text) { this.text = text; }
	//	public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}
