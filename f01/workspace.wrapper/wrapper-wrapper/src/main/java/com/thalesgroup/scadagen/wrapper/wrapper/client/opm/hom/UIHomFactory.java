package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIHomFactory implements UIHomFactory_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIHomFactory_i> factorys = new HashMap<String, UIHomFactory_i>();
	public void addFactory(String className, UIHomFactory_i uiHomFactory) { this.factorys.put(className, uiHomFactory); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UIHomFactory() {}
	private static UIHomFactory instance = null;
	public static UIHomFactory getInstance() {
		if ( null == instance ) instance = new UIHomFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UIHom_i get(String key) {
		String function = "get";
		logger.begin(function);
		UIHom_i uiHom_i = null;
		for ( String className : factorys.keySet() ) {
			UIHomFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				uiHom_i = uiFactory.get(key);
				if ( null != uiHom_i ) break;
			}
		}
		logger.end(function);
		return uiHom_i;
	}
}
