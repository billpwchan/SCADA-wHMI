package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionCtrl_i.UIEventActionCtrlAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;

public class UIEventActionCtrl extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionCtrl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionCtrl ( ) {
		supportedActions = new String[] {
				  UIEventActionCtrlAction.SendIntControl.toString()
				, UIEventActionCtrlAction.SendFloatControl.toString()
				, UIEventActionCtrlAction.SendStringControl.toString()};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strEnvName			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strCommandValue		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		String strBypassInitCond	= (String) action.getParameter(ActionAttribute.OperationString5.toString());
		String strBypassRetCond		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
		String strSendAnyway		= (String) action.getParameter(ActionAttribute.OperationString7.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		boolean isValid = false;
		
		int bypassInitCond	= -1;
		int bypassRetCond	= -1;
		int sendAnyway		= -1;
		
		try {
			bypassInitCond	= Integer.parseInt(strBypassInitCond);
			bypassRetCond	= Integer.parseInt(strBypassRetCond);
			sendAnyway		= Integer.parseInt(strSendAnyway);
			isValid = true;
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strBypassInitCond[}] strBypassRetCond[{}] strSendAnyway[{}] IS INVALID", new Object[]{strBypassInitCond, strBypassRetCond, strSendAnyway});
		}
		if ( isValid ) {
			CtlMgr ctlMgr = CtlMgr.getInstance(instance);
			
			if ( strAction.equalsIgnoreCase(UIEventActionCtrlAction.SendIntControl.toString()) ) {
				int intCommandValue = -1;
				boolean isValidCommandValue = false;
				try {
					intCommandValue	= Integer.parseInt(strCommandValue);
					isValidCommandValue = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "strCommandValue[{}] NumberFormatException", strCommandValue);
				}
				if ( isValidCommandValue ) {
					logger.info(className, function, "intCommandValue[{}]", intCommandValue);
					ctlMgr.sendControl(strEnvName, new String[]{strAddress}, intCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
				} else {
					logger.warn(className, function, "isValidCommandValue IS INVALID");
				}
	
			} else if ( strAction.equalsIgnoreCase(UIEventActionCtrlAction.SendFloatControl.toString()) ) {
				float floatCommandValue = -1;
				boolean isValidCommandValue = false;
				try {
					floatCommandValue	= Float.parseFloat(strCommandValue);
					isValidCommandValue = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "strCommandValue[{}] NumberFormatException", strCommandValue);
				}
				if ( isValidCommandValue ) {
					logger.info(className, function, "floatCommandValue[{}]", floatCommandValue);
					ctlMgr.sendControl(strEnvName, new String[]{strAddress}, floatCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
				} else {
					logger.warn(className, function, "floatCommandValue IS INVALID");
				}
			} else if ( strAction.equals(UIEventActionCtrlAction.SendStringControl.toString()) ) {
				logger.info(className, function, "strCommandValue[{}]", strCommandValue);
				ctlMgr.sendControl(strEnvName, new String[]{strAddress}, strCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
			}
		} else {
			logger.warn(className, function, "command details IS INVALID");
		}
		
		logger.end(className, function);
		return bContinue;
	}
}
