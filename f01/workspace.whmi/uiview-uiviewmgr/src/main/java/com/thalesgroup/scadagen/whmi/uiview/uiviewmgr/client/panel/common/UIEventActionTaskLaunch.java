package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch_i.UIEventActionTaskLaunchAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

public class UIEventActionTaskLaunch extends UIEventActionExecute_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
		
	public UIEventActionTaskLaunch() {
		supportedActions = new String[]{
				UIEventActionTaskLaunchAction.UITaskLaunch.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		if ( logger.isDebugEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				logger.debug(function, "entry.getKey[{}] entry.getValue[{}]", entry.getKey(), entry.getValue());
			}
		}
		
		String strOperationString1			= (String) action.getParameter(ActionAttribute.OperationString1.toString());

		if ( strOperationString1.equals(UIEventActionTaskLaunchAction.UITaskLaunch.toString()) ) {
			
			String strOperationString2			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strOperationString3			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strOperationString4			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strOperationString5			= (String) action.getParameter(ActionAttribute.OperationString5.toString());

			boolean isValid = false;
			
			int screen = 0;
			try {
				screen = Integer.parseInt(strOperationString3);
				if ( screen < 0 ) screen = uiNameCard.getUiScreen();
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strOperationString3[{}] NumberFormatException", strOperationString3);
			}
			if ( isValid ) {
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setUiPath(strOperationString2);
				taskLaunch.setTaskUiScreen(screen);
				taskLaunch.setHeader(strOperationString4);
				taskLaunch.setExecute(strOperationString5);
				
				logger.debug(function, "fire event task strOperationString1[{}]", strOperationString1);
				
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			} else {
				logger.warn(function, "strOperationString3[{}] IS INVALID", strOperationString3);
			}

		} else if ( strOperationString1.equals(UIEventActionTaskLaunchAction.UITaskLaunch_UIWidgetMgrFactory.toString()) ) {
			
			String strUiPath			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strUiScreen			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strUiCtrl			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strUiView			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strUiOpts			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			String strUiElem			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
			
			logger.debug(function, "strUiPath[{}]", strUiPath);
			logger.debug(function, "strUiScreen[{}]", strUiScreen);
			logger.debug(function, "strUiCtrl[{}]", strUiCtrl);
			logger.debug(function, "strUiView[{}]", strUiView);
			logger.debug(function, "strUiOpts[{}]", strUiOpts);
			logger.debug(function, "strUiElem[{}]", strUiElem);
			
			boolean isValid = false;
			
			int screen = 0;
			try {
				screen = Integer.parseInt(strUiScreen);
				if ( screen < 0 ) screen = uiNameCard.getUiScreen();
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strUiScreen[{}] NumberFormatException", strUiScreen);
			}
			if ( isValid ) {
				UITaskLaunch uiTaskLaunch = new UITaskLaunch();
				uiTaskLaunch.setUiPath(strUiPath);
				uiTaskLaunch.setTaskUiScreen(screen);
				uiTaskLaunch.setUiCtrl(strUiCtrl);
				uiTaskLaunch.setUiView(strUiView);
				uiTaskLaunch.setUiOpts(strUiOpts);
				uiTaskLaunch.setUiElem(strUiElem);
				
				logger.debug(function, "fire event task strOperationString1[{}]", strOperationString1);
				
				uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));	
			} else {
				logger.warn(function, "strUiScreen[{}] IS INVALID", strUiScreen);
			}

		}

		logger.end(function);
		return bContinue;
	}
}
