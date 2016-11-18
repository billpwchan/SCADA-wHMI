package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;

public class UIEventActionDbm extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDbm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strWriteIntValue = "WriteIntValue";
	private final String strWriteFloatValue = "WriteFloatValue";
	private final String strWriteStringValue = "WriteStringValue";
	
	public UIEventActionDbm() {
		supportedActions = new String[]{strWriteIntValue, strWriteFloatValue, strWriteStringValue};
	}

	public void executeAction(UIEventAction action) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strValue				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		Database database = Database.getInstance();
		
		database.connect();
		
		String key = strAction + "_" + className + "_" + "dynamic" + "_" + strAddress;
		
		if ( strAction.equals(strWriteIntValue) ) {
			boolean isValid = false;
			int value = 0;
			try {
				value = Integer.parseInt(strValue);
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "strValue[{}] NumberFormatException", strValue);
			}
			if ( isValid ) {
				database.addWriteIntValueRequest(key, strScsEnvId, strAddress, value);
			} else {
				logger.warn(className, function, "strValue[{}] IS INVALID", strValue);
			}
		} else if ( strAction.equals(strWriteFloatValue) ) {
			boolean isValid = false;
			float value = 0;
			try {
				value = Float.parseFloat(strValue);
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "strValue[{}] NumberFormatException", strValue);
			}
			if ( isValid ) {
				database.addWriteFloatValueRequest(key, strScsEnvId, strAddress, value);
			} else {
				logger.warn(className, function, "strValue[{}] IS INVALID", strValue);
			}
		} else if ( strAction.equals(strWriteStringValue) ) {
			database.addWriteStringValueRequest(key, strScsEnvId, strAddress, strValue);
		}
		
//		if ( null != database ) {
//			database.disconnect();
//		}
		
		database = null;
		
		logger.end(className, function);
	}
}
