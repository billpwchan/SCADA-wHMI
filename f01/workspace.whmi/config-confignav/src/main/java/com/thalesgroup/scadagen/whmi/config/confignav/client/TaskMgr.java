package com.thalesgroup.scadagen.whmi.config.confignav.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

public class TaskMgr implements AsyncCallback<Tasks> {
	
	private Logger logger = Logger.getLogger(TaskMgr.class.getName());
	
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
		
		logger.log(Level.FINE, "setTaskMgrEvent Begin");
		
		this.taskMgrEvents.add(taskMgrEvent);
		
		logger.log(Level.FINE, "setTaskMgrEvent End");
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final TaskServiceAsync taskMgrService = GWT.create(TaskService.class);
	
	public void initTasks(String profile, String location, int level, String header) {
		
		logger.log(Level.FINE, "initTasks Begin");
		
		logger.log(Level.SEVERE, "initTasks this.level["+level+"] this.header["+header+"]");
		
		String module		= null;
		
		String mappingFile	= null;
		
		String settingFile	= null;
		
		taskMgrService.taskServer(module, mappingFile, settingFile, profile, location, level, header, this);
		
		logger.log(Level.FINE, "initTasks End");
	}

	public void onFailure(Throwable caught) {
		// Show the RPC error message to the user
		
		logger.log(Level.SEVERE, "onFailure Begin");
		
		this.failded = true;
		
		for (Iterator<TaskMgrEvent> iterator = taskMgrEvents.iterator(); iterator.hasNext();) {
			TaskMgrEvent taskMgrEvent = iterator.next();
		    taskMgrEvent.failed();
		    iterator.remove();
		}

		logger.log(Level.SEVERE, "onFailure End");
	}// onFailure

	public void onSuccess(Tasks tsksCur) {
		// Success on get Menu from server
		
		logger.log(Level.FINE, "onSuccess Begin");

		if ( null != tsksCur ) {

			logger.log(Level.SEVERE, "onSuccess tsksCur.size()["+tsksCur.size()+"] calling the callback: taskMgrEvent.ready()");
		
			this.ready = true;
			
			for (Iterator<TaskMgrEvent> iterator = taskMgrEvents.iterator(); iterator.hasNext();) {
				TaskMgrEvent taskMgrEvent = iterator.next();
			    taskMgrEvent.ready(tsksCur);
			    iterator.remove();
			}

		} else {
			
			logger.log(Level.SEVERE, "onSuccess tsksCur is null");
			
		}
		
		logger.log(Level.FINE, "onSuccess End");

	}// onSuccess
	public boolean isReady() {
		
		logger.log(Level.SEVERE, "isReady Begin/End");
		
		return ready;
	}
	public boolean isFailded() {
		
		logger.log(Level.SEVERE, "isFailded Begin/End");
		
		return failded;
	}
	public void reset(){
		
		logger.log(Level.FINE, "reset Begin");
		
		this.ready = false;
		this.failded = false;
		
		logger.log(Level.FINE, "reset End");
	}

}
