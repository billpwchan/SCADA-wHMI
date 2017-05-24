package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Window;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.AppEntryPoint_i.PropertiesName;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IAppEntryPoint;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IAppEntryPointFactory;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint extends MwtEntryPointApp {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[AppEntryPoint] ";

	/**
     * Constructor.
     */
    public AppEntryPoint() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	for ( Entry<String, List<String>> entry : Window.Location.getParameterMap().entrySet() ) {
    		params.put(entry.getKey(), entry.getValue());
    	}

    	String framework = Util.getStringParameter(params, PropertiesName.framework.toString());
		if ( null != framework ) {
			
			launch(params);
		} else {
			
			final String mode	= "XMLFile";
			final String module	= null;
			final String folder	= "UIConfig";
		    final String xml	= "UILauncher_AppEntryPoint.xml";
		    final String tag	= "header";
			
	        List<String> keys = new LinkedList<String>();
	        for ( String properties : PropertiesName.toStrings() ) {
	        	keys.add(properties);
	        }
	        
	        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
	        
	        web.getWebConfig( mode,  module, folder, xml, tag, keys, new WebConfigMgrEvent() {
				@Override
				public void updated(Map<String, String> keyValues) {
					LOGGER.debug(LOG_PREFIX+"onContextReadyEventAfter getWebConfig updated");
					
					Map<String, Object> params = new HashMap<String, Object>();
					for ( String key : keyValues.keySet() ) {
						params.put(key, keyValues.get(key));
	        		}
					
					launch(params);
				}
				@Override
				public void failed() {
					LOGGER.debug(LOG_PREFIX+"onContextReadyEventAfter getWebConfig failed");
					launch(null);
				}
	        });
		}
    }
    
    private void launch(final Map<String, Object> params) {
    	
    	IAppEntryPoint iAppEntryPoint = null;

    	if ( null != params ) {

    		String framework = Util.getStringParameter(params, PropertiesName.framework.toString());
    		
    		LOGGER.debug(LOG_PREFIX+"launch framework["+framework+"]");

    		iAppEntryPoint = IAppEntryPointFactory.getEntryPoint(framework);
    		
    		if ( null != iAppEntryPoint ) {
    			iAppEntryPoint.launch(params);
    		} else {
    			LOGGER.warn(LOG_PREFIX+"launch framework["+framework+"] iAppEntryPoint IS NULL");
    		}
    	} else {
    		LOGGER.warn(LOG_PREFIX+"launch map IS NULL");
    	}
    }
}
