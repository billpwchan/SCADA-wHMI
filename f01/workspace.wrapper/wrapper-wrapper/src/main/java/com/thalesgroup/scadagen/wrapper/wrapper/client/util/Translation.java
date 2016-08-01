package com.thalesgroup.scadagen.wrapper.wrapper.client.util;

import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.Dictionary;

public class Translation {
	private final static Logger logger = Logger.getLogger(Translation.class.getName());
	private static final String JS_DICTIONARY_VAR_NAME = "table";
	private static final Dictionary dictionary = Dictionary.getDictionary(JS_DICTIONARY_VAR_NAME);
	public static String getWording(String key) {
		logger.log(Level.SEVERE, "getWording[{}] ");
		String value = key;
        try {
            if (dictionary != null) value = dictionary.get(key);
        }
        catch (final MissingResourceException e) {
        	logger.log(Level.SEVERE, "Can't find key [" + key + "] in dictionary");
        }
        return value;
	}
}
