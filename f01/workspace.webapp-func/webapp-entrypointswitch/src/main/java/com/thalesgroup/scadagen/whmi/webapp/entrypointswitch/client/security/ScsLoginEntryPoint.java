package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.FrameworkFactory;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.IFramework;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.util.Util;

/**
 * Login application for MAESTRO demo.
 */
public class ScsLoginEntryPoint implements EntryPoint {
	
	/** logger */
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getSimpleName());
    private final String logPrefix_ = "["+className_+"] ";
	
	private final String mode	= IFramework.XML_MODE;
	private final String module	= IFramework.XML_MODULE;
	private final String folder	= IEntryPoint.STR_XML_FOLDER;
    private final String xml	= className_+IFramework.XML_EXTENSION;
    private final String tag	= IFramework.XML_TAG;
	
    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {
    	final String f = "onModuleLoad";
    	
    	logger_.debug(logPrefix_+f);

        final List<String> keys = new LinkedList<String>();
        for ( String properties : IEntryPoint.PropertiesName.toStrings() ) {
        	keys.add(properties);
        }
        
        final UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
        web.getWebConfig(mode, module, folder, xml, tag, keys, new WebConfigMgrEvent() {
			@Override
			public void updated(Map<String, String> keyValues) {
				logger_.debug(logPrefix_+f+" updated");
				
				final Map<String, Object> params = new HashMap<String, Object>();
				for ( String key : keyValues.keySet() ) {
					params.put(key, keyValues.get(key));
        		}
				
				realize(params);
			}
			@Override
			public void failed() {
				logger_.debug(logPrefix_+f+" failed");
				realize(null);
			}
        });

    }

    private void realize(final Map<String, Object> params) {
    	final String f = "realize";
    	
    	IFramework iScsLoginEntryPoint = null;

    	if ( null != params ) {
    		final String uiFrmw = Util.getStringParameter(params, IEntryPoint.PropertiesName.uiFrmw.toString());
    		logger_.debug(logPrefix_+f+" uiFrmw["+uiFrmw+"]");
    		
    		iScsLoginEntryPoint = new FrameworkFactory().getEntryPoint(uiFrmw);
    	} else {
    		logger_.warn(logPrefix_+f+" map IS NULL");
    	}
    	
		if ( null != iScsLoginEntryPoint ) {
			iScsLoginEntryPoint.launch(params);
		} else {
			logger_.warn(logPrefix_+f+" iAppEntryPoint IS NULL");
		}
    }
    

}
