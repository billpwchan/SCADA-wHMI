package com.thalesgroup.scadagen.whmi.uiutil.uiutil.client;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

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
	public static String replaceKeyword(String input, String regex, String replace) {
//		String function = "replaceKeyword";
//		logger.trace(className, function, "regex[{}] input[{}]", new Object[]{regex, input});
		String ret = input;
		try {
			RegExp regExp = RegExp.compile(regex);
			MatchResult matcher = regExp.exec(input);
			boolean matchFound = matcher != null;
			if ( matchFound) {
//				logger.trace(className, function, "matcher.getGroupCount()[{}]", matcher.getGroupCount());
				for ( int i=0; i < matcher.getGroupCount(); ++i) {
					String key = matcher.getGroup(i);
//					logger.trace(className, function, "matcher.getGroup([{}])[{}]", i, key);
//					String translation = Translation.getWording(key);
//					logger.trace(className, function, "key[{}] translation[{}]", new Object[]{key, translation});
					if ( null != replace ) {
						ret = ret.replaceAll(key, replace);
					}
				}
			}
		} catch ( RuntimeException e ) {
//			logger.warn(className, function, "RuntimeException[{}]", e.toString());
		}
//		logger.trace(className, function, "ret[{}]", ret);
		return ret;
	}
}
