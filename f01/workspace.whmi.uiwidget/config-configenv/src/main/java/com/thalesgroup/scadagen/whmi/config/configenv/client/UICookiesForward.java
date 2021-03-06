package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;


public class UICookiesForward {

	private static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(UICookiesForward.class.getName());
	
	public static void forwardString(String name, String defaultValue) {
		final String function = "forwardString";
		logger.begin(function);
		logger.debug(function, "name[{}]", name);
		
		Settings setting = Settings.getInstance();
		
		if ( null != setting.get(name) ) {
			// Find value from URL and Valid
			String value = setting.get(name);

			logger.debug(function, "Cookies name[{}] value[{}]", name, value);
			
			setting.set(name, value);
		} else {
			// Find value from Cookies and Valid
			String value = UICookies.getCookies(name);
			if ( null != value ) {

				logger.debug(function, "Cookies name[{}] value[{}]", name, value);
				
				setting.set(name, value);
			}
		}
		
		logger.debug(function, "setting.get([{}])[{}] defaultValue[{}]", new Object[]{name, setting.get(name), defaultValue});
		
		// Store the default value to 1 if not found on URL and Cookies
		if ( null == setting.get(name) ) setting.set(name, Integer.toString(1));
		
		logger.debug(function, "setting.get([{}])[{}]", name, setting.get(name));

		// Store the name to Cookies
		UICookies.setCookies(name, setting.get(name));
		
		logger.end(function);
	}
	
	public static void forwardInt(String name, int defaultValue) {
		final String function = "forwardInt";
		logger.begin(function);
		logger.debug(function, "name[{}]", name);
		
		Settings setting = Settings.getInstance();
		
		if ( null != setting.get(name) ) {
			// Find value from URL and Valid
			String strValue = setting.get(name);
			int value = 1;
			try {
				value = Integer.parseInt(strValue);
				if ( value <= 0 ) value = 1;
			} catch ( NumberFormatException e) {
				logger.warn(function, "NumberFormatException e[{}]", e.toString());
			}
			
			logger.debug(function, "URL name[{}] value[{}]", name, value);
			
			setting.set(name, Integer.toString(value));
		} else {
			// Find value from Cookies and Valid
			String strValue = UICookies.getCookies(name);
			if ( null != strValue ) {
				int value = 1;
				try {
					value = Integer.parseInt(strValue);
					if ( value <= 0 ) value = 1;
				} catch ( NumberFormatException e) {
					logger.warn(function, "NumberFormatException e[{}]", e.toString());
				}
				
				logger.debug(function, "Cookies name[{}] value[{}]", name, value);
				
				setting.set(name, Integer.toString(value));
			}
		}
		
		logger.debug(function, "setting.get([{}])[{}] defaultValue[{}]", new Object[]{name, setting.get(name), defaultValue});
		
		// Store the default value to 1 if not found on URL and Cookies
		if ( null == setting.get(name) ) setting.set(name, Integer.toString(defaultValue));
		
		logger.debug(function, "setting.get([{}])[{}]", name, setting.get(name));

		// Store the value to Cookies
		UICookies.setCookies(name, setting.get(name));
		
		logger.end(function);
	}
}
