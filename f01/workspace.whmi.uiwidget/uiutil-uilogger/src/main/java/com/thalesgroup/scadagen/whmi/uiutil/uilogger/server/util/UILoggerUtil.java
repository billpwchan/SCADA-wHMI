package com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.util;

public class UILoggerUtil {
	public static String getClassSimpleName(String className) { return getClassSimpleName(className, true); }
	public static String getClassSimpleName(String className, boolean withExt) {
		char strDot = '.';
		int firstChar, lastChar;
		firstChar = className.lastIndexOf (strDot) + 1;
		if ( firstChar > 0 ) {
			className = className.substring ( firstChar );
		}
		if ( ! withExt ) {
			lastChar = className.lastIndexOf(strDot) - 1;
			if ( lastChar > 0 ) {
				className = className.substring ( 0, lastChar );
			}
		}
		return className;
	}
}
