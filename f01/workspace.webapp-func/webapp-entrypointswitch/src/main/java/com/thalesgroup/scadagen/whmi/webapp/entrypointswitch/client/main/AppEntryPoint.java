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
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.FrameworkFactory;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IFramework;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.util.Util;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint extends MwtEntryPointApp {
	
	/** logger */
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_ = "["+className_+"] ";

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
    	final String f = "onContextReadyEventAfter";
    	final Map<String, Object> params = new HashMap<String, Object>();
    	
    	// Load Parameter from URL
    	for ( Entry<String, List<String>> entry : Window.Location.getParameterMap().entrySet() ) {
    		params.put(entry.getKey(), entry.getValue());
    	}

    	final String uiFrmw = Util.getStringParameter(params, IEntryPoint.PropertiesName.uiFrmw.toString());
		if ( null != uiFrmw ) {
			
			realize(params);
		} else {
			
			final String mode	= IFramework.XML_MODE;
			final String module	= IFramework.XML_MODULE;
			final String folder	= IFramework.XML_FOLDER;
		    final String xml	= this.getClass().getSimpleName()+IFramework.XML_EXTENSION;
		    final String tag	= IFramework.XML_TAG;
			
		    final List<String> keys = new LinkedList<String>();
	        for ( String properties : IEntryPoint.PropertiesName.toStrings() ) {
	        	keys.add(properties);
	        }
	        
	        final UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
	        
	        web.getWebConfig( mode,  module, folder, xml, tag, keys, new WebConfigMgrEvent() {
				@Override
				public void updated(Map<String, String> keyValues) {
					logger_.debug(logPrefix_+f+" getWebConfig updated");
					
					final Map<String, Object> params = new HashMap<String, Object>();
					for ( String key : keyValues.keySet() ) {
						params.put(key, keyValues.get(key));
	        		}
					
					realize(params);
				}
				@Override
				public void failed() {
					logger_.debug(logPrefix_+f+" getWebConfig failed");
					realize(null);
				}
	        });
		}
    }
    
    private void realize(final Map<String, Object> params) {
    	final String f = "realize";
    	IFramework iAppEntryPoint = null;

    	if ( null != params ) {

    		final String uiFrmw = Util.getStringParameter(params, IEntryPoint.PropertiesName.uiFrmw.toString());
    		
    		logger_.debug(logPrefix_+f+ "uiFrmw["+uiFrmw+"]");

    		iAppEntryPoint = new FrameworkFactory().getEntryPoint(uiFrmw);
    		
    		if ( null != iAppEntryPoint ) {
    			iAppEntryPoint.launch(params);
    		} else {
    			logger_.warn(logPrefix_+f+" uiFrmw["+uiFrmw+"] iAppEntryPoint IS NULL");
    		}
    	} else {
    		logger_.warn(logPrefix_+f+" map IS NULL");
    	}
    }
}
