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

/**
 * Class to handle the i18n translation in client side
 * Support the RegExp Pattern and RegExp Flag to replace the keyword
 * 
 * @author syau
 *
 */
public class Translation {
	
	private static final String className = Translation.class.getSimpleName();
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(Translation.class.getName());
	
	private static final String JS_DICTIONARY_VAR_NAME = "table";
	private static final Dictionary dictionary = Dictionary.getDictionary(JS_DICTIONARY_VAR_NAME);
	
	private static String translatePattern = "&[0-9a-zA-Z/$_-]+";
	/**
	 * Set the Translate RegExp Pattern
	 * 
	 * @param translatePattern RegExp Pattern for the Translate Mapping
	 */
	public static void setTranslatePattern(String translatePattern) { 
//		final String function = "setTranslatePattern";
//		logger.begin(className, function);
		if ( null != translatePattern ) Translation.translatePattern = translatePattern;
//		logger.trace(className, function, "Translation.translatePattern[{}]", Translation.translatePattern);
//		logger.end(className, function);
	}
	/**
	 * Get the Translate RegExp Pattern
	 * 
	 * @return translatePattern RegExp Pattern for the Translate Mapping
	 */
	public static String getTranslatePattern() {
//		final String function = "getTranslatePattern";
//		logger.beginEnd(className, function, "translatePattern[{}]", translatePattern);
		return Translation.translatePattern;
	}

	private static String translateFlag = "g";
	/**
	 * Set the Translate RegExp Flag
	 * 
	 * @param translateFlag Translation Flag for the RegExp Flag
	 */
	public static void setTranslateFlag(String translateFlag) { 
//		final String function = "setTranslateFlag";
//		logger.begin(className, function);
		if ( null != translateFlag ) Translation.translateFlag = translateFlag;
//		logger.trace(className, function, "Translation.translateFlag[{}]", Translation.translateFlag);
//		logger.end(className, function);
	}
	/**
	 * Get the Translate RegExp Flag
	 * 
	 * @return Translation Flag for the RegExp Flag
	 */
	public static String getTranslateFlag() {
//		final String function = "getTranslateFlag";
//		logger.beginEnd(className, function, "translateFlag[{}]", translateFlag);
		return Translation.translateFlag;
	}
	
	/**
	 * Using i18n to mapping the input string without Translation Pattern
	 * 
	 * @param inputStr String to map
	 * @return Mapped result from the i18n mapping, otherwise return original string
	 */
	public static String getWording(String inputStr) {
		final String function = "getWording";
		logger.trace(className, function, "getWording inputStr[{}]", inputStr);
		String value = inputStr;
        try {
            if (dictionary != null) value = dictionary.get(inputStr);
        }
        catch (final MissingResourceException e) {
        	logger.debug(className, function, "Can't find inputStr[{}] in dictionary", inputStr);
        }
        return value;
	}
	
	/**
	 * Using i18n to mapping the input string with Translation Pattern
	 * 
	 * @param inputStr String to map
	 * @return Mapped pattern will be replace, otherwise keep the original word and return
	 */
	public static String getDBMessage(String inputStr) {
//		final String function = "getDBMessage";
//		logger.trace(className, function, "inputStr[{}]", inputStr);
		String ret = Translation.getDBMessage(Translation.translatePattern, Translation.translateFlag, inputStr);
//		logger.trace(className, function, "inputStr[{}] ret[{}]", inputStr, ret);
		return ret;
	}


	/**
	 * Using i18n to mapping the input string with Translation Pattern
	 * 
	 * @param regex Translate RegExp Pattern
	 * @param flag Translate RegExp Flag
	 * @param inputStr String to map
	 * @return  Mapped pattern will be replace, otherwise keep the original word and return
	 * @return
	 */
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
