package com.thalesgroup.scadagen.wrapper.wrapper.server;

import com.thalesgroup.hypervisor.mwt.core.util.common.session.ISessionListContainer;
import com.thalesgroup.hypervisor.mwt.core.util.common.session.SessionContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.SessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.i18n.DictionaryManager;

import java.util.MissingResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Translation {
	
	private final static Logger logger			= LoggerFactory.getLogger(Translation.class.getName());

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
	            	ISessionListContainer sessionListContainer = (ISessionListContainer)ServicesImplFactory.getInstance().getService(ISessionListContainer.class);
	            	SessionContainer sessionContainer = sessionListContainer.getSessionContainer(sessionId);
	            	if (sessionContainer != null) {
	            		Object object = sessionContainer.getAttribute("currentLang");
	            		if ( null != object ) {
	            			if ( object instanceof String )
	            				currentLang = (String)object;
	            		}
	            	}
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
        	logger.error("Can't find key [{}] in dictionary", key);
        	logger.error("MissingResourceException[{}]", e.toString());
        }
        return value;
	}
}
