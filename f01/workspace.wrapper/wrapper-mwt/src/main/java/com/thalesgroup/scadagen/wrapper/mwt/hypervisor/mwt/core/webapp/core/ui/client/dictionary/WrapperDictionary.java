package com.thalesgroup.scadagen.wrapper.mwt.hypervisor.mwt.core.webapp.core.ui.client.dictionary;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;

public class WrapperDictionary {
	public static String Get (String tag) {
        String caption = Dictionary.getWording(tag);
        if (caption.startsWith("#Undefined#")) {
            caption = tag;
        }
        return tag;
	}
}
