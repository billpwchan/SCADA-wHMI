package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventType_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionAlm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBusFire;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionCtrl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDialogMsg;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDpc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionGrc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionJS;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOls;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionSimultaneousLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecuteMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;

public class InitUIEventActionExecuteMgrFactorys {
	
	private final static String name = InitUIEventActionExecuteMgrFactorys.class.getName();
	private final static String className = UIWidgetUtil.getClassSimpleName(name);
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void init() {
		String function = "init";
		logger.begin(className, function);
	
		UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
		uiEventActionExecuteMgr.removeUIEventActionExecuteMgrFactory(name);
		uiEventActionExecuteMgr.addUIEventActionExecute(name, new UIEventActionExecuteMgrFactory() {
			
			@Override
			public UIEventActionExecute_i getUIEventActionExecute(String key) {
				final String function = "getUIEventActionExecute";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionExecute_i uiEventActionExecute_i = null;
				
				if ( key.equals(UIActionEventType_i.UIActionEventType.alm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionAlm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.ctl.toString()) ) {
					uiEventActionExecute_i = new UIEventActionCtrl();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dbm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDbm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dialogmsg.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDialogMsg();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dpc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDpc();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.grc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionGrc();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.opm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionOpm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.simlogin.toString()) ) {
					uiEventActionExecute_i = new UIEventActionSimultaneousLogin();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.uitask.toString()) ) {
					uiEventActionExecute_i = new UIEventActionTaskLaunch();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.widget.toString()) ) {
					uiEventActionExecute_i = new UIEventActionWidget();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.event.toString()) ) {
					uiEventActionExecute_i = new UIEventActionBusFire();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.js.toString()) ) {
					uiEventActionExecute_i = new UIEventActionJS();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.ols.toString()) ) {
					uiEventActionExecute_i = new UIEventActionOls();
				}
				
				if ( null == uiEventActionExecute_i ) logger.warn(className, function, "key[{}] uiEventActionExecute_i IS NULL", key);
				
				return uiEventActionExecute_i;
			}
		});
	
	}
}
