package com.thalesgroup.scadagen.wrapper.wrapper.server;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.DictionaryManager;
import com.thalesgroup.scadagen.common.calculated.GDGMessage;

import java.util.MissingResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Translation {
	
	private final static Logger logger			= LoggerFactory.getLogger(GDGMessage.class.getName());

	public static String getWording(String key) {
		logger.info("getWording[{}]", key);
		String value = key;
        try {

        	Dictionary dico = null;
        	String currentLang = "en";

//        	try {
//    			String sessionId = SessionManager.getRequestCxtSessionId();
//    			
//    			if ( null != sessionId ) {
//    				ISessionListContainer sessionListContainer = (ISessionListContainer)ServicesImplFactory.getInstance().getService(ISessionListContainer.class);
//    				SessionContainer sessionContainer = sessionListContainer.getSessionContainer(sessionId);
//    				if (sessionContainer != null) {
//    					String l = (String) sessionContainer.getAttribute("currentLang");
//    					if ( null != l ) {
//    						currentLang = l;
//    					} else {
//    						logger.log(Level.SEVERE, "using default lang en, currentLang IS NULL");
//    					}
//    					
//    				} else {
//    					logger.log(Level.SEVERE, "sessionContainer IS NULL");
//    				}					
//    			} else {
//    				logger.log(Level.SEVERE, "sessionId IS NULL");
//    			}
//        	} catch ( Exception e) {
//        		logger.log(Level.SEVERE, "getWording currentLang e["+e.toString()+"]");
//        	}

        	logger.info("currentLang[{}]", currentLang);

        	dico = DictionaryManager.getInstance().getDictionary(currentLang);     
        	if ( null != dico ) {
        		if ( null != currentLang ) {
        			value = dico.getWording(key);
        		} else {
        			logger.error("currentLang IS NULL");
        		}
        	} else {
        		logger.error("dico IS NULL");
        	}
        }
        catch (final MissingResourceException e) {
        	logger.error("Can't find key [{}] in dictionary", key);
        	logger.error("MissingResourceException[{}]", e.toString());
        }
        return value;
	}
}
