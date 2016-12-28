package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.MissingResourceException;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class Translation {
//	private static final String className = UIWidgetUtil.getClassSimpleName(Translation.class.getName());
//	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private static final String JS_DICTIONARY_VAR_NAME = "table";
	private static final Dictionary dictionary = Dictionary.getDictionary(JS_DICTIONARY_VAR_NAME);
	
	private static String translatePatten = "&[0-9a-zA-Z/$_-]+";
	public void setTranslatePatten(String translatePatten) { Translation.translatePatten = translatePatten; }
	public String getTranslatePatten() { return Translation.translatePatten;}
	
	public static String getWording(String key) {
//		final String function = "getWording";
//		logger.trace(className, function, "getWording key[{}]", key);
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
//		logger.trace(className, function, "input[{}]", input);
		String result = Translation.getDBMessage(Translation.translatePatten, input);
//		logger.trace(className, function, "result[{}]", result);
		return result;
	}
	
	public static String getDBMessage(String regex, String input) {
//		final String function = "getDBMessage";
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
					String translation = Translation.getWording(key);		
//					logger.trace(className, function, "key[{}] translation[{}]", new Object[]{key, translation});
					if ( null != translation ) {
						ret = ret.replaceAll(key, translation);
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
