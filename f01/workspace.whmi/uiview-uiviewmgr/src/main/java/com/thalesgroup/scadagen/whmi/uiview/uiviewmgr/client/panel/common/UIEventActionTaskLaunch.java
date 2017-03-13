package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch_i.UIEventActionTaskLaunchAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

public class UIEventActionTaskLaunch extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDbm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
		
	public UIEventActionTaskLaunch() {
		supportedActions = new String[]{
				UIEventActionTaskLaunchAction.UITaskLaunch.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String strOperationString1			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strOperationString2			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strOperationString3			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strOperationString4			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		String strOperationString5			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}

		if ( strOperationString1.equals(UIEventActionTaskLaunchAction.UITaskLaunch.toString()) ) {
			boolean isValid = false;
			
			int screen = 0;
			try {
				screen = Integer.parseInt(strOperationString3);
				if ( screen < 0 ) screen = uiNameCard.getUiScreen();
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "strOperationString3[{}] NumberFormatException", strOperationString3);
			}
			if ( isValid ) {
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setUiPath(strOperationString2);
				taskLaunch.setTaskUiScreen(screen);
				taskLaunch.setHeader(strOperationString4);
				taskLaunch.setExecute(strOperationString5);
				
				logger.info(className, function, "fire event task launch");
				
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			} else {
				logger.warn(className, function, "strOperationString3[{}] IS INVALID", strOperationString3);
			}

		}

		logger.end(className, function);
		return bContinue;
	}
}
