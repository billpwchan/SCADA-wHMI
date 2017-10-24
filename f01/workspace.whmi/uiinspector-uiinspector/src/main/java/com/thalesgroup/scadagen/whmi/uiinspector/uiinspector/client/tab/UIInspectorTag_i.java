package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

public interface UIInspectorTag_i {
	
	public final static String INSPECTOR = "inspector";
	
	public enum Attribute {
		
		  dioValueTableLength("dioValueTableLength")
		, dioValueTableDovnameColIndex("dioValueTableDovnameColIndex")
		, dioValueTableValueColIndex("dioValueTableValueColIndex")
		, dioValueTableLabelColIndex("dioValueTableLabelColIndex")
		
		, aioValueTableLength("aioValueTableLength")
		, aioValueTableDovnameColIndex("aioValueTableDovnameColIndex")
		, aioValueTableValueColIndex("aioValueTableValueColIndex")
		, aioValueTableLabelColIndex("aioValueTableLabelColIndex")
		
		, sioValueTableLength("sioValueTableLength")
		, sioValueTableDovnameColIndex("sioValueTableDovnameColIndex")
		, sioValueTableValueColIndex("sioValueTableValueColIndex")
		, sioValueTableLabelColIndex("sioValueTableLabelColIndex")
		
		, byPassInitCond("byPassInitCond")
		, byPassRetCond("byPassRetCond")
		, sendAnyway("sendAnyway")
		
		;
		private final String text;
		private Attribute(final String text) { this.text = text; }
	//	public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}
