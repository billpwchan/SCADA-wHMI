package com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Mgr_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr;

public class MgrFactory {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private MgrFactory() {}
	
	private static MgrFactory instance = null;
	public static MgrFactory getInstance() {
		if ( null == instance ) instance = new MgrFactory();
		return instance;
	}

	public Mgr_i getMgr(String name, String key) {
		final String function = "getMgr";
		logger.begin(className, function);
		logger.debug(className, function, "name[{}] key[{}]", name, key);
		Mgr_i mgr = null;
		if ( null != name ) {
			
			if ( TscMgr.class.getSimpleName().equals(name) ) {
				mgr = TscMgr.getInstance(key);
			}
			if ( OlsMgr.class.getSimpleName().equals(name) ) {
				mgr = OlsMgr.getInstance(key);
			}
		}
		logger.end(className, function);
		return mgr;
	}
}
