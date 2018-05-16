package com.thalesgroup.scadagen.whmi.uiutil.uiutil.client;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class UIWidgetUtil {

	public static String replaceKeyword(String input, String regex, String replace) {
//		String function = "replaceKeyword";
//		logger.trace(function, "regex[{}] input[{}]", new Object[]{regex, input});
		String ret = input;
		try {
			RegExp regExp = RegExp.compile(regex);
			MatchResult matcher = regExp.exec(input);
			boolean matchFound = matcher != null;
			if ( matchFound) {
//				logger.trace(function, "matcher.getGroupCount()[{}]", matcher.getGroupCount());
				for ( int i=0; i < matcher.getGroupCount(); ++i) {
					String key = matcher.getGroup(i);
//					logger.trace(function, "matcher.getGroup([{}])[{}]", i, key);
//					String translation = Translation.getWording(key);
//					logger.trace(function, "key[{}] translation[{}]", new Object[]{key, translation});
					if ( null != replace ) {
						ret = ret.replaceAll(key, replace);
					}
				}
			}
		} catch ( RuntimeException e ) {
//			logger.warn(function, "RuntimeException[{}]", e.toString());
		}
//		logger.trace(function, "ret[{}]", ret);
		return ret;
	}
	public static short getShort(int intValue) {
//		String function = "getShort";
//		logger.begin(function);
//		logger.info(function, "intValue[{}]", intValue);
		short shortValue = 0;
		shortValue = (short)min(max(intValue, Short.MIN_VALUE), Short.MAX_VALUE);
//		logger.end(function);
		return shortValue;
	}
	public static String [] getStringArray ( String strArray, String split ) {
//		String function = "getStringArray";
//		logger.begin(function);
//		logger.info(function, "strArray[{}]", strArray);
		String [] result = null;
		if ( null != strArray ) {
			String [] strArrays = strArray.split(split);
			if ( null != strArrays ) {
				result = new String[strArrays.length];
				for ( int i = 0 ; i < strArrays.length ; ++i ) {
					result[i] = strArrays[i];
				}
			} else {
//				logger.warn(function, "strArrays IS NULL");
			}
		} else {
//			logger.warn(function, "strArray IS NULL");
		}
//		logger.end(function);
		return result;
	}
	
	public static int [] getIntArray ( String strArray, String split ) {
//		String function = "getIntArray";
//		logger.begin(function);
//		logger.info(function, "strArray[{}]", strArray);
		int [] result = null;
		if ( null != strArray ) {
			String [] strArrays = strArray.split(split);
			if ( null != strArrays ) {
				result = new int[strArrays.length];
				try {
					for ( int i = 0 ; i < strArrays.length ; ++i ) {
						result[i] = Integer.parseInt(strArrays[i]);
					}
				} catch ( NumberFormatException ex ) {
//					logger.warn(function, "strArrays[{}] IS INVALID", strArray);
				}
			} else {
//				logger.warn(function, "strArrays IS NULL");
			}
		} else {
//			logger.warn(function, "strArray IS NULL");
		}
//		logger.end(function);
		return result;
	}

	public static short [] getShortArray ( String strArray, String split ) {
//		String function = "getShortArray";
//		logger.begin(function);
//		logger.info(function, "strArray[{}]", strArray);
		short [] result = null;
		if ( null != strArray ) {
			String [] strArrays = strArray.split(split);
			if ( null != strArrays ) {
				result = new short[strArrays.length];
				try {
					for ( int i = 0 ; i < strArrays.length ; ++i ) {
						result[i] = Short.parseShort(strArrays[i]);
					}
				} catch ( NumberFormatException ex ) {
//					logger.warn(function, "strArrays[{}] IS INVALID", strArray);
				}
			} else {
//				logger.warn(function, "strArrays IS NULL");
			}
		} else {
//			logger.warn(function, "strArray IS NULL");
		}
//		logger.end(function);
		return result;
	}
}
