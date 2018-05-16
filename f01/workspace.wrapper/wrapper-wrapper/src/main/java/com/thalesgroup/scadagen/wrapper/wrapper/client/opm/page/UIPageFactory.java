package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIPageFactory implements UIPageFactory_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIPageFactory_i> factorys = new HashMap<String, UIPageFactory_i>();
	public void addFactory(String className, UIPageFactory_i uiPageFactory_i) { this.factorys.put(className, uiPageFactory_i); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UIPageFactory() {}
	private static UIPageFactory instance = null;
	public static UIPageFactory getInstance() {
		if ( null == instance ) instance = new UIPageFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UIPage_i get(String key) {
		String function = "get";
		logger.begin(function);
		UIPage_i iUIPage = null;
		for ( String className : factorys.keySet() ) {
			UIPageFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				iUIPage = uiFactory.get(key);
				if ( null != iUIPage ) break;
			}
		}
		logger.end(function);
		return iUIPage;
	}
}
