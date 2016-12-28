package com.thalesgroup.scadagen.wrapper.wrapper.server;

import com.thalesgroup.hypervisor.mwt.core.util.common.session.ISessionListContainer;
import com.thalesgroup.hypervisor.mwt.core.util.common.session.SessionContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.SessionManager;
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
	
	private static String translatePatten = "&[0-9a-zA-Z/$_-]+";
	public void setTranslatePatten(String translatePatten) { Translation.translatePatten = translatePatten; }
	public String getTranslatePatten() { return Translation.translatePatten;}

	public static String getWording(String key) {
		logger.debug("getWording[{}]", key);
		String value = key;
        try {

        	Dictionary dico = null;
        	String currentLang = "en";
        	
        	logger.debug("currentLang[{}] BF", currentLang);
        	try {
	        	String sessionId = SessionManager.getRequestCxtSessionId();
	        	if ( null != sessionId ) {
	            	if (ServicesImplFactory.getInstance() != null) {
	            		ISessionListContainer sessionListContainer = (ISessionListContainer)ServicesImplFactory.getInstance().getService(ISessionListContainer.class);
	            		SessionContainer sessionContainer = sessionListContainer.getSessionContainer(sessionId);
	    	        	if (sessionContainer != null) {
	    	        		String string = sessionContainer.getAttributeTyped(String.class, "currentLang");
	    	        		if ( null != string ) {
	    	        			currentLang = string;
	    	        		} else {
	    	        			logger.warn("getWording string IS NULL!");
	    	        		}
	    	        	} else {
	    	        		logger.warn("getWording sessionContainer IS NULL!");
	    	        	}
	            	} else {
	            		logger.warn("getWording ServicesImplFactory.getInstance() IS NULL!");
	            	}
	        	} else {
	        		logger.warn("getWording sessionId IS NULL!");
	        	}
        	} catch ( Exception e) {
        		logger.warn("getWording currentLang e[{}]", e.toString());
	    	}
        	logger.debug("currentLang[{}] AF", currentLang);
        	
        	dico = DictionaryManager.getInstance().getDictionary(currentLang);     
        	if ( null != dico ) {
        		value = dico.getWording(key);
        	} else {
        		logger.error("dico IS NULL");
        	}
        }
        catch (final MissingResourceException e) {
        	logger.warn("Can't find key [{}] in dictionary MissingResourceException[{}]", key, e.toString());
        }
        return value;
	}
	
	public static String getDBMessage(String input) {
		return Translation.getDBMessage(translatePatten, input);
	}
	
	public static String getDBMessage(String regex, String input) {
		logger.trace("{} regex[{}] input[{}]", new Object[]{"getDBMessage", regex, input});
		String ret = input;
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(input);
			while(m.find()) {
				String key = m.group();
				logger.trace("{} m.group()[{}]", "getDBMessage", key);
				String translation = Translation.getWording(key);
				
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
