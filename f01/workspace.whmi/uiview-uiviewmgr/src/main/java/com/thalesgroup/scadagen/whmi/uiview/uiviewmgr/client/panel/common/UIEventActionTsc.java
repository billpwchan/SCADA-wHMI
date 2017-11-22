package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTsc_i.UIEventActionTscAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr;

public class UIEventActionTsc extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionTsc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionTsc ( ) {
		supportedActions = UIEventActionTscAction.toStrings();
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
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetInterval.toString()) ) {
		
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strInterval			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			int interval = Integer.parseInt(strInterval);
			
			tscMgr.setIntervalRequest(strClientKey, strScsEnvId, strTaskName, interval, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetFilter.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strFilter			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setFilterRequest(strClientKey, strScsEnvId, strTaskName, strFilter, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetEndTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strEndTime			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			long endTime = Long.parseLong(strEndTime);
			
			tscMgr.setEndTimeRequest(strClientKey, strScsEnvId, strTaskName, endTime, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetDescription.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strDescription		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setDescriptionRequest(strClientKey, strScsEnvId, strTaskName, strDescription, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetDates.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strDgid				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strDates				= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			int dgid = Integer.parseInt(strDgid);
			
			String [] datesarr = strDates.split(",");
			
			int [] dates = new int[datesarr.length];
			for ( int i = 0 ; i < dates.length ; ++i ) {
				dates[i] = Integer.parseInt(datesarr[i]);
			}
			
			tscMgr.setDatesRequest(strClientKey, strScsEnvId, dgid, dates, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetCommand.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strCommand			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setCommandRequest(strClientKey, strScsEnvId, strTaskName, strCommand, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetTaskArguments.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strArguments			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			int arguments = Integer.parseInt(strArguments);
			
			tscMgr.setTaskArgumentsRequest(strClientKey, strScsEnvId, strTaskName, arguments, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.SetArguments.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strArguments			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			
			tscMgr.setArgumentsRequest(strClientKey, strScsEnvId, strTaskName, strArguments, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetTaskType.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getTaskTypeRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetTaskNames.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			
			tscMgr.getTaskNamesRequest(strClientKey, strScsEnvId);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetStartTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getStartTimeRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetRemoveAtEnd.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getRemoveAtEndRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetLog.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getLogRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetInterval.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getIntervalRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetFilter.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getFilterRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetEndTime.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getEndTimeRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetDescription.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getDescriptionRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetDayGroupNamesAndIds.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());

			tscMgr.getDayGroupNamesAndIdsRequest(strClientKey, strScsEnvId);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetDates.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			
			String strId				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			int id = Integer.parseInt(strId);
			
			tscMgr.getDatesRequest(strClientKey, strScsEnvId, id);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetCommand.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getCommandRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.GetArguments.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.getArgumentsRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.RemoveTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.removeTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.IsEnabled.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			tscMgr.isEnabledRequest(strClientKey, strScsEnvId, strTaskName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.EnableTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.enableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.DisableTask.toString()) ) {
			
			String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			String strClientName		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			
			tscMgr.disableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.AddCompleteTask.toString()) ) {
				
				String strClientKey			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
				String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
				String strTaskName			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
				String strDesription		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
				String strCommand			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
				String strArguments			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
				String strStartTime			= (String) action.getParameter(ActionAttribute.OperationString8.toString());
				String strEndTime			= (String) action.getParameter(ActionAttribute.OperationString9.toString());
				String strInterval			= (String) action.getParameter(ActionAttribute.OperationString10.toString());
				String strFilter			= (String) action.getParameter(ActionAttribute.OperationString11.toString());
				String strInhibited			= (String) action.getParameter(ActionAttribute.OperationString12.toString());
				String strLog				= (String) action.getParameter(ActionAttribute.OperationString13.toString());
				String strRemoveAtEnd		= (String) action.getParameter(ActionAttribute.OperationString14.toString());
				String strClientName		= (String) action.getParameter(ActionAttribute.OperationString15.toString());
				
				int arguments = Integer.parseInt(strArguments);
				int interval = Integer.parseInt(strInterval);
				int inhibited = Integer.parseInt(strInhibited);
				int log = Integer.parseInt(strLog);
				int removeAtEnd = Integer.parseInt(strRemoveAtEnd);
				
				tscMgr.addCompleteTaskRequest(strClientKey, strScsEnvId, strTaskName
						, strDesription
						, strCommand
						, arguments
						, strStartTime
						, strEndTime
						, interval
						, strFilter
						, inhibited
						, log
						, removeAtEnd						
						, strClientName);
			
		}
		else if ( strAction.equalsIgnoreCase(UIEventActionTscAction.AddTask.toString()) ) {
		
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
