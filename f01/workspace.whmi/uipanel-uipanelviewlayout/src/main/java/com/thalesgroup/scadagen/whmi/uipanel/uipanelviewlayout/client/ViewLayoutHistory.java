package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class ViewLayoutHistory {
	
	private static Logger logger = Logger.getLogger(ViewLayoutHistory.class.getName());

	private ViewLayoutMode viewLayoutMode;
	private ViewLayoutAction viewLayoutAction;
	private int activateSelection;
	private UITaskLaunch taskLaunchs[] = null;
	
	public ViewLayoutHistory() {
	}
	
	public ViewLayoutHistory(ViewLayoutHistory viewLayoutHistory) {
		set(viewLayoutMode, viewLayoutAction, activateSelection, taskLaunchs);
	}
	
	private void set(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction, int activateSelection, UITaskLaunch taskLaunchs[]){
		this.setViewLayoutMode(viewLayoutMode);
		this.setViewLayoutAction(viewLayoutAction);
		this.setActivateSelection(activateSelection);
		this.setTaskLaunchs(taskLaunchs);
	}

	public ViewLayoutMode getViewLayoutMode() {
		return viewLayoutMode;
	}

	public void setViewLayoutMode(ViewLayoutMode viewLayoutMode) {
		this.viewLayoutMode = viewLayoutMode;
	}

	public ViewLayoutAction getViewLayoutAction() {
		return viewLayoutAction;
	}

	public void setViewLayoutAction(ViewLayoutAction viewLayoutAction) {
		this.viewLayoutAction = viewLayoutAction;
	}

	public int getActivateSelection() {
		return activateSelection;
	}

	public void setActivateSelection(int activateSelection) {
		this.activateSelection = activateSelection;
	}

	public UITaskLaunch[] getTaskLaunchs() {
		return taskLaunchs;
	}

	public void setTaskLaunchs(UITaskLaunch taskLaunchs[]) {
		
		logger.log(Level.FINE, "setTaskLaunchs Begin");
		
		this.taskLaunchs = null;
		this.taskLaunchs = new UITaskLaunch[taskLaunchs.length];
		for ( int i=0 ; i<taskLaunchs.length ; ++i ) {
			if ( null != taskLaunchs[i] ) {
				this.taskLaunchs[i] = new UITaskLaunch(taskLaunchs[i]);
			} else {
				logger.log(Level.FINE, "setTaskLaunchs this.taskLunch[" + i + "] is null");
			}
		}
		
		logger.log(Level.FINE, "setTaskLaunchs End");
	}
	
	public void debug(String prefix) {
		
		logger.log(Level.SEVERE, "debug "+prefix+" Begin");
		
		logger.log(Level.SEVERE, "debug "+prefix+" taskLaunchs.length: [" + taskLaunchs.length +"]");
		logger.log(Level.SEVERE, "debug "+prefix+" activateSelection: [" + activateSelection +"]");
		
		logger.log(Level.SEVERE, "debug "+prefix+" viewLayoutMode: [" + viewLayoutMode +"]");
		logger.log(Level.SEVERE, "debug "+prefix+" viewLayoutAction: [" + viewLayoutAction +"]");
		
		
		for ( int i = 0 ; i < taskLaunchs.length ; ++i ) {
			UITaskLaunch taskLaunch = taskLaunchs[i];
			if ( null != taskLaunch ) {
				logger.log(Level.SEVERE, "debug "+prefix+" taskLaunch[" + i + "][" + taskLaunch.getHeader() + "]");
			} else {
				logger.log(Level.SEVERE, "debug "+prefix+" taskLunch[" + i + "] is null");
			}
		}
		
		logger.log(Level.SEVERE, "debug "+prefix+" End");
		
	}

}