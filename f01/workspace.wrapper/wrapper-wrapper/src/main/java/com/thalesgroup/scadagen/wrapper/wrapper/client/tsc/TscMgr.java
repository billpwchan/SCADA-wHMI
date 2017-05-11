package com.thalesgroup.scadagen.wrapper.wrapper.client.tsc;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Mgr_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.subject.SubjectMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr_i.Request;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.LogUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.tsc.ITSCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.tsc.ScsTSCComponentAccess;

public class TscMgr implements Mgr_i {
	
	private String className = UIWidgetUtil.getClassSimpleName(TscMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, Mgr_i> instances = new HashMap<String, Mgr_i>();
	public static Mgr_i getInstance(String key) {
		if ( ! instances.containsKey(key) ) {
			Mgr_i mgr = new TscMgr();
			instances.put(key, mgr);
		}
		Mgr_i instance = instances.get(key);
		return instance;
	}

	public static Set<Entry<String, Mgr_i>> getInstances() {
		return instances.entrySet();
	}
	
	private SubjectMgr subjectMgr = null;
	@Override
	public void setSubject(String key, Subject subject) { subjectMgr.setSubject(key, subject); }
	@Override
	public void removeSubject(String key) { subjectMgr.removeSubject(key); }
	
	private ScsTSCComponentAccess scsTSCComponentAccess = null;
	private TscMgr () {
		
		final String function = "TscMgr";
		logger.begin(className, function);
		
		subjectMgr = new SubjectMgr();
		
		subjectMgr.setPrefix(className);
		subjectMgr.setUILogger(logger);
		
		scsTSCComponentAccess = new ScsTSCComponentAccess(new ITSCComponentClient() {
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setSetTaskArgumentsResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetTaskArguments.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetStartTimeResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetStartTime.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetIntervalResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetInterval.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetFilterResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetFilter.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetEndTimeResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetEndTime.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetDescritionResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetDescription.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetDatesResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetDates.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetCommandResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetCommand.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSetArgumentsResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.SetArguments.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setRemoveTaskResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.RemoveTask.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setIsEnabledResult(String clientKey, int status, int errorCode, String errorMessage) {
				
				final String function = Request.IsEnabled.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("status", new JSONNumber(status));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetTaskTypeResult(String clientKey, int taskType, int errorCode, String errorMessage) {
				
				final String function = Request.GetTaskType.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("taskType", new JSONNumber(taskType));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetTaskNamesResult(String clientKey, String[] taskNames, int errorCode, String errorMessage) {
				
				final String function = Request.GetTaskNames.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("taskNames", subjectMgr.getJSONArray(function, taskNames));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetStartTimeResult(String clientKey, long startTime, int errorCode, String errorMessage) {
				
				final String function = Request.GetStartTime.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("startTime", new JSONNumber(startTime));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetRemoveAtEndResult(String clientKey, int removeAtEnd, int errorCode, String errorMessage) {
				
				final String function = Request.GetRemoveAtEnd.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("removeAtEnd", new JSONNumber(removeAtEnd));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetLogResult(String clientKey, int log, int errorCode, String errorMessage) {
				
				final String function = Request.GetLog.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("log", new JSONNumber(log));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetIntervalResult(String clientKey, int interval, int errorCode, String errorMessage) {
				
				final String function = Request.GetInterval.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("interval", new JSONNumber(interval));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetFilterResult(String clientKey, String filter, int errorCode, String errorMessage) {
				
				final String function = Request.GetFilter.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("filter", new JSONString(filter));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetEndTimeResult(String clientKey, long endTime, int errorCode, String errorMessage) {
				
				final String function = Request.GetEndTime.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("endTime", new JSONNumber(endTime));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetDescriptionResult(String clientKey, String description, int errorCode, String errorMessage) {
				
				final String function = Request.GetDescription.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("description", new JSONString(description));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetDayGroupNamesAndIdsResult(String clientKey, int[] ids, String[] names, int errorCode, String errorMessage) {
				
				final String function = Request.GetDayGroupNamesAndIds.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("ids", subjectMgr.getJSONArray(function, ids));
		    	jsdata.put("names", subjectMgr.getJSONArray(function, names));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetDatesResult(String clientKey, int[] dates, int errorCode, String errorMessage) {
				
				final String function = Request.GetDates.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("dates", subjectMgr.getJSONArray(function, dates));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetCommandResult(String clientKey, String command, int errorCode, String errorMessage) {
				
				final String function = Request.GetCommand.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("command", new JSONString(command));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setGetArgumentsResult(String clientKey, String arguments, int errorCode, String errorMessage) {
		    	
				final String function = Request.GetArguments.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("arguments", new JSONString(arguments));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setEnableTaskResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.EnableTask.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);

		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setDisableTaskResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.DisableTask.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);

		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setAddTaskResult(String clientKey, int errorCode, String errorMessage) {
				
				final String function = Request.AddTask.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);

		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setAddCompleteTaskResult(String clientKey, int errorCode, String errorMessage) {

				final String function = Request.AddCompleteTask.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);

		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
		});
		
		logger.end(className, function);
	}
	
	private void logs(String function, String clientKey, String scsEnvId) {
		logger.info(className, function, "clientKey[{}]", clientKey);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
	}
	private void logs(String function, String clientKey, String scsEnvId, String taskName) {
		logs(function, clientKey, scsEnvId);
		logger.info(className, function, "taskName[{}]", taskName);
	}
	private void logs(String function, String clientKey, String scsEnvId, String taskName, String clientName) {
		logs(function, clientKey, scsEnvId, taskName);
		logger.info(className, function, "clientName[{}]", clientName);
	}
	
	public void setStartTimeRequest(String clientKey, String scsEnvId, String taskName, long startTime,
            String clientName) {
		final String function = Request.SetStartTime.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "startTime[{}]", startTime);
		scsTSCComponentAccess.setStartTimeRequest(clientKey, scsEnvId, taskName, startTime, clientName);
		
		logger.end(className, function);
	}
	public void setIntervalRequest(String clientKey, String scsEnvId, String taskName, int interval,
	            String clientName) {
		final String function = Request.SetInterval.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "interval[{}]", interval);
		scsTSCComponentAccess.setIntervalRequest(clientKey, scsEnvId, taskName, interval, clientName);
		
		logger.end(className, function);
	}
	public void setFilterRequest(String clientKey, String scsEnvId, String taskName, String filter, String clientName) {
		final String function = Request.SetFilter.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "filter[{}]", filter);
		scsTSCComponentAccess.setFilterRequest(clientKey, scsEnvId, taskName, filter, clientName);
		
		logger.end(className, function);
	}
	public void setEndTimeRequest(String clientKey, String scsEnvId, String taskName, long endTime, String clientName) {
		final String function = Request.SetEndTime.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "endTime[{}]", endTime);
		scsTSCComponentAccess.setEndTimeRequest(clientKey, scsEnvId, taskName, endTime, clientName);
		
		logger.end(className, function);
	}
	public void setDescriptionRequest(String clientKey, String scsEnvId, String taskName, String description,
            String clientName) {
		final String function = Request.SetDescription.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "description[{}]", description);
		scsTSCComponentAccess.setDescriptionRequest(clientKey, scsEnvId, taskName, description, clientName);
		
		logger.end(className, function);
	}
	public void setDatesRequest(String clientKey, String scsEnvId, int dgid, int[] dates, String clientName) {
		final String function = Request.SetDates.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId);
		logger.info(className, function, "dgid[{}]", dgid);
		LogUtil.logArray(logger, className, function, "dates", dates);
		logger.info(className, function, "clientName[{}]", clientName);
		scsTSCComponentAccess.setDatesRequest(clientKey, scsEnvId, dgid, dates, clientName);
		
		logger.end(className, function);
	}
	public void setCommandRequest(String clientKey, String scsEnvId, String taskName, String command,
            String clientName) {
		final String function = Request.SetCommand.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "command[{}]", command);
		scsTSCComponentAccess.setCommandRequest(clientKey, scsEnvId, taskName, command, clientName);
		
		logger.end(className, function);
	}
	public void setTaskArgumentsRequest(String clientKey, String scsEnvId, String taskName, int arguments,
            String clientName) {
		final String function = Request.SetTaskArguments.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "arguments[{}]", arguments);
		scsTSCComponentAccess.setTaskArgumentsRequest(clientKey, scsEnvId, taskName, arguments, clientName);
		
		logger.end(className, function);
	}
	public void setArgumentsRequest(String clientKey, String scsEnvId, String taskName, String arguments,
            String clientName) {
		final String function = Request.SetArguments.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "arguments[{}]", arguments);
		scsTSCComponentAccess.setArgumentsRequest(clientKey, scsEnvId, taskName, arguments, clientName);
		
		logger.end(className, function);
	}
	public void getTaskTypeRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetTaskType.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getTaskTypeRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getTaskNamesRequest(String clientKey, String scsEnvId) {
		final String function = Request.GetTaskNames.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId);
		scsTSCComponentAccess.getTaskNamesRequest(clientKey, scsEnvId);
		
		logger.end(className, function);
	}
	public void getStartTimeRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetStartTime.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getStartTimeRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getRemoveAtEndRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetRemoveAtEnd.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getRemoveAtEndRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getLogRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetLog.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getLogRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getIntervalRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetInterval.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getIntervalRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getFilterRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetFilter.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getFilterRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getEndTimeRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetEndTime.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getEndTimeRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getDescriptionRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetDescription.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getDescriptionRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getDayGroupNamesAndIdsRequest(String clientKey, String scsEnvId) {
		final String function = Request.GetDayGroupNamesAndIds.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId);
		scsTSCComponentAccess.getDayGroupNamesAndIdsRequest(clientKey, scsEnvId);
		
		logger.end(className, function);
	}
	public void getDatesRequest(String clientKey, String scsEnvId, int Id) {
		final String function = Request.GetDates.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId);
		logger.info(className, function, "Id[{}]", Id);
		scsTSCComponentAccess.getDatesRequest(clientKey, scsEnvId, Id);
		
		logger.end(className, function);
	}
	public void getCommandRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetCommand.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getCommandRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void getArgumentsRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.GetArguments.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.getArgumentsRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void removeTaskRequest(String clientKey, String scsEnvId, String taskName, String clientName) {
		final String function = Request.RemoveTask.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		scsTSCComponentAccess.removeTaskRequest(clientKey, scsEnvId, taskName, clientName);
		
		logger.end(className, function);
	}
	public void isEnabledRequest(String clientKey, String scsEnvId, String taskName) {
		final String function = Request.IsEnabled.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName);
		scsTSCComponentAccess.isEnabledRequest(clientKey, scsEnvId, taskName);
		
		logger.end(className, function);
	}
	public void enableTaskRequest(String clientKey, String scsEnvId, String taskName, String clientName) {
		final String function = Request.EnableTask.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		scsTSCComponentAccess.enableTaskRequest(clientKey, scsEnvId, taskName, clientName);
		
		logger.end(className, function);
	}
    public void disableTaskRequest(String clientKey, String scsEnvId, String taskName, String clientName) {
    	final String function = Request.DisableTask.toString();
    	logger.begin(className, function);
    	
    	logs(function, clientKey, scsEnvId, taskName, clientName);
    	scsTSCComponentAccess.DisableTaskRequest(clientKey, scsEnvId, taskName, clientName);
    	
    	logger.end(className, function);
    }
	public void addCompleteTaskRequest(String clientKey, String scsEnvId, String taskName, String description,
            String command, int arguments, String startTime, String entIme, int interval, String filter, int inhibited,
            int log, int removeAtEnd, String clientName) {
		final String function = Request.AddCompleteTask.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		logger.info(className, function, "description[{}]", description);
		logger.info(className, function, "command[{}]", command);
		logger.info(className, function, "arguments[{}]", arguments);
		logger.info(className, function, "startTime[{}]", startTime);
		logger.info(className, function, "entIme[{}]", entIme);
		logger.info(className, function, "interval[{}]", interval);
		logger.info(className, function, "filter[{}]", filter);
		logger.info(className, function, "inhibited[{}]", inhibited);
		logger.info(className, function, "log[{}]", log);
		logger.info(className, function, "removeAtEnd[{}]", removeAtEnd);
		
		scsTSCComponentAccess.addCompleteTaskRequest(clientKey, scsEnvId, taskName, description, command, arguments, startTime, entIme, interval, filter, inhibited, log, removeAtEnd, clientName);
		
		logger.end(className, function);
	}
	public void addTaskRequest(String clientKey, String scsEnvId, String taskName, String clientName) {
		
		final String function = Request.AddTask.toString();
		logger.begin(className, function);
		
		logs(function, clientKey, scsEnvId, taskName, clientName);
		scsTSCComponentAccess.addTaskRequest(clientKey, scsEnvId, taskName, clientName);
		
		logger.end(className, function);
	}
}
