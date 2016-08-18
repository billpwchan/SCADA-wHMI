package com.thalesgroup.scadagen.whmi.uiutil.uiutil.client;

public class UIWidgetUtil {
	public static String getLogPrefix(String className) {
		return "["+className+"] ";
	}
	public static String getClassSimpleName(String className) {
		return getClassSimpleName(className, true);
	}
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
