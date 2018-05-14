package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class UINetworkFactory implements UINetworkFactory_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private Map<String, UINetworkFactory_i> factorys = new HashMap<String, UINetworkFactory_i>();
	public void addFactory(String className, UINetworkFactory_i uiNetworkFactory_i) { this.factorys.put(className, uiNetworkFactory_i); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UINetworkFactory() {}
	private static UINetworkFactory instance = null;
	public static UINetworkFactory getInstance() {
		if ( null == instance ) instance = new UINetworkFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UINetwork_i get(String key) {
		String function = "get";
		logger.begin(className, function);
		UINetwork_i iUINetwork = null;
		for ( String className : factorys.keySet() ) {
			UINetworkFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				iUINetwork = uiFactory.get(key);
				if ( null != iUINetwork ) break;
			}
		}
		logger.end(className, function);
		return iUINetwork;
	}
}
