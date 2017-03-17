package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;

public class UIEventActionProcessorMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessorMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIEventActionProcessorMgr instance = null;
	public static UIEventActionProcessorMgr getInstance() { 
		if ( null == instance ) 
			instance = new UIEventActionProcessorMgr();
		return instance; }
	private UIEventActionProcessorMgr() {}
	
	public HashMap<String, UIEventActionProcessorMgrFactory> hashMap = new HashMap<String, UIEventActionProcessorMgrFactory>();
	public void addUIEventActionProcessorMgrFactory(String key, UIEventActionProcessorMgrFactory uiEventActionProcessorMgrFactory) {
		hashMap.put(key, uiEventActionProcessorMgrFactory);	
	}
	public void removeUIEventActionProcessorMgrFactory(String key) { hashMap.remove(key); }
	public void clearUIEventActionProcessorMgrFactorys() { this.hashMap.clear(); }
	
	public UIEventActionProcessor_i getUIEventActionProcessorMgr(String key) {
		final String function = "getUIEventActionExecute";
		logger.info(className, function, "key[{}]", key);
		UIEventActionProcessor_i uiEventActionProcessor_i = null;
		for ( String k : hashMap.keySet() ) {
			UIEventActionProcessorMgrFactory v = hashMap.get(k);
			if ( null != k ) {
				uiEventActionProcessor_i = v.getUIEventActionProcessor(key);
			} else {
				logger.warn(className, function, "v from the k[{}] IS NULL", k);
			}
			
			if ( null != uiEventActionProcessor_i ) break;
		}
		if ( null == uiEventActionProcessor_i ) {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}
		return uiEventActionProcessor_i;
	}

}
