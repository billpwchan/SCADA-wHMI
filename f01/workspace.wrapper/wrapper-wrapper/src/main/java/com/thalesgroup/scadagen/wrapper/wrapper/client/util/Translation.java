package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.MissingResourceException;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
//import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
//import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
//import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class Translation {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(Translation.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static final String JS_DICTIONARY_VAR_NAME = "table";
	private static final Dictionary dictionary = Dictionary.getDictionary(JS_DICTIONARY_VAR_NAME);
	
	private static String translatePatten = "&[0-9a-zA-Z/$_-]+";
	public static void setTranslatePatten(String translatePatten) { 
//		final String function = "setTranslatePatten";
//		logger.begin(className, function);
		if ( null != translatePatten ) Translation.translatePatten = translatePatten;
//		logger.trace(className, function, "Translation.translatePatten[{}]", Translation.translatePatten);
//		logger.end(className, function);
	}
	public static String getTranslatePatten() {
//		final String function = "setTranslatePatten";
//		logger.beginEnd(className, function, "translatePatten[{}]", translatePatten);
		return Translation.translatePatten;
	}

	private static String translateFlag = "g";
	public static void setTranslateFlag(String translateFlag) { 
//		final String function = "setTranslateFlag";
//		logger.begin(className, function);
		if ( null != translateFlag ) Translation.translateFlag = translateFlag;
//		logger.trace(className, function, "Translation.translateFlag[{}]", Translation.translateFlag);
//		logger.end(className, function);
	}
	public static String getTranslateFlag() {
//		final String function = "getTranslateFlag";
//		logger.beginEnd(className, function, "translateFlag[{}]", translateFlag);
		return Translation.translateFlag;
	}
	
	public static String getWording(String key) {
		final String function = "getWording";
		logger.trace(className, function, "getWording key[{}]", key);
		String value = key;
        try {
            if (dictionary != null) value = dictionary.get(key);
        }
        catch (final MissingResourceException e) {
        	logger.debug(className, function, "Can't find key [{}] in dictionary", key);
        }
        return value;
	}
	
	public static String getDBMessage(String input) {
//		final String function = "getDBMessage";
//		logger.trace(className, function, "input[{}]", input);
		String result = Translation.getDBMessage(Translation.translatePatten, Translation.translateFlag, input);
//		logger.trace(className, function, "result[{}]", result);
		return result;
	}

	public static String getDBMessage(String regex, String flag, String inputStr) {
//		final String function = "getDBMessage";
//		logger.trace(className, function, "regex[{}] flag[{}] inputStr[{}]", new Object[]{regex, flag, inputStr});
		String ret = inputStr;
		if ( null != ret && ! ret.isEmpty() ) {
			if ( null != regex && null != flag && null != inputStr ) {

				try {
					// Compile and use regular expression
					RegExp regExp = RegExp.compile(regex, flag);
					MatchResult matcher = regExp.exec(inputStr);
					if ( null != regExp ) {

						int i = 0;
						final int j = 99;
						while ( matcher != null && i < j ) {
							String groupStr = matcher.getGroup(0);
							
//							int getLastIndex = regExp.getLastIndex();
//							logger.trace(className, function, "getLastIndex[{}] groupStr[{}]", getLastIndex, groupStr);
							String translation = Translation.getWording(groupStr);
//							logger.trace(className, function, "getLastIndex[{}]  groupStr[{}] translation[{}]", new Object[]{getLastIndex, groupStr, translation});
							if ( null != translation ) ret = ret.replaceAll(groupStr, translation);

							matcher = regExp.exec(inputStr);
							++i;
						}
						
					}
				} catch ( RuntimeException e ) {
//					logger.warn(className, function, "RuntimeException[{}]", e.toString());
				}

			} else {
//				logger.warn(className, function, "regex[{}] OR flag[{}] OR inputStr[{}] IS NULL", new Object[]{regex, flag, inputStr});
			}
		} else {
//			logger.warn(className, function, "ret[{}] IS NULL OR Empty", ret);
		}

//		logger.trace(className, function, "ret[{}]", ret);
		return ret;
	}

}
