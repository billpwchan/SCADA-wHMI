package com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIDialogMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIDialogMgr() {}
	private static UIDialogMgr instance = null;
	public static UIDialogMgr getInstance() {
		if ( null == instance ) { 
			instance = new UIDialogMgr();
		}
		return instance;
	}
	
	private HashMap<String, UIDialogMgrFactory> hashMap = new HashMap<String, UIDialogMgrFactory>();
	public void addUIDialogMgrFactory(String key, UIDialogMgrFactory uiDialogMgrFactory) {
		String function = "addDialogs";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		hashMap.put(className, uiDialogMgrFactory);
		logger.end(className, function);
	}
	public void removeUIDialogMgrFactory(String key) { hashMap.remove(key); }
	public void clearUIDialogMgrFactorys() { this.hashMap.clear(); }
	
	public UIDialog_i getDialog(String key) {
		String function = "getDialog";
		logger.begin(className, function);

		logger.info(className, function, "key[{}]", key);
		UIDialog_i uiDialog_i = null;
		for ( String k : hashMap.keySet() ) {
			UIDialogMgrFactory v = hashMap.get(k);
			if ( null != k ) {
				uiDialog_i = v.getUIDialog(key);
			} else {
				logger.warn(className, function, "v from the k[{}] IS NULL", k);
			}
			
			if ( null != uiDialog_i ) break;
		}
		if ( null == uiDialog_i ) {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}
		
		logger.begin(className, function);
		
		return uiDialog_i;
	}
}
