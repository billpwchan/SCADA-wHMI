package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;

public class UIEventActionExecuteMgr {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionExecuteMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private static UIEventActionExecuteMgr instance = null;
	public static UIEventActionExecuteMgr getInstance() { 
		if ( null == instance ) 
			instance = new UIEventActionExecuteMgr();
		return instance; }
	private UIEventActionExecuteMgr() {}
	public UIEventActionExecute_i getUIEventActionExecute(String key) {
		final String function = "getUIEventActionExecute";
		logger.info(className, function, "key[{}]", key);
		UIEventActionExecute_i uiEventActionExecute = null;
		if ( key.equals(UIActionEventType.widget.toString()) ) {
			uiEventActionExecute = new UIEventActionWidget();
		} else if ( key.equals(UIActionEventType.dialogmsg.toString()) ) {
			uiEventActionExecute = new UIEventActionDialogMsg();
		} else if ( key.equals(UIActionEventType.opm.toString()) ) {
			uiEventActionExecute = new UIEventActionOpm();
		} else if ( key.equals(UIActionEventType.ctl.toString()) ) {
			uiEventActionExecute = new UIEventActionCtrl();
		} else if ( key.equals(UIActionEventType.dpc.toString()) ) {
			uiEventActionExecute = new UIEventActionDpc();
		} else if ( key.equals(UIActionEventType.dbm.toString()) ) {
			uiEventActionExecute = new UIEventActionDbm();
		} else if ( key.equals(UIActionEventType.uitask.toString()) ) {
			uiEventActionExecute = new UIEventActionTaskLaunch();
		}
		return uiEventActionExecute;
	}
}
