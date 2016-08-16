package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

public class UIWidgetUtil {
	public static String getClassSimpleName(String name) {
		return getClassSimpleName(name, true);
	}
	public static String getClassSimpleName(String name, boolean withExt) {
		char strDot = '.';
		int firstChar, lastChar;
		firstChar = name.lastIndexOf (strDot) + 1;
		if ( firstChar > 0 ) {
			name = name.substring ( firstChar );
		}
		if ( ! withExt ) {
			lastChar = name.lastIndexOf(strDot) - 1;
			if ( lastChar > 0 ) {
				name = name.substring ( 0, lastChar );
			}
		}
		return name;
	}
}
