package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.MissingResourceException;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
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
		final String function = "setTranslatePatten";
		logger.begin(className, function);
		Translation.translatePatten = translatePatten;
		logger.debug(className, function, "Translation.translatePatten[{}]", Translation.translatePatten);
		logger.end(className, function);
	}
	public static String getTranslatePatten() {
		final String function = "setTranslatePatten";
		logger.beginEnd(className, function, "translatePatten[{}]", translatePatten);
		return Translation.translatePatten;
	}

	private static String translateFlag = "";
	public static void setTranslateFlag(String translateFlag) { 
		final String function = "setTranslateFlag";
		logger.begin(className, function);
		Translation.translateFlag = translateFlag;
		logger.debug(className, function, "Translation.translateFlag[{}]", Translation.translateFlag);
		logger.end(className, function);
	}
	public static String getTranslateFlag() {
		final String function = "getTranslateFlag";
		logger.beginEnd(className, function, "translateFlag[{}]", translateFlag);
		return Translation.translateFlag;
	}
	
	public static String getWording(String key) {
//		final String function = "getWording";
//		logger.debug(className, function, "getWording key[{}]", key);
		String value = key;
        try {
            if (dictionary != null) value = dictionary.get(key);
        }
        catch (final MissingResourceException e) {
//        	logger.warn(className, function, "Can't find key [{}] in dictionary", key);
        }
        return value;
	}
	
	public static String getDBMessage(String input) {
//		final String function = "getDBMessage";
//		logger.debug(className, function, "input[{}]", input);
		String result = Translation.getDBMessage(Translation.translatePatten, Translation.translateFlag, input);
//		logger.debug(className, function, "result[{}]", result);
		return result;
	}

	public static String getDBMessage(String regex, String flag, String input) {
//		final String function = "getDBMessage";
//		logger.debug(className, function, "regex[{}] flag[{}] input[{}]", new Object[]{regex, flag, input});
		String ret = input;
		try {
			RegExp regExp = RegExp.compile(regex, flag);
			MatchResult matcher = null;
			while ( (matcher=regExp.exec(input)) != null ) {
//				logger.debug(className, function, "matcher.getGroupCount()[{}]", matcher.getGroupCount());
				if ( matcher.getGroupCount() > 0 ) {
					String key = matcher.getGroup(matcher.getGroupCount()-1);
//					logger.debug(className, function, "[{}]", key);
					String translation = Translation.getWording(key);		
//					logger.debug(className, function, "key[{}] translation[{}]", new Object[]{key, translation});
					if ( null != translation ) ret = ret.replaceAll(key, translation);
				}
			}

		} catch ( RuntimeException e ) {
//			logger.warn(className, function, "RuntimeException[{}]", e.toString());
		}
//		logger.debug(className, function, "ret[{}]", ret);
		return ret;
	}

}
