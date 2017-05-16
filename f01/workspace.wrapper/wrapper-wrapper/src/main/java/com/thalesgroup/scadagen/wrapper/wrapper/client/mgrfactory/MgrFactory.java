package com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Mgr_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr;

public class MgrFactory {
	
	private static String className = UIWidgetUtil.getClassSimpleName(MgrFactory.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
			
			String strTscMgr = UIWidgetUtil.getClassSimpleName(TscMgr.class.getName());
			
			if ( name.equals(strTscMgr) ) {
				mgr = TscMgr.getInstance(key);
			}
		}
		logger.end(className, function);
		return mgr;
	}
}
