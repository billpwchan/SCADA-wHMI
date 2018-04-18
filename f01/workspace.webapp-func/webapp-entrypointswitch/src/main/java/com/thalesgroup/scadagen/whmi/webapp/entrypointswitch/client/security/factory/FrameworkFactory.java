package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.IFramework.FrameworkName;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.warehouse.COCC;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.warehouse.FAS;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.warehouse.SCADAgen;

public class FrameworkFactory {
	
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";

    public IFramework getEntryPoint(final String key) {
    	final String f = "getEntryPoint";
    	
    	IFramework iScsLoginEntryPoint = null;
    	
    	logger_.debug(logPrefix_+f+" key["+key+"]");
    	
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
			logger_.warn(logPrefix_+f+" key IS NULL");
		}
    	
		return iScsLoginEntryPoint;
    }
}
