package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIUserFactory implements UIUserFactory_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIUserFactory_i> factorys = new HashMap<String, UIUserFactory_i>();
	public void addFactory(String className, UIUserFactory_i uiUserFactory_i) { this.factorys.put(className, uiUserFactory_i); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UIUserFactory() {}
	private static UIUserFactory instance = null;
	public static UIUserFactory getInstance() {
		if ( null == instance ) instance = new UIUserFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UIUser_i get(String key) {
		String function = "get";
		logger.begin(function);
		UIUser_i iUIUser = null;
		for ( String className : factorys.keySet() ) {
			UIUserFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				iUIUser = uiFactory.get(key);
				if ( null != iUIUser ) break;
			}
		}
		logger.end(function);
		return iUIUser;
	}
}
