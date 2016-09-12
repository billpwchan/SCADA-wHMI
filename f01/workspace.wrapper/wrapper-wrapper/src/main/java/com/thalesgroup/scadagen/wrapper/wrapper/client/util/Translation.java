package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.MissingResourceException;
import com.google.gwt.i18n.client.Dictionary;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class Translation {
	private static final String className = UIWidgetUtil.getClassSimpleName(DpcMgr.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private static final String JS_DICTIONARY_VAR_NAME = "table";
	private static final Dictionary dictionary = Dictionary.getDictionary(JS_DICTIONARY_VAR_NAME);
	public static String getWording(String key) {
		final String function = "getWording";
		logger.info(className, function, "getWording key[{}]", key);
		String value = key;
        try {
            if (dictionary != null) value = dictionary.get(key);
        }
        catch (final MissingResourceException e) {
        	logger.warn(className, function, "Can't find key [{}] in dictionary", key);
        }
        return value;
	}
}
