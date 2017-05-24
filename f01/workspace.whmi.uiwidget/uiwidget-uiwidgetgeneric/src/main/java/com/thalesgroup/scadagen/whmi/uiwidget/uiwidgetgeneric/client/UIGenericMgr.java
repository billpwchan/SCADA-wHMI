package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;

public class UIGenericMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIGenericMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIGenericMgr instance = null;
	public static UIGenericMgr getInstance() { 
		if ( null == instance ) 
			instance = new UIGenericMgr();
		return instance; }
	private UIGenericMgr() {}
	
	public Map<String, UIGenericMgrFactory> hashMap = new HashMap<String, UIGenericMgrFactory>();
	public void addUIGenericMgrFactory(String key, UIGenericMgrFactory uiGenericMgrFactory) {
		hashMap.put(key, uiGenericMgrFactory);	
	}
	public void removeUIGenericMgrFactory(String key) { hashMap.remove(key); }
	public void clearUIGenericMgrFactorys() { this.hashMap.clear(); }
	
	public UIGeneric getUIGeneric(String key) {
		final String function = "getUIGeneric";
		logger.info(className, function, "key[{}]", key);
		UIGeneric uiGenericMgr_i = null;
		for ( String k : hashMap.keySet() ) {
			UIGenericMgrFactory v = hashMap.get(k);
			if ( null != k ) {
				uiGenericMgr_i = v.getUIGeneric(key);
			} else {
				logger.warn(className, function, "v from the k[{}] IS NULL", k);
			}
			
			if ( null != uiGenericMgr_i ) break;
		}
		if ( null == uiGenericMgr_i ) {
			logger.warn(className, function, "uiGenericMgr_i IS NULL");
		}
		return uiGenericMgr_i;
	}
}
