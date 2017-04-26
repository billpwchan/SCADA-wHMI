package com.thalesgroup.scadasoft.gwebhmi.security.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadasoft.gwebhmi.security.client.ScsLoginEntryPoint_i.FrameworkName;
import com.thalesgroup.scadasoft.gwebhmi.security.client.ScsLoginEntryPoint_i.PropertiesName;
import com.thalesgroup.scadasoft.gwebhmi.security.client.factory.COCC;
import com.thalesgroup.scadasoft.gwebhmi.security.client.factory.FAS;
import com.thalesgroup.scadasoft.gwebhmi.security.client.factory.IScsLoginEntryPoint;
import com.thalesgroup.scadasoft.gwebhmi.security.client.factory.WHMI;

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
			public void updated(Map<String, String> map) {
				LOGGER.debug(LOG_PREFIX+"onModuleLoad updated");
				launch(map);
			}
			@Override
			public void failed() {
				LOGGER.debug(LOG_PREFIX+"onModuleLoad failed");
				launch(null);
			}
        });

    }
    
    private void launch(Map<String, String> map) {
    	
    	IScsLoginEntryPoint iScsLoginEntryPoint = null;

    	if ( null != map ) {
    		String framework = map.get(PropertiesName.framework.toString());
    		LOGGER.debug(LOG_PREFIX+"launch framework["+framework+"]");
    		
    		iScsLoginEntryPoint = getEntryPoint(framework);
    	} else {
    		LOGGER.warn(LOG_PREFIX+"launch map IS NULL");
    	}
    	
		if ( null != iScsLoginEntryPoint ) {
			iScsLoginEntryPoint.launch(map);
		} else {
			LOGGER.warn(LOG_PREFIX+"launch iAppEntryPoint IS NULL");
		}
    }
    
    private IScsLoginEntryPoint getEntryPoint(String key) {
    	
    	IScsLoginEntryPoint iScsLoginEntryPoint = null;
    	
    	LOGGER.debug(LOG_PREFIX+"getEntryPoint key["+key+"]");
    	
 		if ( null != key ) {
        	if ( FrameworkName.SCADAgen.toString().equals(key) )  {
        		iScsLoginEntryPoint = new WHMI();
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
