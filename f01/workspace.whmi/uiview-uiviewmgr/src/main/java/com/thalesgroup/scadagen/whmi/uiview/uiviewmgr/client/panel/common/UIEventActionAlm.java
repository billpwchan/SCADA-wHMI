package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.alm.AlmMgr;

public class UIEventActionAlm extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionAlm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public final String strNotifyExternalAlarm = "NotifyExternalAlarm";
	public final String strNotifyExternalEvent = "NotifyExternalEvent";
	
	public UIEventActionAlm ( ) {
		supportedActions = new String[] {strNotifyExternalAlarm, strNotifyExternalEvent};
	}
	
	public void executeAction(UIEventAction action) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strConfigFileName	= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		String strClassId			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
		String strPointAlias		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
		String strObjectId			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
		String strExtSourceId		= (String) action.getParameter(ActionAttribute.OperationString7.toString());

		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		boolean isValid = false;
		
		int classId		= -1;
		int objectId	= -1;
		int extSourceId	= -1;
		
		try {
			classId			= Integer.parseInt(strClassId);
			objectId		= Integer.parseInt(strObjectId);
			extSourceId		= Integer.parseInt(strExtSourceId);
			isValid = true;
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strClassId[}] strObjectId[{}] strExtSourceId[{}] IS INVALID", new Object[]{strClassId, strObjectId, strExtSourceId});
		}
		if ( isValid ) {
			AlmMgr almMgr = AlmMgr.getInstance(className); 
			
			if ( strAction.equals(strNotifyExternalAlarm) ) {
				
				String strIsAlarm			= (String) action.getParameter(ActionAttribute.OperationString8.toString());
				String strMessage			= (String) action.getParameter(ActionAttribute.OperationString9.toString());
				
				boolean isAlarm = false;
				if ( null != strIsAlarm && strIsAlarm.equalsIgnoreCase("true") )
					isAlarm = true;
				
				almMgr.notifyExternalAlarm(strKey, strScsEnvId
						, strConfigFileName, classId, strPointAlias
		    			, objectId, extSourceId, isAlarm, strMessage
		    			);
			} else if ( strAction.equals(strNotifyExternalEvent) ) {
				
				String strMessage			= (String) action.getParameter(ActionAttribute.OperationString8.toString());
				
				almMgr.notifyExternalEvent(strKey, strScsEnvId
		    			, strConfigFileName, classId, strPointAlias
		    			, objectId, extSourceId, strMessage
		    			);
			}
		}
	}
}
