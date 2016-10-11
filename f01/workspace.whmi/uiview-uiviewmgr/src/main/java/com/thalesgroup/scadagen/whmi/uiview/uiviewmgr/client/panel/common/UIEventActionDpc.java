package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIEventActionDpc extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDpc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private final String strSendChangeValueStatus = "SendChangeValueStatus";
	
	public UIEventActionDpc ( ) {
		supportedActions = new String[] {strSendChangeValueStatus};
	}
	
	public void executeAction(UIEventAction action) {
		final String function = logPrefix+" execute";
		logger.begin(className, function);
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strEnvName			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strValidityStatus	= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		if ( strAction.equals(strSendChangeValueStatus) ) {
			String strStatus = strValidityStatus;
			int status = 0;
			boolean isValid = false;
			try {
				status = Integer.parseInt(strStatus);
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "strStatus NumberFormatException", strStatus);
			}
			
			if ( isValid ) {

				String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + status + "_" + strAddress;
					
				logger.info(className, function, "key[{}]", key);
				
				DpcMgr dpcMgr = DpcMgr.getInstance(instance);
				dpcMgr.sendChangeVarStatus(key, strEnvName, strAddress, status);
			} else {
				logger.warn(className, function, "command details IS INVALID");
			}
		}
		logger.end(className, function);
	}
}
