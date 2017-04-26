package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;

public class InitUIDialogMgrFactorys {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitUIDialogMgrFactorys.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void init() {
		String function = "init";
		logger.begin(className, function);
		
		UIDialogMgr uiDialgMgr = UIDialogMgr.getInstance();
		uiDialgMgr.clearUIDialogMgrFactorys();
		uiDialgMgr.addUIDialogMgrFactory(className, new UIDialogMgrFactory() {
			
			@Override
			public UIDialog_i getUIDialog(String key) {
				final String function = "getUIDialog";
				logger.info(className, function, "key[{}]", key);
				UIDialog_i uiDialog_i = null;
				if (
						UIWidgetUtil.getClassSimpleName(UIDialogMsg.class.getName())
						.equals(key)
						) {
					uiDialog_i = new UIDialogMsg();
				}
				
				if ( null == uiDialog_i ) logger.warn(className, function, "key[{}] uiDialog_i IS NULL", key);
				
				return uiDialog_i;
			}
		});
		
		logger.end(className, function);
	}
}
