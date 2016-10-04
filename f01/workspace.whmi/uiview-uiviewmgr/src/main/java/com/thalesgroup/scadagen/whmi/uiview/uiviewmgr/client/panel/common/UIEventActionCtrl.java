package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.WidgetExecuteAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;

public class UIEventActionCtrl {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionCtrl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	private String instance = "";
	
	public UIEventActionCtrl ( String logPrefix, String instance ) {
		this.logPrefix = logPrefix;
		this.instance = instance;
	}
	
	public void execute(UIEventAction action) {
		final String function = logPrefix+" execute";
		logger.begin(className, function);
		String strAction			= (String) action.getParameter(WidgetExecuteAttribute.OperationString1.toString());
		String strEnvName			= (String) action.getParameter(WidgetExecuteAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(WidgetExecuteAttribute.OperationString3.toString());
		String strCommandValue		= (String) action.getParameter(WidgetExecuteAttribute.OperationString4.toString());
		String strBypassInitCond	= (String) action.getParameter(WidgetExecuteAttribute.OperationString5.toString());
		String strBypassRetCond		= (String) action.getParameter(WidgetExecuteAttribute.OperationString6.toString());
		String strSendAnyway		= (String) action.getParameter(WidgetExecuteAttribute.OperationString7.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		CtlMgr ctlMgr = CtlMgr.getInstance(instance);
		
		int bypassInitCond	= Integer.parseInt(strBypassInitCond);
		int bypassRetCond	= Integer.parseInt(strBypassRetCond);
		int sendAnyway		= Integer.parseInt(strSendAnyway);
		
		if ( strAction.equals("SendControlInt") ) {
			int intCommandValue	= Integer.parseInt(strCommandValue);
			logger.info(className, function, "intCommandValue[{}]", intCommandValue);
			ctlMgr.sendControl(strEnvName, new String[]{strAddress}, intCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
		} else if ( strAction.equals("SendControlFloat") ) {
			float floatCommandValue	= Float.parseFloat(strCommandValue);
			logger.info(className, function, "floatCommandValue[{}]", floatCommandValue);
			ctlMgr.sendControl(strEnvName, new String[]{strAddress}, floatCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
		} else if ( strAction.equals("SendControlString") ) {
			logger.info(className, function, "strCommandValue[{}]", strCommandValue);
			ctlMgr.sendControl(strEnvName, new String[]{strAddress}, strCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
		}
		logger.end(className, function);
	}
}
