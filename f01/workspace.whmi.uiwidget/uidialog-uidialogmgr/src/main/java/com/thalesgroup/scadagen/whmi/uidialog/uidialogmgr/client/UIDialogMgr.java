package com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgr_i;
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
	
	private HashMap<String, UIDialogMgr_i> dialogs = new HashMap<String, UIDialogMgr_i>();
	public void addDialogs(String key, UIDialogMgr_i dialog) {
		String function = "addDialogs";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		this.dialogs.put(key, dialog);
		logger.end(className, function);
	}
	
	public UIDialog_i getDialog(String key) {
		String function = "getDialog";
		
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		
		UIDialogMgr_i uiDialogMsgMgr_i = null;
		UIDialog_i uiDialog_i = null;
		uiDialogMsgMgr_i = dialogs.get(key);
		if ( null != uiDialogMsgMgr_i ) {
			uiDialog_i = uiDialogMsgMgr_i.getDialog();	
		} else {
			logger.warn(className, function, "key[{}] uiDialog_i IS NULL", key);
		}
		
		logger.begin(className, function);
		
		return uiDialog_i;
	}
}
