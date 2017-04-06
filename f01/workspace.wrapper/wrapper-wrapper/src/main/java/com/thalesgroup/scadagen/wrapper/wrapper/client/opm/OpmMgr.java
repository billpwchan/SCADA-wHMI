package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class OpmMgr {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(OpmMgr.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private HashMap<String, UIOpmFactory> uiOpmFactorys = new HashMap<String, UIOpmFactory>();
	public void addUIOpmFactory(String className, UIOpmFactory uiOpmFactory) { this.uiOpmFactorys.put(className, uiOpmFactory); }
	public void cleanUIOpmFactory() { this.uiOpmFactorys.clear(); };
	
	private OpmMgr() {}
	private static OpmMgr instance = null;
	public static OpmMgr getInstance() {
		if ( null == instance ) instance = new OpmMgr();
		return instance;
	}
	
	public UIOpm_i getOpm(String key) {
		String function = "getOpm";
		logger.begin(className, function);
		UIOpm_i uiOpm_i = null;
		for ( String className : uiOpmFactorys.keySet() ) {
			UIOpmFactory uiOpmFactory = uiOpmFactorys.get(className);
			if ( null != uiOpmFactory ) {
				uiOpm_i = uiOpmFactory.getOpm(key);
			}
		}
		logger.end(className, function);
		return uiOpm_i;
	}
	
}
