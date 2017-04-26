package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionAlm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBusFire;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionCtrl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDialogMsg;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDpc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionGrc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecuteMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;

public class InitUIEventActionExecuteMgrFactorys {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitUIEventActionExecuteMgrFactorys.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public static void init() {
		String function = "init";
		logger.begin(className, function);
		
		UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
		uiEventActionExecuteMgr.clearUIEventActionExecuteMgrFactorys();
		uiEventActionExecuteMgr.addUIEventActionExecute(className, new UIEventActionExecuteMgrFactory() {
			
			@Override
			public UIEventActionExecute_i getUIEventActionExecute(String key) {
				final String function = "getUIEventActionExecute";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionExecute_i uiEventActionExecute_i = null;
				
				if ( key.equals(UIActionEventType.alm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionAlm();
				}
				else if ( key.equals(UIActionEventType.ctl.toString()) ) {
					uiEventActionExecute_i = new UIEventActionCtrl();
				}
				else if ( key.equals(UIActionEventType.dbm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDbm();
				}
				else if ( key.equals(UIActionEventType.dialogmsg.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDialogMsg();
				}
				else if ( key.equals(UIActionEventType.dpc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDpc();
				}
				else if ( key.equals(UIActionEventType.grc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionGrc();
				}
				else if ( key.equals(UIActionEventType.opm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionOpm();
				}
				else if ( key.equals(UIActionEventType.uitask.toString()) ) {
					uiEventActionExecute_i = new UIEventActionTaskLaunch();
				}
				else if ( key.equals(UIActionEventType.widget.toString()) ) {
					uiEventActionExecute_i = new UIEventActionWidget();
				}
				else if ( key.equals(UIActionEventType.event.toString()) ) {
					uiEventActionExecute_i = new UIEventActionBusFire();
				}
				
				if ( null == uiEventActionExecute_i ) logger.warn(className, function, "key[{}] uiEventActionExecute_i IS NULL", key);
				
				return uiEventActionExecute_i;
			}
		});
		
		logger.end(className, function);
	}
}
