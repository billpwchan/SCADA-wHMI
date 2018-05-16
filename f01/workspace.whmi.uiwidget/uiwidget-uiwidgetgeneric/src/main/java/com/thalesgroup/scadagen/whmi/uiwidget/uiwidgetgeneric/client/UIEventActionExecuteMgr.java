package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecuteMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

public class UIEventActionExecuteMgr {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIEventActionExecuteMgr instance = null;
	public static UIEventActionExecuteMgr getInstance() { 
		if ( null == instance ) 
			instance = new UIEventActionExecuteMgr();
		return instance; }
	private UIEventActionExecuteMgr() {}
	
	private Map<String, UIEventActionExecuteMgrFactory> hashMap = new HashMap<String, UIEventActionExecuteMgrFactory>();
	public void addUIEventActionExecute(String key, UIEventActionExecuteMgrFactory uiEventActionExecuteMgrFactory) {
		hashMap.put(key, uiEventActionExecuteMgrFactory);
	}
	public void removeUIEventActionExecuteMgrFactory(String key) { hashMap.remove(key); }
	public void clearUIEventActionExecuteMgrFactorys() { this.hashMap.clear(); }
	
	public UIEventActionExecute_i getUIEventActionExecute(String key) {
		final String function = "getUIEventActionExecute";
		logger.info(function, "key[{}]", key);
		UIEventActionExecute_i uiEventActionExecute = null;
		for ( String k : hashMap.keySet() ) {
			UIEventActionExecuteMgrFactory v = hashMap.get(k);
			if ( null != k ) {
				uiEventActionExecute = v.getUIEventActionExecute(key);
			} else {
				logger.warn(function, "v from the k[{}] IS NULL", k);
			}
			
			if ( null != uiEventActionExecute ) break;
		}
		if ( null == uiEventActionExecute ) {
			logger.warn(function, "uiEventActionExecute IS NULL");
		}
		return uiEventActionExecute;
	}
}