package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.IFramework.FrameworkName;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse.COCC;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse.FAS;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse.SCADAgen;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory.warehouse.SCADAgenStandalone;

public class FrameworkFactory {
	
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";

    public IFramework getEntryPoint(String key) {
    	final String f = "getEntryPoint";
    	IFramework iAppEntryPoint = null;
    	
    	logger_.debug(logPrefix_+f+" key["+key+"]");
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
    		logger_.warn(logPrefix_+f+" key IS NULL");
    	}
    	
    	return iAppEntryPoint;
    }
}
