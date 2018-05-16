package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class Util {

	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(Util.class.getName());
	public static Map<String, String> getParameters(String inputstr) {
		final String function = "getParameters";
		logger.begin(function);
		logger.debug(function, "inputstr[{}]", inputstr);
		Map<String, String> confs = new HashMap<String, String>();
		String [] uiConfs = inputstr.split("&");
		if ( null != inputstr && inputstr.trim().length() > 0 ) {
			if ( null != uiConfs ) {
				for ( String conf : uiConfs ) {
					String [] keyValue = conf.split("=");
					if ( null != keyValue ) {
						if ( 2 == keyValue.length ) {
							confs.put(keyValue[0], keyValue[1]);
							logger.debug(function, "keyValue[0][{}] keyValue[1][{}]", keyValue[0], keyValue[1]);
						} else {
							logger.warn(function, "conf[{}] split keyValue.length[{}] IS NOT EQUAL to 2", conf, keyValue.length);
						}
					} else {
						logger.warn(function, "keyValue IS NULL");
					}
				}
			} else {
				logger.warn(function, "uiConfs IS NULL");
			}
		}
		logger.end(function);
		return confs;
	}
}
