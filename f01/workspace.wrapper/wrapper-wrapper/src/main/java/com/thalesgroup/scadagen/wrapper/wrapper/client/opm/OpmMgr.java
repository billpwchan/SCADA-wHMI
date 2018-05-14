package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class OpmMgr {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
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
		logger.begin(className, function);
		UIOpm_i uiOpm_i = null;
		for ( String className : uiOpmFactorys.keySet() ) {
			UIOpmFactory uiOpmFactory = uiOpmFactorys.get(className);
			if ( null != uiOpmFactory ) {
				uiOpm_i = uiOpmFactory.getOpm(key);
				if ( null != uiOpm_i ) break;
			}
		}
		logger.end(className, function);
		return uiOpm_i;
	}
	
}
