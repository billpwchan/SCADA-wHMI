package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.tsc;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory.MgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.tsc.TscMgr_i.Request;

public class UIIWidgetVerifyTSCControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIIWidgetVerifyTSCControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strTscMgr = "TscMgr";
	
	private Subject getSubject() {
		final String function = "getSubject";
		
		logger.begin(className, function);
		
		Subject subject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "update");
				JSONObject obj = this.subject.getState();
				uiGeneric.setWidgetValue("resultvalue", obj.toString());
			}
			
		};
		observer.setSubject(subject);

		logger.end(className, function);
		
		return subject;
	}

	private void setStartTime() {
		final String function = "setStartTime";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strStartTime		= uiGeneric.getWidgetValue("starttimevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientnamevalue");
		
		long startTime = Long.parseLong(strStartTime);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setStartTimeRequest(strClientKey, strScsEnvId, strTaskName, startTime, strClientName);
		
		logger.end(className, function);
	}
	
	private void setInterval() {
		final String function = "setInterval";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strInterval		= uiGeneric.getWidgetValue("intervalvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientnamevalue");
		
		int interval = Integer.parseInt(strInterval);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setIntervalRequest(strClientKey, strScsEnvId, strTaskName, interval, strClientName);
		
		logger.end(className, function);
	}


	private void setFilter() {
		final String function = "setFilter";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strFilter		= uiGeneric.getWidgetValue("filtervalue");
		String strClientName	= uiGeneric.getWidgetValue("clientnamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setFilterRequest(strClientKey, strScsEnvId, strTaskName, strFilter, strClientName);
		
		logger.end(className, function);
	}


	private void setEndTime() {
		final String function = "setEndTime";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strEndTime		= uiGeneric.getWidgetValue("endtimevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientnamevalue");
		
		long endTime = Long.parseLong(strEndTime);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setEndTimeRequest(strClientKey, strScsEnvId, strTaskName, endTime, strClientName);
		
		logger.end(className, function);
	}


	private void setDescription() {
		final String function = "setDescription";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strDescription	= uiGeneric.getWidgetValue("descriptionvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientnamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setDescriptionRequest(strClientKey, strScsEnvId, strTaskName, strDescription, strClientName);
		
		logger.end(className, function);
	}
	
	private void setDates() {
		final String function = "setCommand";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strDgid			= uiGeneric.getWidgetValue("dgidvalue");
		String strDates			= uiGeneric.getWidgetValue("datesvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		int dgid = Integer.parseInt(strDgid);
		
		String [] datesarr = strDates.split(",");
		
		int [] dates = new int[datesarr.length];
		for ( int i = 0 ; i < dates.length ; ++i ) {
			dates[i] = Integer.parseInt(datesarr[i]);
		}
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setDatesRequest(strClientKey, strScsEnvId, dgid, dates, strClientName);
		
		logger.end(className, function);
	}

	private void setCommand() {
		final String function = "setCommand";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strCommand		= uiGeneric.getWidgetValue("commandvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setCommandRequest(strClientKey, strScsEnvId, strTaskName, strCommand, strClientName);
		
		logger.end(className, function);
	}
	
	private void setTaskArguments() {
		final String function = "setTaskArguments";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strArguments		= uiGeneric.getWidgetValue("argumentsvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		int arguments = Integer.parseInt(strArguments);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setTaskArgumentsRequest(strClientKey, strScsEnvId, strTaskName, arguments, strClientName);
		
		logger.end(className, function);
	}
	
	private void setArguments() {
		final String function = "setArguments";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strArguments		= uiGeneric.getWidgetValue("argumentsvalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.setArgumentsRequest(strClientKey, strScsEnvId, strTaskName, strArguments, strClientName);
		
		logger.end(className, function);
	}
	
	private void getTaskType() {
		final String function = "getTaskType";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getTaskTypeRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}

	private void getTaskNames() {
		final String function = "getTaskNames";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getTaskNamesRequest(strClientKey, strScsEnvId);
		
		logger.end(className, function);
	}


	private void getStartTime() {
		final String function = "getStartTime";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getStartTimeRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getRemoveAtEnd() {
		final String function = "getRemoveAtEnd";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getRemoveAtEndRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getLog() {
		final String function = "getLog";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getLogRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getInterval() {
		final String function = "getInterval";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getIntervalRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getFilter() {
		final String function = "getFilter";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getFilterRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getEndTime() {
		final String function = "getEndTime";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getEndTimeRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getDescription() {
		final String function = "getDescription";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getDescriptionRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}
	
	
	private void getDayGroupNamesAndIds() {
		final String function = "getDayGroupNamesAndIds";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getDayGroupNamesAndIdsRequest(strClientKey, strScsEnvId);
		
		logger.end(className, function);
	}
	
	private void getDates() {
		final String function = "getDates";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strId			= uiGeneric.getWidgetValue("idvalue");
		
		int id = Integer.parseInt(strId);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getDatesRequest(strClientKey, strScsEnvId, id);
		
		logger.end(className, function);
	}


	private void getCommand() {
		final String function = "getCommand";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getCommandRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void getArguments() {
		final String function = "getArguments";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.getArgumentsRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void removeTask() {
		final String function = "removeTask";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.removeTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
		
		logger.end(className, function);
	}


	private void isEnabled() {
		final String function = "isEnabled";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.isEnabledRequest(strClientKey, strScsEnvId, strTaskName);
		
		logger.end(className, function);
	}


	private void enableTask() {
		final String function = "enableTask";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.enableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
		
		logger.end(className, function);
	}


	private void disableTask() {
		final String function = "disableTask";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.disableTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
		
		logger.end(className, function);
	}
	
	
	private void addCompleteTask() {
		final String function = "addCompleteTask";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		String strDescription	= uiGeneric.getWidgetValue("descriptionvalue");
		String strCommand		= uiGeneric.getWidgetValue("commandvalue");
		String strArguments		= uiGeneric.getWidgetValue("argumentsvalue");
		String strStartTime		= uiGeneric.getWidgetValue("starttimevalue");
		String strEndTime		= uiGeneric.getWidgetValue("endtimevalue");
		String strInterval		= uiGeneric.getWidgetValue("intervalvalue");
		String strFilter		= uiGeneric.getWidgetValue("filtervalue");
		String strInhibited		= uiGeneric.getWidgetValue("inhibitedvalue");
		String strLog			= uiGeneric.getWidgetValue("logvalue");
		String strRemoveAtEnd	= uiGeneric.getWidgetValue("removeatendvalue");
		
		int arguments = Integer.parseInt(strArguments);
		int interval = Integer.parseInt(strInterval);
		int inhibited = Integer.parseInt(strInhibited);
		int log = Integer.parseInt(strLog);
		int removeAtEnd = Integer.parseInt(strRemoveAtEnd);
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.addCompleteTaskRequest(strClientKey, strScsEnvId, strTaskName
				, strDescription
				, strCommand, arguments
				, strStartTime, strEndTime
				, interval, strFilter, inhibited, log, removeAtEnd, strClientName);
		
		logger.end(className, function);
	}


	private void addTask() {
		final String function = "addTask";
		logger.begin(className, function);
		
		String strTsckey		= uiGeneric.getWidgetValue("tsckeyvalue");
		String strClientKey		= uiGeneric.getWidgetValue("clientkeyvalue");
		String strScsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String strTaskName		= uiGeneric.getWidgetValue("tasknamevalue");
		String strClientName	= uiGeneric.getWidgetValue("clientname");
		
		TscMgr tscMgr = (TscMgr) MgrFactory.getInstance().getMgr(strTscMgr, strTsckey);
		
		tscMgr.setSubject(className + function, getSubject());
		
		tscMgr.addTaskRequest(strClientKey, strScsEnvId, strTaskName, strClientName);
		
		logger.end(className, function);
	}
	
	private void launch(String element) {

		if ( Request.SetStartTime.toString().toLowerCase().equals(element) ) {
			setStartTime();
		}
		else if ( Request.SetInterval.toString().toLowerCase().equals(element) ) {
			setInterval();
		}
		else if ( Request.SetFilter.toString().toLowerCase().equals(element) ) {
			setFilter();
		}
		else if ( Request.SetEndTime.toString().toLowerCase().equals(element) ) {
			setEndTime();
		}
		else if ( Request.SetDescription.toString().toLowerCase().equals(element) ) {
			setDescription();
		}
		else if ( Request.SetDates.toString().toLowerCase().equals(element) ) {
			setDates();
		}
		else if ( Request.SetCommand.toString().toLowerCase().equals(element) ) {
			setCommand();
		}
		else if ( Request.SetTaskArguments.toString().toLowerCase().equals(element) ) {
			setTaskArguments();
		}
		else if ( Request.SetArguments.toString().toLowerCase().equals(element) ) {
			setArguments();
		}
		else if ( Request.GetTaskType.toString().toLowerCase().equals(element) ) {
			getTaskType();
		}
		else if ( Request.GetTaskNames.toString().toLowerCase().equals(element) ) {
			getTaskNames();
		}
		else if ( Request.GetStartTime.toString().toLowerCase().equals(element) ) {
			getStartTime();
		}
		else if ( Request.GetRemoveAtEnd.toString().toLowerCase().equals(element) ) {
			getRemoveAtEnd();
		}
		else if ( Request.GetLog.toString().toLowerCase().equals(element) ) {
			getLog();
		}
		else if ( Request.GetInterval.toString().toLowerCase().equals(element) ) {
			getInterval();
		}
		else if ( Request.GetFilter.toString().toLowerCase().equals(element) ) {
			getFilter();
		}
		else if ( Request.GetEndTime.toString().toLowerCase().equals(element) ) {
			getEndTime();
		}
		else if ( Request.GetDescription.toString().toLowerCase().equals(element) ) {
			getDescription();
		}
		else if ( Request.GetDayGroupNamesAndIds.toString().toLowerCase().equals(element) ) {
			getDayGroupNamesAndIds();
		}
		else if ( Request.GetDates.toString().toLowerCase().equals(element) ) {
			getDates();
		}
		else if ( Request.GetCommand.toString().toLowerCase().equals(element) ) {
			getCommand();
		}
		else if ( Request.GetArguments.toString().toLowerCase().equals(element) ) {
			getArguments();
		}
		else if ( Request.RemoveTask.toString().toLowerCase().equals(element) ) {
			removeTask();
		}
		else if ( Request.IsEnabled.toString().toLowerCase().equals(element) ) {
			isEnabled();
		}
		else if ( Request.EnableTask.toString().toLowerCase().equals(element) ) {
			enableTask();
		}
		else if ( Request.DisableTask.toString().toLowerCase().equals(element) ) {
			disableTask();
		}
		else if ( Request.AddCompleteTask.toString().toLowerCase().equals(element) ) {
			addCompleteTask();
		}
		else if ( Request.AddTask.toString().toLowerCase().equals(element) ) {
			addTask();
		}
	}

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(className, function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				// TODO Auto-generated method stub
				
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				// TODO Auto-generated method stub
			}
		
			@Override
			public void envUp(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void envDown(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}

}
