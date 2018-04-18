package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.util;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class Util {
	
	private final String className_ = UIWidgetUtil.getClassSimpleName(UIGws.class.getName());
	private UILogger logger_ = UILoggerFactory.getInstance().getLogger(className_);

	public Object getObjectParameter(final Map<String, Object> params, final String key) {
		final Object ret = ( null != params) ? params.get(key) : null;
    	logger_.warn(className_, "getParameter", "key[{}] IS ret[{}]", key, ret);
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
    				logger_.warn(className_, "getParameter", "key[{}] obj IS NOT A String", key);
    			}
    		} else {
    			logger_.warn(className_, "getParameter", "key[{}] obj IS NULL", key);
    		}
		}
		return ret;
	}
	
}
