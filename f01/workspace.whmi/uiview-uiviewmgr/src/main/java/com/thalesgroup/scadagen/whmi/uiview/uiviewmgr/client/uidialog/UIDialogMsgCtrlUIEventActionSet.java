package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorCore_i;

public class UIDialogMsgCtrlUIEventActionSet implements UIDialogMsgCtrl_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogMsgCtrlUIEventActionSet.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String actionsetkey = null;
	private UIEventActionProcessorCore_i uiEventActionProcessorCore_i = null;
	private Map<String, Map<String, Object>> override = null;
	public UIDialogMsgCtrlUIEventActionSet(UIEventActionProcessorCore_i uiEventActionProcessorCore_i, String actionsetkey, Map<String, Map<String, Object>> override) {
		this.uiEventActionProcessorCore_i = uiEventActionProcessorCore_i;
		this.actionsetkey = actionsetkey;
		this.override = override;
	}
	@Override
	public void response() {
		String function = "response";
		logger.begin(className, function);
		if ( null != uiEventActionProcessorCore_i ) {
			logger.debug(className, function, "call uieventactionprocess executeactionset");
			uiEventActionProcessorCore_i.executeActionSet(actionsetkey, override);	
		} else {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}
		logger.end(className, function);
	}

}
