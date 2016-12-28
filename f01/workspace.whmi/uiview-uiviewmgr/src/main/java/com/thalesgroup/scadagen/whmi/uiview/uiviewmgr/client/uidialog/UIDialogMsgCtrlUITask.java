package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIDialogMsgCtrlUITask implements UIDialogMsgCtrl_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogMsgCtrlUITask.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UINameCard uiNameCard;
	private UITask_i uiTask = null;
	public UIDialogMsgCtrlUITask(UINameCard uiNameCard, UITask_i uiTask) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiTask = uiTask;
	}
	
	@Override
	public void response() {
		String function = "response";
		logger.begin(className, function);
		if ( null != uiTask ) {
			logger.debug(className, function, "fire uiTask on uiNameCard eventbus");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTask));
		} else {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}
		logger.end(className, function);
	}
	
}
