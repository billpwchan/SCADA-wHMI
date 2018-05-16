package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class URLSetting {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public void storeURLSetting(Settings settings) {
		final String function = "storeURLSetting";
		logger.begin(function);
		final Map<String, List<String>> paramsMap = Window.Location.getParameterMap();
		for ( String key: paramsMap.keySet() ) {
			final List<String> values = paramsMap.get(key);
			if ( values.size() > 0 ) {
				final String value = values.get(0);
				final String keyLowerCase = key.toLowerCase();
				settings.set(keyLowerCase, value);
				logger.debug(function, "keyLowerCase[{}] value[{}]", keyLowerCase, value);
			}
		}
		logger.end(function);
	}
}
