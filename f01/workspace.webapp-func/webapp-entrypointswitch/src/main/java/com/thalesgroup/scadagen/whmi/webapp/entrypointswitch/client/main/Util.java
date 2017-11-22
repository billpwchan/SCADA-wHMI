package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main;

import java.util.List;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;

public class Util {
	
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[Util] ";
	
    public static String getStringParameter(Map<String, Object> params, String key) {
    	LOGGER.debug(LOG_PREFIX+"getStringParameter key["+key+"]");
    	String value = null;
		Object obj = params.get(key);
		if ( obj instanceof List<?> ) {
			List<?> list = (List<?>) obj;
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
			LOGGER.warn(LOG_PREFIX+"getStringParameter key["+key+"] obj IS NOT A String");
		}
		LOGGER.debug(LOG_PREFIX+"getStringParameter key["+key+"] value["+value+"]");
		return value;
    }
}
