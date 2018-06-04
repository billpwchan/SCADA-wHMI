package com.thalesgroup.scadagen.wrapper.wrapper.server.translation;

import com.thalesgroup.hypervisor.mwt.core.util.common.session.ISessionListContainer;
import com.thalesgroup.hypervisor.mwt.core.util.common.session.SessionContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.DictionaryManager;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class to handle the i18n translation in server side
 * Support the RegExp Pattern and RegExp Flag to replace the keyword
 * 
 * @author syau
 * 
 */
public class Translation {
	
	private final static UILogger_i logger = UILoggerFactory.getInstance().get(Translation.class.getName());
	
	private final static String strTranslatePattern	= "Translation_TranslatePatten_Server";
	private final static String strTranslateFlag	= "Translation_TranslateFlag_Server";
	
	private static String translatePattern = "&[0-9a-zA-Z/$_-]+";
	/**
	 * Set the Translate RegExp Pattern
	 * 
	 * @param translatePattern RegExp Pattern for the Translate Mapping
	 */
	public static void setTranslatePattern(String translatePattern) { Translation.translatePattern = translatePattern; }
	/**
	 * Get the Translate RegExp Pattern
	 * 
	 * @return translatePattern RegExp Pattern for the Translate Mapping
	 */
	public static String getTranslatePattern() { return Translation.translatePattern;}
	
	private static int translateFlag = 0;
	/**
	 * Set the Translate RegExp Flag
	 * 
	 * @param translateFlag Translation Flag for the RegExp Flag
	 */
	public static void setTranslateFlag(int translateFlag) { Translation.translateFlag = translateFlag; }
	/**
	 * Get the Translate RegExp Flag
	 * 
	 * @return Translation Flag for the RegExp Flag
	 */
	public static int getTranslateFlag() { return Translation.translateFlag;}
	
	private static boolean reloaded = false;
	/**
	 * Load the Configuration from the locate setting
	 * 
	 * @param sessionId Current Session ID
	 */
	private static void loadConfig(String sessionId) {
		final String function = "loadConfig";
		String tmpTranslatePattern = getWording(sessionId, strTranslatePattern);
		if ( null != tmpTranslatePattern && ! tmpTranslatePattern.equals(strTranslatePattern)) {
			setTranslatePattern(tmpTranslatePattern); 
		} else {
			logger.warn("{} sessionId[{}] strTranslatePattern[{}] tmpTranslatePattern[{}] IS NULL", new Object[]{function, sessionId, strTranslatePattern, tmpTranslatePattern});
		}
		logger.trace("{} getTranslatePattern[{}]", new Object[]{function, getTranslatePattern()});
		
		String tmpTranslateFlag = getWording(sessionId, strTranslateFlag);
		if ( null != tmpTranslateFlag && ! tmpTranslateFlag.equals(strTranslateFlag)) {
			try {
				setTranslateFlag(Integer.parseInt(tmpTranslateFlag)); 
			} catch ( NumberFormatException ex ) {
				logger.warn("sessionId[{}] strTranslateFlag[{}]", sessionId, strTranslateFlag);
			}
		} else {
			logger.warn("{} strTranslateFlag[{}] tmpTranslateFlag[{}] IS NULL", new Object[]{function, strTranslateFlag, tmpTranslateFlag});
		}
		reloaded = true;
		logger.trace("{} getTranslateFlag[{}]", new Object[]{function, getTranslateFlag()});
	}

	public static final String UNDEFINED_WORDING = "#Undefined#";
	/**
	 * Using i18n to mapping the input string without Translation Pattern
	 * 
	 * @param sessionId Current session ID
	 * @param inputStr String to map
	 * @return Mapped result from the i18n mapping, otherwise return original string
	 */
	public static String getWording(String sessionId, String inputStr) {
		final String function = "getWording";
		logger.trace("{} inputStr[{}]", function, inputStr);
		String value = inputStr;
        try {
        	
    		ISessionListContainer sessionListContainer = ServicesImplFactory.getInstance().getService(ISessionListContainer.class);
//    		String sessionId = operatorOpmInfo.getSessionId();
    		SessionContainer sessionContainer = sessionListContainer.getSessionContainer(sessionId);
        	
    		// Retrieve HV dictionary depending current language
    		Dictionary dico = null;
    		if (sessionContainer != null) {
    			String currentLang = sessionContainer.getAttributeTyped(String.class, "currentLang");
    			if (currentLang != null) {
    	        	dico = DictionaryManager.getInstance().getDictionary(currentLang);     
    	        	if ( null != dico ) {
    	        		value = dico.getWording(inputStr);
    	        	} else {
    	        		logger.error("{} dico IS NULL", function);
    	        	}
    			}
    			else {
    				logger.error("{} Current lang is null for session id {}", function, sessionId);
    			}
    		}
    		else {
    			logger.error("{} Cannot find container for session id {}", function, sessionId);
    		}
 
        }
        catch (final MissingResourceException e) {
        	logger.warn("{} Can't find key [{}] in dictionary MissingResourceException[{}]", new Object[]{inputStr, e.toString()});
        }
        
        // Try to remove the UNDEFINED_WORDING prefix when string not found
        if ( value.length() == UNDEFINED_WORDING.length()+inputStr.length() ) {
        	if ( value.equals(UNDEFINED_WORDING+inputStr)) {
        		value = inputStr;
        	}
        }
        
        return value;
	}
	
	/**
	 * Using i18n to mapping the input string with Translation Pattern
	 * 
	 * @param sessionId Current session ID
	 * @param inputStr String to map
	 * @return Mapped pattern will be replace, otherwise keep the original word and return
	 */
	public static String getDBMessage(String sessionId, String inputStr) {
		final String function = "getDBMessage";
		logger.trace("{} inputStr[{}]", new Object[]{function, inputStr});
		String ret = inputStr;
		// Skip the empty String to enhance the efficiency
		if ( null != inputStr && ! inputStr.isEmpty() ) {
			if ( ! reloaded ) loadConfig(sessionId);
			ret = Translation.getDBMessage(sessionId, getTranslatePattern(), getTranslateFlag(), inputStr);
		}
		logger.trace("{} inputStr[{}] ret[{}]", new Object[]{function, inputStr, ret});
		return ret;
	}
	
	/**
	 * Using i18n to mapping the input string with Translation Pattern
	 * 
	 * @param sessionId Current session ID
	 * @param regex Translate RegExp Pattern
	 * @param flag Translate RegExp Flag
	 * @param inputStr String to map
	 * @return  Mapped pattern will be replace, otherwise keep the original word and return
	 */
	public static String getDBMessage(String sessionId, String regex, int flag, String inputStr) {
		final String function = "getDBMessage";
		logger.trace("{} regex[{}] flag[{}] inputStr[{}]", new Object[]{function, regex, flag, inputStr});
		String ret = inputStr;
		try {
			Pattern p = Pattern.compile(regex, flag);
			Matcher m = p.matcher(inputStr);
			while(m.find()) {
				String key = m.group();
				logger.trace("{} key[{}]", function, key);
				String translation = Translation.getWording(sessionId, key);
				
				logger.trace("{} key[{}] translation[{}]", new Object[]{function, key, translation});
				if ( null != translation ) {
					ret = ret.replaceAll(key, translation);
				}
			}
		} catch ( PatternSyntaxException e ) {
			logger.warn("{} PatternSyntaxException[{}]", function, e.toString());
		}
		logger.trace("{} regex[{}] flag[{}] inputStr[{}] ret[{}]", new Object[]{function, regex, flag, inputStr, ret});
		return ret;
	}
}
