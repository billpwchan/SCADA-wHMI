package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTsc_i.UIEventActionTscAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr;

public class UIEventActionTsc extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionTsc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionTsc ( ) {
		supportedActions = new String[] {
				  UIEventActionTscAction.SetStartTime.toString()
				, UIEventActionTscAction.SetFilter.toString()
				, UIEventActionTscAction.SetEndTime.toString()
				, UIEventActionTscAction.SetDescription.toString()
				, UIEventActionTscAction.GetTaskNames.toString()
				, UIEventActionTscAction.GetStartTime.toString()
				, UIEventActionTscAction.GetRemoveAtEnd.toString()
				, UIEventActionTscAction.GetLog.toString()
				, UIEventActionTscAction.GetInterval.toString()
				, UIEventActionTscAction.GetFilter.toString()
				, UIEventActionTscAction.GetEndTime.toString()
				, UIEventActionTscAction.GetDescription.toString()
				, UIEventActionTscAction.GetCommand.toString()
				, UIEventActionTscAction.GetArguments.toString()
				, UIEventActionTscAction.RemoveTask.toString()
				, UIEventActionTscAction.IsEnabled.toString()
				, UIEventActionTscAction.EnableTask.toString()
				, UIEventActionTscAction.DisableTask.toString()
				, UIEventActionTscAction.AddTask.toString()
		};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		TscMgr tscMgr = (TscMgr) TscMgr.getInstance(className);
		
		if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetStartTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strStartTime			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			long startTime = Long.parseLong(strStartTime);
			
			tscMgr.setStartTimeRequest(strClientKey, strScsEnvId, strTaskName, startTime, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetFilter.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strFilter			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setFilterRequest(strClientKey, strScsEnvId, strTaskName, strFilter, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetEndTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strEndTime			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			long endTime = Long.parseLong(strEndTime);
			
			tscMgr.setEndTimeRequest(strClientKey, strScsEnvId, strTaskName, endTime, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetDescription.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strDescription		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setDescriptionRequest(strClientKey, strScsEnvId, strTaskName, strDescription, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetTaskNames.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			
			tscMgr.getTaskNamesRequest(strClientKey, strScsEnvId);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetStartTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getStartTimeRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetRemoveAtEnd.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getRemoveAtEndRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetLog.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getLogRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetInterval.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getIntervalRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetFilter.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getFilterRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetEndTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getEndTimeRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetDescription.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getDescriptionRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetCommand.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getCommandRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetArguments.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getArgumentsRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.RemoveTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.removeTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.IsEnabled.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.isEnabledRequest(strClientKey, strScsEnvId, strTaskName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.EnableTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.enableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.DisableTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.disableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.AddTask.toString()) ) {
		
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.addTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
		
		}
		
		logger.end(className, function);
		return bContinue;
	}
}
