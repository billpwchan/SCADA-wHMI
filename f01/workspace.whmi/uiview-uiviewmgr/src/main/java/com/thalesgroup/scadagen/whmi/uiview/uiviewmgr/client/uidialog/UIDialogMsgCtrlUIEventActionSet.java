package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;

public class UIDialogMsgCtrlUIEventActionSet implements UIDialogMsgCtrl_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogMsgCtrlUIEventActionSet.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String actionsetkey = null;
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	public UIDialogMsgCtrlUIEventActionSet(UIEventActionProcessor_i uiEventActionProcessor_i, String actionsetkey) {
		this.uiEventActionProcessor_i = uiEventActionProcessor_i;
		this.actionsetkey = actionsetkey;
	}
	@Override
	public void response() {
		String function = "response";
		logger.begin(className, function);
		if ( null != uiEventActionProcessor_i ) {
			logger.debug(className, function, "call uieventactionprocess executeactionset");
			uiEventActionProcessor_i.executeActionSet(actionsetkey);	
		} else {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}
		logger.end(className, function);
	}

}
