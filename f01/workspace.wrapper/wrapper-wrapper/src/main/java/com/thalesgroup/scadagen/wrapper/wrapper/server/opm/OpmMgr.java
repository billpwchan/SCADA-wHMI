package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class OpmMgr {

	private static UILogger_i logger = UILoggerFactory.getInstance().get(OpmMgr.class.getName());

	private static Map<String, UIOpm_i> instances = new HashMap<String, UIOpm_i>();

	public static UIOpm_i getInstance(String key) {

		logger.info("getInstance key[{}]", key);

		UIOpm_i uiOpm_i = instances.get(key);

		if ( null == uiOpm_i ) {

			if ( UIOpmSCADAgen.class.getSimpleName().equals(key) ) {

				uiOpm_i = UIOpmSCADAgen.getInstance(); 
			}

			if ( null != uiOpm_i ) instances.put(key, uiOpm_i);
		}

		if ( null == uiOpm_i ) logger.warn("getInstance uiOpm_i IS NULL");

		return uiOpm_i;
	}
	private OpmMgr () {}
}
