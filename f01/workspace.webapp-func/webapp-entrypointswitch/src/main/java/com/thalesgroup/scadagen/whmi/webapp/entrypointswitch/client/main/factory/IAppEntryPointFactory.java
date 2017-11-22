package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.AppEntryPoint_i.FrameworkName;

public class IAppEntryPointFactory {
	
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[IAppEntryPointFactory] ";
	
    public static IAppEntryPoint getEntryPoint(String key) {
    	IAppEntryPoint iAppEntryPoint = null;
    	
    	LOGGER.debug(LOG_PREFIX+"getEntryPoint key["+key+"]");
    	if ( null != key ) {
        	if ( FrameworkName.SCADAgen.toString().equals(key) ) {
        		iAppEntryPoint = new SCADAgen();
        	} 
        	else if ( FrameworkName.FAS.toString().equals(key) ) {
        		iAppEntryPoint = new FAS();
    		} 
        	else if ( FrameworkName.COCC.toString().equals(key) ) {
        		iAppEntryPoint = new COCC();
    		} 
        	else if ( FrameworkName.SCADAgenStandalone.toString().equals(key) ) {
        		iAppEntryPoint = new SCADAgenStandalone();
    		}
    	} else {
    		LOGGER.warn(LOG_PREFIX+"getEntryPoint key IS NULL");
    	}
    	
    	return iAppEntryPoint;
    }
}
