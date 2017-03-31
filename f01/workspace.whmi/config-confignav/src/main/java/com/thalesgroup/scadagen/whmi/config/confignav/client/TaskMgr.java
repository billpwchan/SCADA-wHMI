package com.thalesgroup.scadagen.whmi.config.confignav.client;

import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class TaskMgr implements AsyncCallback<Tasks> {
	
	private final String className = UIWidgetUtil.getClassSimpleName(TaskMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private boolean ready = false;
	private boolean failded = false;
	
	private static TaskMgr instance = null;
	private TaskMgr () {}
	public static TaskMgr getInstance() {
		if ( instance == null ) {
			instance = GWT.create(TaskMgr.class);
		}
		return instance;
	}

	private LinkedList<TaskMgrEvent> taskMgrEvents = new LinkedList<TaskMgrEvent>();
	public void setTaskMgrEvent ( TaskMgrEvent taskMgrEvent ) {
		final String function = "setTaskMgrEvent";
		
		logger.begin(className, function);
		
		this.taskMgrEvents.add(taskMgrEvent);
		
		logger.end(className, function);
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final TaskServiceAsync taskMgrService = GWT.create(TaskService.class);
	
	public void initTasks(String profile, String location, int level, String header) {
		final String function = "initTasks";
		
		logger.begin(className, function);
		
		logger.info(className, function, "initTasks this.level[{}] this.header[{}]", level, header);
		
		String module		= null;
		
		String mappingFile	= null;
		
		String settingFile	= null;
		
		taskMgrService.taskServer(module, mappingFile, settingFile, profile, location, level, header, this);
		
		logger.end(className, function);
	}

	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		// Show the RPC error message to the user
		
		logger.begin(className, function);
		
		logger.error(className, function, "");
		
		this.failded = true;
		
		for (Iterator<TaskMgrEvent> iterator = taskMgrEvents.iterator(); iterator.hasNext();) {
			TaskMgrEvent taskMgrEvent = iterator.next();
		    taskMgrEvent.failed();
		    iterator.remove();
		}

		logger.end(className, function);
	}// onFailure

	public void onSuccess(Tasks tsksCur) {
		final String function = "onSuccess";
		// Success on get Menu from server
		
		logger.begin(className, function);

		if ( null != tsksCur ) {

			logger.debug(className, function, "tsksCur.size()[{}]", tsksCur.size());
		
			this.ready = true;
			
			for (Iterator<TaskMgrEvent> iterator = taskMgrEvents.iterator(); iterator.hasNext();) {
				TaskMgrEvent taskMgrEvent = iterator.next();
			    taskMgrEvent.ready(tsksCur);
			    iterator.remove();
			}
		} else {
			logger.warn(className, function, "tsksCur is null");
		}
		
		logger.end(className, function);

	}// onSuccess
	public boolean isReady() {
		final String function = "isReady";
		
		logger.beginEnd(className, function);
		
		return ready;
	}
	public boolean isFailded() {
		final String function = "isFailded";
		
		logger.error(className, function, "");
		
		return failded;
	}
	public void reset(){
		final String function = "reset";
		
		logger.begin(className, function);
		
		this.ready = false;
		this.failded = false;
		
		logger.end(className, function);
	}

}
