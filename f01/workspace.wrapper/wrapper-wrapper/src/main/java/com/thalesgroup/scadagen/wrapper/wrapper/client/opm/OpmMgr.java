package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class OpmMgr {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(OpmMgr.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, UIOpm_i> instances = new HashMap<String, UIOpm_i>();
	
	public static UIOpm_i getInstance(String key) {
		String function = "getInstance";
		
		logger.begin(className, function);
		
		logger.info(className, function, "key[{}]", key);
		
		UIOpm_i uiOpm_i = instances.get(key);
		
		if ( null == uiOpm_i ) {
			
			String classNameUIOpmSCS = UIWidgetUtil.getClassSimpleName(UIOpmSCS.class.getName());
			String classNameUIOpmSCADAgen = UIWidgetUtil.getClassSimpleName(UIOpmSCADAgen.class.getName());
			
			logger.info(className, function, "classNameUIOpmSCS[{}]", classNameUIOpmSCS);
			logger.info(className, function, "classNameUIOpmSCADAgen[{}]", classNameUIOpmSCADAgen);
			
			if ( classNameUIOpmSCS.equals(key) ) {	uiOpm_i = UIOpmSCS.getInstance(); }
			if ( classNameUIOpmSCADAgen.equals(key) ) {	uiOpm_i = UIOpmSCADAgen.getInstance(); }
			
			if ( null != uiOpm_i ) instances.put(key, uiOpm_i);
		}
		
		if ( null == uiOpm_i ) logger.warn(className, function, "uiOpm_i IS NULL");
		
		logger.end(className, function);
		
		return uiOpm_i;
	}
	private OpmMgr () {}


}
