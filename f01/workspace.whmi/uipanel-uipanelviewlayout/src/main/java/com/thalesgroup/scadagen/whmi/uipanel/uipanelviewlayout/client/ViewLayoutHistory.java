package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ViewLayoutHistory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(ViewLayoutHistory.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

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
		final String function = "setTaskLaunchs";
		
		logger.begin(className, function);
		
		this.taskLaunchs = null;
		this.taskLaunchs = new UITaskLaunch[taskLaunchs.length];
		for ( int i=0 ; i<taskLaunchs.length ; ++i ) {
			if ( null != taskLaunchs[i] ) {
				this.taskLaunchs[i] = new UITaskLaunch(taskLaunchs[i]);
			} else {
				logger.info(className, function, "setTaskLaunchs this.taskLunch[{}] is null", i);
			}
		}
		
		logger.end(className, function);
	}
	
	public void debug(String prefix) {
		final String function = "debug["+prefix+"]";
		
		logger.begin(className, function);
		
		logger.info(className, function, "taskLaunchs.length: [{}]", taskLaunchs.length);
		logger.info(className, function, "activateSelection: [{}]", activateSelection);
		
		logger.info(className, function, "viewLayoutMode: [{}]",  viewLayoutMode);
		logger.info(className, function, "viewLayoutAction: [{}]", viewLayoutAction);
		
		
		for ( int i = 0 ; i < taskLaunchs.length ; ++i ) {
			UITaskLaunch taskLaunch = taskLaunchs[i];
			if ( null != taskLaunch ) {
				logger.info(className, function, "taskLaunch[{}][{}]", i, taskLaunch.getHeader());
			} else {
				logger.info(className, function, "taskLunch[{}] is null", i);
			}
		}
		
		logger.end(className, function);
		
	}

}