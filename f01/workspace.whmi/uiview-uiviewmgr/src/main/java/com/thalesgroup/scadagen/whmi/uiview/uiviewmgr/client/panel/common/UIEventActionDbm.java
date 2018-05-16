package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm_i.UIEventActionDbmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class UIEventActionDbm extends UIEventActionExecute_i {
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionDbm() {
		supportedActions = new String[]{
				  UIEventActionDbmAction.WriteIntValue.toString()
				, UIEventActionDbmAction.WriteFloatValue.toString()
				, UIEventActionDbmAction.WriteStringValue.toString()
				};
	}
	
	private final static String strProperties				= "properties";
	private final static String dictionariesCacheName		= "UIInspectorPanel";
	private final static String strPropPrefix				= "UIEventAction.Dbm.";
	private final static String strPropName	 				= strPropPrefix+strProperties;
	private final static String strDatabaseMultiReadingKey	= strPropPrefix+"DatabaseWritingKey";

	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strValue				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		if ( logger.isTraceEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				if ( null != obj ) logger.trace(function, "key[{}] obj[{}]", key, obj);
			}
		}

		String databaseWritingKey = "DatabaseWritingSingleton";
		databaseWritingKey = ReadProp.readString(dictionariesCacheName, strPropName, strDatabaseMultiReadingKey, databaseWritingKey);
		logger.debug(function, "databaseWritingKey[{}]", databaseWritingKey);
		
		DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(databaseWritingKey);
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
					logger.warn(function, "strValue[{}] NumberFormatException", strValue);
				}
				if ( isValid ) {
					databaseWriting_i.addWriteIntValueRequest(key, strScsEnvId, strAddress, value);
				} else {
					logger.warn(function, "strValue[{}] IS INVALID", strValue);
				}
			} else if ( strAction.equals(UIEventActionDbmAction.WriteFloatValue.toString()) ) {
				boolean isValid = false;
				float value = 0;
				try {
					value = Float.parseFloat(strValue);
					isValid = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(function, "strValue[{}] NumberFormatException", strValue);
				}
				if ( isValid ) {
					databaseWriting_i.addWriteFloatValueRequest(key, strScsEnvId, strAddress, value);
				} else {
					logger.warn(function, "strValue[{}] IS INVALID", strValue);
				}
			} else if ( strAction.equals(UIEventActionDbmAction.WriteStringValue.toString()) ) {
				databaseWriting_i.addWriteStringValueRequest(key, strScsEnvId, strAddress, strValue);
			}
			
			databaseWriting_i.disconnect();
			
			databaseWriting_i = null;
			
		} else {
			logger.warn(function, "databaseWritingKey IS INVALID", databaseWritingKey);
		}

		logger.end(function);
		return bContinue;
	}
}
