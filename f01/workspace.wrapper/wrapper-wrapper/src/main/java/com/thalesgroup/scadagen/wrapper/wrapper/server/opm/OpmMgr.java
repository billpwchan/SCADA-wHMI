package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpmMgr {
	
	private final static Logger logger = LoggerFactory.getLogger(OpmMgr.class.getName());
	
	private static HashMap<String, UIOpm_i> instances = new HashMap<String, UIOpm_i>();
	
	public static UIOpm_i getInstance(String key) {

		logger.info("key[{}]", key);
		
		UIOpm_i uiOpm_i = instances.get(key);
		
		if ( null == uiOpm_i ) {
			
			String UIOpmSCADAgenClassName = UIOpmSCADAgen.class.getSimpleName();

			if ( UIOpmSCADAgenClassName.equals(key) ) {	uiOpm_i = UIOpmSCADAgen.getInstance(); }
			
			if ( null != uiOpm_i ) instances.put(key, uiOpm_i);
		}
		
		if ( null == uiOpm_i ) logger.warn("uiOpm_i IS NULL");

		return uiOpm_i;
	}
	private OpmMgr () {}

}
