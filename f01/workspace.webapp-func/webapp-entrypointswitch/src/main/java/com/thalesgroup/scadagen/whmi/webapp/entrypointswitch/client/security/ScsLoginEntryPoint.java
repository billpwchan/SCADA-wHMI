package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.ScsLoginEntryPoint_i.FrameworkName;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.ScsLoginEntryPoint_i.PropertiesName;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.COCC;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.FAS;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.IScsLoginEntryPoint;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.SCADAgen;

/**
 * Login application for MAESTRO demo.
 */
public class ScsLoginEntryPoint implements EntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[ScsLoginEntryPoint] ";
	
	private final static String mode	= ConfigurationType.XMLFile.toString();
	private final static String module	= null;
	private final static String folder	= "UIConfig";
    private final static String xml		= "UILauncher_ScsLoginEntryPoint.xml";
    private final static String tag		= "header";
	
    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {
    	
    	LOGGER.debug(LOG_PREFIX+"onModuleLoad");

        List<String> keys = new LinkedList<String>();
        for ( String properties : PropertiesName.toStrings() ) {
        	keys.add(properties);
        }
        
        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
        web.getWebConfig(mode, module, folder, xml, tag, keys, new WebConfigMgrEvent() {
			@Override
			public void updated(Map<String, String> keyValues) {
				LOGGER.debug(LOG_PREFIX+"onModuleLoad updated");
				
				Map<String, Object> params = new HashMap<String, Object>();
				for ( String key : keyValues.keySet() ) {
					params.put(key, keyValues.get(key));
        		}
				
				launch(params);
			}
			@Override
			public void failed() {
				LOGGER.debug(LOG_PREFIX+"onModuleLoad failed");
				launch(null);
			}
        });

    }
    
    private String getStringParameter(Map<String, Object> params, String key) {
    	String value = null;
		Object obj = params.get(PropertiesName.framework.toString());
		if ( null != obj ) {
			if ( obj instanceof String ) {
				value = (String)obj;
			} else {
				LOGGER.warn(LOG_PREFIX+"getStringParameter key["+key+"] obj IS NOT A String");
			}
		} else {
			LOGGER.warn(LOG_PREFIX+"getStringParameter key["+key+"] obj IS NULL");
		}
		return value;
    }
    
    private void launch(Map<String, Object> params) {
    	
    	IScsLoginEntryPoint iScsLoginEntryPoint = null;

    	if ( null != params ) {
    		String framework = getStringParameter(params, PropertiesName.framework.toString());
    		LOGGER.debug(LOG_PREFIX+"launch framework["+framework+"]");
    		
    		iScsLoginEntryPoint = getEntryPoint(framework);
    	} else {
    		LOGGER.warn(LOG_PREFIX+"launch map IS NULL");
    	}
    	
		if ( null != iScsLoginEntryPoint ) {
			iScsLoginEntryPoint.launch(params);
		} else {
			LOGGER.warn(LOG_PREFIX+"launch iAppEntryPoint IS NULL");
		}
    }
    
    private IScsLoginEntryPoint getEntryPoint(String key) {
    	
    	IScsLoginEntryPoint iScsLoginEntryPoint = null;
    	
    	LOGGER.debug(LOG_PREFIX+"getEntryPoint key["+key+"]");
    	
 		if ( null != key ) {
        	if ( FrameworkName.SCADAgen.toString().equals(key) )  {
        		iScsLoginEntryPoint = new SCADAgen();
        	}
        	else if ( FrameworkName.FAS.toString().equals(key) )  {
        		iScsLoginEntryPoint = new FAS();
    		}
        	else if ( FrameworkName.COCC.toString().equals(key) )  {
        		iScsLoginEntryPoint = new COCC();
    		}
        	else {
        		iScsLoginEntryPoint = new FAS();
    		}
		} else {
			LOGGER.warn(LOG_PREFIX+"getEntryPoint key IS NULL");
		}
    	
		return iScsLoginEntryPoint;
    }
}
