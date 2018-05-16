package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOls_i.UIEventActionOlsAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr;

public class UIEventActionOls extends UIEventActionExecute_i {
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionOls ( ) {
		supportedActions = new String[] {
				  UIEventActionOlsAction.DeleteData.toString()
				, UIEventActionOlsAction.SubscribeOlsList.toString()
				, UIEventActionOlsAction.UnsubscribeOlsList.toString()
				, UIEventActionOlsAction.ReadData.toString()
				, UIEventActionOlsAction.InsertData.toString()
				, UIEventActionOlsAction.UpdateData.toString()
		};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		OlsMgr olgMgr = (OlsMgr) OlsMgr.getInstance(className);
		
		if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.DeleteData.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strListName			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strFieldList			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			String [] fieldLists = strFieldList.split(",");
			
			olgMgr.deleteData(strKey, strScsEnvId, strListServer, strListName, fieldLists);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.SubscribeOlsList.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strListName			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strFieldList			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			String strFilter			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
			
			String [] strFieldLists = strFieldList.split(",");
			
			olgMgr.subscribeOlsList(strKey, strScsEnvId, strListServer, strListName, strFieldLists, strFilter);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.UnsubscribeOlsList.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strSubUUID			= (String) action.getParameter(ActionAttribute.OperationString5.toString());

			olgMgr.unsubscribeOlsList(strKey, strScsEnvId, strListServer, strSubUUID);

		} else if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.ReadData.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strListName			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strFieldList			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			String strFilter			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
			
			String [] strFieldLists = strFieldList.split(",");
			
			olgMgr.readData(strKey, strScsEnvId, strListServer, strListName, strFieldLists, strFilter);

		} else if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.InsertData.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strListName			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strOlsEntry			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			String [] strOlsEntrys = strOlsEntry.split("|");
			Map<String, String> olsEntry = new LinkedHashMap<String, String>();
			for ( String entry : strOlsEntrys ) {
				String [] keyValue = entry.split(",");
				olsEntry.put(keyValue[0], keyValue[1]);
			}
			
			olgMgr.insertData(strKey, strScsEnvId, strListServer, strListName, olsEntry);

		} else if ( strAction.equalsIgnoreCase(UIEventActionOlsAction.UpdateData.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strListServer		= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strListName			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strOlsEntryKey		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			String strOlsEntry			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
			
			String [] strOlsEntrys = strOlsEntry.split("|");
			Map<String, String> olsEntry = new LinkedHashMap<String, String>();
			for ( String entry : strOlsEntrys ) {
				String [] keyValue = entry.split(",");
				olsEntry.put(keyValue[0], keyValue[1]);
			}
			
			olgMgr.updateData(strKey, strScsEnvId, strListServer, strListName, strOlsEntryKey, olsEntry);

		}
		
		logger.end(function);
		return bContinue;
	}
}
