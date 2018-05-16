package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.util;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class Util {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	public Object getObjectParameter(final Map<String, Object> params, final String key) {
		final Object ret = ( null != params) ? params.get(key) : null;
    	logger.warn("getParameter", "key[{}] IS ret[{}]", key, ret);
		return ret;
	}
	
	public String getStringParameter(final Map<String, Object> params, final String key) {
		String ret = null;
		if ( null != params) {
			Object obj = getObjectParameter(params, key);
			if ( null != obj ) {
				if ( obj instanceof String ) {
					ret = (String)obj;
    			} else {
    				logger.warn("getParameter", "key[{}] obj IS NOT A String", key);
    			}
    		} else {
    			logger.warn("getParameter", "key[{}] obj IS NULL", key);
    		}
		}
		return ret;
	}
	
}
