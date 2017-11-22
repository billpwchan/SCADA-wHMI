package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

/**
 * @author syau
 *
 */
public class UIControlPriorityFactory {

	private static final String className = UIWidgetUtil.getClassSimpleName(UIControlPriorityFactory.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private Map<String, UIControlPriorityFactory_i> factorys = new HashMap<String, UIControlPriorityFactory_i>();
	public void addFactory(String className, UIControlPriorityFactory_i uiControlPriorityFactory) { this.factorys.put(className, uiControlPriorityFactory); }
	public void cleanFactory() { this.factorys.clear(); };
	
	private UIControlPriorityFactory() {}
	private static UIControlPriorityFactory instance = null;
	public static UIControlPriorityFactory getInstance() {
		if ( null == instance ) instance = new UIControlPriorityFactory();
		return instance;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public UIControlPriority_i get(String key) {
		String function = "get";
		logger.begin(className, function);
		UIControlPriority_i uiControlPriority_i = null;
		for ( String className : factorys.keySet() ) {
			UIControlPriorityFactory_i uiFactory = factorys.get(className);
			if ( null != uiFactory ) {
				uiControlPriority_i = uiFactory.get(key);
				if ( null != uiControlPriority_i ) break;
			}
		}
		logger.end(className, function);
		return uiControlPriority_i;
	}
}
