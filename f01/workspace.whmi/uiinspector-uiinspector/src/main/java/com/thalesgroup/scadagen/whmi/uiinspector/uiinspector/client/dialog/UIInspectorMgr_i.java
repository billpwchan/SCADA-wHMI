package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dialog;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;

public interface UIInspectorMgr_i {
	
	public final static String STR_DICTIONARIES_CACHE_NAME = UIInspector_i.strUIInspector;
	public final static String STR_INSP_DIALOGBOX_PROP_PREFIX = "inspectorpaneldialogbox.";
	public final static String STR_INSP_DIALOGBOX_PROP = STR_INSP_DIALOGBOX_PROP_PREFIX+"properties";
	
	public final static String STR_DATABASE_MULTI_READING_KEY = "DatabaseMultiReadingKey";
	public final static String STR_DATABASE_PATH_PATTERN = "DatabasePathPattern";
	
	public enum DatabasePathPattern {
		  C1166B("1166B")
		;
		private final String text;
		private DatabasePathPattern(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
}
