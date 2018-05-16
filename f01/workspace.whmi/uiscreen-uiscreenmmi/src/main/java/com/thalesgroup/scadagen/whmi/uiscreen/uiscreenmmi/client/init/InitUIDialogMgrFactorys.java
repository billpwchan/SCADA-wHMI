package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;

public class InitUIDialogMgrFactorys {
	
	private final static String name = InitUIDialogMgrFactorys.class.getName();
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(InitUIDialogMgrFactorys.class.getName());
	
	public static void init() {
		String function = "init";
		logger.begin(function);
		
		UIDialogMgr uiDialogMgr = UIDialogMgr.getInstance();
		uiDialogMgr.clearUIDialogMgrFactorys();
		uiDialogMgr.addUIDialogMgrFactory(name, new UIDialogMgrFactory() {
			
			@Override
			public UIDialog_i getUIDialog(String key) {
				final String function = "getUIDialog";
				logger.info(function, "key[{}]", key);
				UIDialog_i uiDialog_i = null;
				if (
						UIDialogMsg.class.getSimpleName()
						.equals(key)
						) {
					uiDialog_i = new UIDialogMsg();
				}
				
				if ( null == uiDialog_i ) logger.warn(function, "key[{}] uiDialog_i IS NULL", key);
				
				return uiDialog_i;
			}
		});
		
		logger.end(function);
	}
}
