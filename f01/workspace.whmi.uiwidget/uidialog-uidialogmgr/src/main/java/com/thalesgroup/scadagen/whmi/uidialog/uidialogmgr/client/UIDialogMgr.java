package com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIDialogMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private UIDialogMgr() {}
	private static UIDialogMgr instance = null;
	public static UIDialogMgr getInstance() {
		if ( null == instance ) { 
			instance = new UIDialogMgr();
		}
		return instance;
	}
	
	private Map<String, UIDialogMgrFactory> hashMap = new HashMap<String, UIDialogMgrFactory>();
	public void addUIDialogMgrFactory(String key, UIDialogMgrFactory uiDialogMgrFactory) {
		String function = "addDialogs";
		logger.begin(function);
		logger.info(function, "key[{}]", key);
		hashMap.put(key, uiDialogMgrFactory);
		logger.end(function);
	}
	public void removeUIDialogMgrFactory(String key) { hashMap.remove(key); }
	public void clearUIDialogMgrFactorys() { this.hashMap.clear(); }
	
	public UIDialog_i getDialog(String key) {
		String function = "getDialog";
		logger.begin(function);

		logger.info(function, "key[{}]", key);
		UIDialog_i uiDialog_i = null;
		for ( String k : hashMap.keySet() ) {
			UIDialogMgrFactory v = hashMap.get(k);
			if ( null != k ) {
				uiDialog_i = v.getUIDialog(key);
			} else {
				logger.warn(function, "v from the k[{}] IS NULL", k);
			}
			
			if ( null != uiDialog_i ) break;
		}
		if ( null == uiDialog_i ) {
			logger.warn(function, "uiDialog_i IS NULL");
		}
		
		logger.begin(function);
		
		return uiDialog_i;
	}
}
