package com.thalesgroup.scadagen.wrapper.wrapper.server.translation;

import com.thalesgroup.hypervisor.mwt.core.util.common.session.ISessionListContainer;
import com.thalesgroup.hypervisor.mwt.core.util.common.session.SessionContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.DictionaryManager;

import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Translation {
	
	private final static Logger logger			= LoggerFactory.getLogger(Translation.class.getName());
	
	private final static String strTranslatePattern	= "Translation_TranslatePatten_Server";
	private final static String strTranslateFlag	= "Translation_TranslateFlag_Server";
	
	private static String translatePattern = "(^&\\S+)|\\s+(&\\S+)";
	public void setTranslatePatten(String translatePatten) { Translation.translatePattern = translatePatten; }
	public String getTranslatePatten() { return Translation.translatePattern;}
	
	private static int translateFlag = 0;
	public void setTranslateFlag(int translateFlag) { Translation.translateFlag = translateFlag; }
	public int getTranslateFlag() { return Translation.translateFlag;}
	
	private static void loadTranslationConfiguration(String sessionId) {
		String tmpTranslatePattern = getWording(sessionId, strTranslatePattern);
		if ( null != tmpTranslatePattern) {
			translatePattern = tmpTranslatePattern; 
		} else {
			logger.warn("sessionId[{}] strTranslatePattern[{}] tmpTranslatePattern[{}] IS NULL", new Object[]{sessionId, strTranslatePattern, tmpTranslatePattern});
		}
		logger.debug("sessionId[{}] translatePattern[{}]", sessionId, translatePattern);
		
		String tmpTranslateFlag = getWording(sessionId, strTranslateFlag);
		if ( null != tmpTranslateFlag) {
			try {
				translateFlag = Integer.parseInt(tmpTranslateFlag); 
			} catch ( NumberFormatException ex ) {
				logger.warn("sessionId[{}] strTranslateFlag[{}]", sessionId, strTranslateFlag);
			}
			
		} else {
			logger.warn("sessionId[{}] strTranslateFlag[{}] tmpTranslateFlag[{}] IS NULL", new Object[]{sessionId, strTranslateFlag, tmpTranslateFlag});
		}
		logger.debug("sessionId[{}] translateFlag[{}]", sessionId, translateFlag);
	}

	public static String getWording(String sessionId, String key) {
		logger.debug("getWording[{}]", key);
		String value = key;
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
    	        		value = dico.getWording(key);
    	        	} else {
    	        		logger.error("dico IS NULL");
    	        	}
    			}
    			else {
    				logger.error("Current lang is null for session id {}", sessionId);
    			}
    		}
    		else {
    			logger.error("Cannot find container for session id {}", sessionId);
    		}
 
        }
        catch (final MissingResourceException e) {
        	logger.warn("Can't find key [{}] in dictionary MissingResourceException[{}]", key, e.toString());
        }
        return value;
	}
	
	public static String getDBMessage(String sessionId, String input) {
		loadTranslationConfiguration(sessionId);
		return Translation.getDBMessage(sessionId, translatePattern, translateFlag, input);
	}
	
	public static String getDBMessage(String sessionId, String regex, int flag, String inputStr) {
		logger.trace("{} regex[{}] input[{}]", new Object[]{"getDBMessage", regex, inputStr});
		String ret = inputStr;
		try {
			Pattern p = Pattern.compile(regex, flag);
			Matcher m = p.matcher(inputStr);
			while(m.find()) {
				String key = m.group();
				logger.trace("{} m.group()[{}]", "getDBMessage", key);
				String translation = Translation.getWording(sessionId, key);
				
				logger.trace("{} key[{}] translation[{}]", new Object[]{"getDBMessage", key, translation});
				if ( null != translation ) {
					ret = ret.replaceAll(key, translation);
				}
			}
		} catch ( PatternSyntaxException e ) {
			logger.warn("{} PatternSyntaxException[{}]", "getDBMessage", e.toString());
		}

		return ret;
	}
}
