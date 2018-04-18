package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.util;

import java.util.List;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;

public class Util {
	
	private final static String CLASSNAME_ = Util.class.getSimpleName();
    private final static ClientLogger LOGGER_ = ClientLogger.getClientLogger(Util.class.getName());
    private final static String LOGPREFIX_ = "["+CLASSNAME_+"] ";
	
    public static String getStringParameter(final Map<String, Object> params, final String key) {
    	final String f = "getStringParameter";
    	LOGGER_.debug(LOGPREFIX_+f+" key["+key+"]");
    	String value = null;
    	final Object obj = params.get(key);
		if ( obj instanceof List<?> ) {
			final List<?> list = (List<?>) obj;
			if ( null != list && ! list.isEmpty() ) {
				for ( Object objs : list ) {
					if ( objs instanceof String ) {
						if(null==value) value="";
						value+=(String)objs;
					}
				}
			}
		} else if ( obj instanceof String ) {
			value = (String)obj;
		} else {
			LOGGER_.warn(LOGPREFIX_+f+" key["+key+"] obj IS NOT A String");
		}
		LOGGER_.debug(LOGPREFIX_+f+" key["+key+"] value["+value+"]");
		return value;
    }
}
