package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm_i.UIEventActionDbmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class UIEventActionDbm extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDbm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionDbm() {
		supportedActions = new String[]{
				  UIEventActionDbmAction.WriteIntValue.toString()
				, UIEventActionDbmAction.WriteFloatValue.toString()
				, UIEventActionDbmAction.WriteStringValue.toString()
				};
	}

	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
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
		
		String strDatabaseWritingKey = "DatabaseWriting";
		logger.debug(className, function, "strDatabaseWritingKey[{}]", strDatabaseWritingKey);
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingKey);
		if ( null != databaseWriting_i ) {
			
			databaseWriting_i.connect();
			
			String key = strAction + "_" + className + "_" + "dynamic" + "_" + strAddress;
			
			if ( strAction.equals(UIEventActionDbmAction.WriteIntValue.toString()) ) {
				boolean isValid = false;
				int value = 0;
				try {
					value = Integer.parseInt(strValue);
					isValid = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "strValue[{}] NumberFormatException", strValue);
				}
				if ( isValid ) {
					databaseWriting_i.addWriteIntValueRequest(key, strScsEnvId, strAddress, value);
				} else {
					logger.warn(className, function, "strValue[{}] IS INVALID", strValue);
				}
			} else if ( strAction.equals(UIEventActionDbmAction.WriteFloatValue.toString()) ) {
				boolean isValid = false;
				float value = 0;
				try {
					value = Float.parseFloat(strValue);
					isValid = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "strValue[{}] NumberFormatException", strValue);
				}
				if ( isValid ) {
					databaseWriting_i.addWriteFloatValueRequest(key, strScsEnvId, strAddress, value);
				} else {
					logger.warn(className, function, "strValue[{}] IS INVALID", strValue);
				}
			} else if ( strAction.equals(UIEventActionDbmAction.WriteStringValue.toString()) ) {
				databaseWriting_i.addWriteStringValueRequest(key, strScsEnvId, strAddress, strValue);
			}
			
			databaseWriting_i.disconnect();
			
			databaseWriting_i = null;
			
		} else {
			logger.warn(className, function, "strDatabaseWritingKey IS INVALID", strDatabaseWritingKey);
		}

		logger.end(className, function);
		return bContinue;
	}
}
