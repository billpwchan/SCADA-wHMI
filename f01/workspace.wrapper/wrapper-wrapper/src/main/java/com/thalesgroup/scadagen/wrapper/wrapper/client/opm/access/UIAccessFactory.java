package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIAccessFactory implements UIAccessFactory_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIAccessFactory_i> factorys = new HashMap<String, UIAccessFactory_i>();
	public void addFactory(String className, UIAccessFactory_i uiAccessFactory_i) { this.factorys.put(className, uiAccessFactory_i); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UIAccessFactory() {}
	private static UIAccessFactory instance = null;
	public static UIAccessFactory getInstance() {
		if ( null == instance ) instance = new UIAccessFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UIAccess_i get(String key) {
		String function = "get";
		logger.begin(function);
		UIAccess_i iUIAccess = null;
		for ( String className : factorys.keySet() ) {
			UIAccessFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				iUIAccess = uiFactory.get(key);
				if ( null != iUIAccess ) break;
			}
		}
		logger.end(function);
		return iUIAccess;
	}
}
