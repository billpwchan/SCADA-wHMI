package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class OpmMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIOpmFactory> uiOpmFactorys = new HashMap<String, UIOpmFactory>();
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
		logger.begin(function);
		UIOpm_i uiOpm_i = null;
		for ( String className : uiOpmFactorys.keySet() ) {
			UIOpmFactory uiOpmFactory = uiOpmFactorys.get(className);
			if ( null != uiOpmFactory ) {
				uiOpm_i = uiOpmFactory.getOpm(key);
				if ( null != uiOpm_i ) break;
			}
		}
		logger.end(function);
		return uiOpm_i;
	}
	
}
