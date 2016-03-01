package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.history.ViewLayoutHistoryMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory.TaskType;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;

public class ViewLayoutMgr {

	private static Logger logger = Logger.getLogger(ViewLayoutMgr.class.getName());

	private ViewLayoutMgrEvent viewLayoutMgrEvent = null;
	private ViewLayoutHistoryMgr viewLayoutHistoryMgr = null;
	private int viewIdActivate = 0;
	private ViewLayoutMode viewLayoutMode;
	private ViewLayoutAction viewLayoutAction;
	private UITaskLaunch taskLaunchs[];

	private UINameCard uiNameCard = null;

	/**
	 * @param viewLayoutMgrEvent
	 * @param uiNameCard
	 */
	public ViewLayoutMgr(ViewLayoutMgrEvent viewLayoutMgrEvent, UINameCard uiNameCard) {

		this.uiNameCard = new UINameCard(uiNameCard);

		this.viewLayoutMgrEvent = viewLayoutMgrEvent;

		this.viewLayoutHistoryMgr = new ViewLayoutHistoryMgr();

		this.taskLaunchs = new UITaskLaunch[ViewLayoutAction.SingleLayout.getValue()];

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});
	}

	/**
	 * Update the Splie Screen UI
	 */
	private void triggerSplitScreenButtonUpdate() {
		viewLayoutMgrEvent.setSplitButton();
	}

	/**
	 * Update the Split Screen Statue and the UI
	 * @param viewLayoutAction
	 */
	public void setSplitScreen(ViewLayoutAction viewLayoutAction) {

		ViewLayoutHistory viewLayoutHistory = null;

		if (null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}
		if (ViewLayoutAction.VDoubleLayout == viewLayoutAction) {

			if (ViewLayoutAction.VDoubleLayout == this.viewLayoutAction) {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);
			} else {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.VDoubleLayout);
			}

		} else if (ViewLayoutAction.HDoubleLayout == viewLayoutAction) {

			if (ViewLayoutAction.HDoubleLayout == this.viewLayoutAction) {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);
			} else {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.HDoubleLayout);
			}

		}

		if (null != viewLayoutHistory) {
			restoreCurrentStatus(viewLayoutHistory);
		}

		triggerSplitScreenButtonUpdate();

		historySnapshot();

	}

	/**
	 * Set the internal status and update the ui
	 * 
	 * @param viewLayoutMode
	 * @param viewLayoutAction
	 */
	private void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction) {

		logger.log(Level.FINE, "setLayout Begin");

		this.viewLayoutMode = viewLayoutMode;

		this.viewLayoutAction = viewLayoutAction;

		this.viewLayoutMgrEvent.setLayout(this.viewLayoutMode, this.viewLayoutAction);

		this.viewIdActivate = 0;

		this.taskLaunchs = null;

		this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];

		for (int i = 0; i < this.taskLaunchs.length; ++i) {
			this.taskLaunchs[i] = null;
		}

		logger.log(Level.FINE, "setLayout created taskLaunchs.length[" + this.taskLaunchs.length + "]");

		logger.log(Level.FINE, "setLayout End");
	}

	private boolean isValidActivateId(int activateId) {
		boolean result = false;
		logger.log(Level.FINE,
				"isValidActivate this.viewIdActivate[" + this.viewIdActivate + "] >= 0 && this.viewIdActivate["
						+ this.viewIdActivate + "] < taskLaunchs.length[" + this.taskLaunchs.length + "]");
		if( this.viewIdActivate >= 0 && this.viewIdActivate < this.taskLaunchs.length ) {
			result = true;
		}
		logger.log(Level.FINE, "isValidActivate result[" + result + "]");
		return result;
	}
	
	/**
	 * Apply the Task Switch the Mode and Action if not match Store the current
	 * views tasklaunch if makeSnapshot is true
	 * 
	 * @param taskLaunch
	 * @param makeSnapshot
	 */
	public void setTaskLaunch(UITaskLaunch taskLaunch, boolean makeSnapshot) {

		logger.log(Level.FINE, "setTaskLaunch Begin");

		// boolean change = false;

		if (TaskLaunchType.IMAGE == taskLaunch.getTaskLaunchType()) {
			logger.log(Level.FINE, "setTaskLaunch TaskType.IMAGE");

			if (ViewLayoutMode.Image != this.viewLayoutMode) {
				logger.log(Level.FINE, "setTaskLaunch ViewLayoutMode.Image != [" + this.viewLayoutMode + "]");

				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);

			}

		} else if (TaskLaunchType.PANEL == taskLaunch.getTaskLaunchType()) {
			logger.log(Level.FINE, "setTaskLaunch TaskType.PANEL");

			if (ViewLayoutMode.Panel != this.viewLayoutMode) {
				logger.log(Level.FINE, "setTaskLaunch ViewLayoutMode.Panel != [" + this.viewLayoutMode + "]");

				setLayout(ViewLayoutMode.Panel, ViewLayoutAction.SingleLayout);

			}
		}

		viewLayoutMgrEvent.setTaskLaunch(taskLaunch, this.viewIdActivate);

		logger.log(Level.FINE, "setTaskLaunch this.viewLayoutAction[" + this.viewLayoutAction + "]");

		if (null == this.taskLaunchs) {
			logger.log(Level.FINE, "setTaskLaunch this.taskLaunchs is null, creating...");
			this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];
			logger.log(Level.FINE,
					"setTaskLaunch this.taskLaunchs created this.taskLaunchs.length[" + this.taskLaunchs.length + "]");
		}


		if ( isValidActivateId(this.viewIdActivate) ) {
			this.taskLaunchs[this.viewIdActivate] = taskLaunch;
		} else {
			logger.log(Level.FINE, "setTaskLaunch this.viewIdActivate is INVALUD[" + this.viewIdActivate
					+ "] taskLaunchs.length[" + this.taskLaunchs.length + "]");
		}
		
		
		triggerTitleChange(taskLaunch);

		// Station
//		UITaskTitle taskTitle = new UITaskTitle();
//		taskTitle.setTaskUiScreen(this.uiNameCard.getUiScreen());
//		taskTitle.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar");
//		taskTitle.setStation(taskLaunch.getNameWithSpace());
//		taskTitle.setTitle(taskLaunch.getTitleWithSpace());
//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskTitle));
		// End of Station

		triggerProfileChange();
		
		// Profile
//		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
//		String profile = opmAuthentication.getCurrentProfile();
//		String operator = opmAuthentication.getCurrentOperator();
//		
//		UITaskProfile taskProfile = new UITaskProfile();
//		taskProfile.setTaskUiScreen(this.uiNameCard.getUiScreen());
//		taskProfile.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar");
//		taskProfile.setProfile(profile);
//		taskProfile.setOperator(operator);
//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskProfile));
		// End of Profile

		logger.log(Level.FINE, "setTaskLaunch hasSnapshot[" + makeSnapshot + "]");

		if (makeSnapshot) {

			historySnapshot();
		}

		triggerTitleChange(taskLaunch);
		triggerSplitScreenButtonUpdate();
		triggerHistoryUIUpdate();

		logger.log(Level.FINE, "setTaskLaunch End");

	}

	/**
	 * Restore the ViewLayout Snapshot
	 * 
	 * @param viewLayoutHistory
	 */
	private void restoreCurrentStatus(ViewLayoutHistory viewLayoutHistory) {

		logger.log(Level.FINE, "restoreCurrentStatus Begin");

		UITaskLaunch taskLaunchs[] = viewLayoutHistory.getTaskLaunchs();

		for (int viewId = 0; viewId < this.taskLaunchs.length; ++viewId) {
			if (null != taskLaunchs[viewId]) {
				this.viewIdActivate = viewId;
				this.setTaskLaunch(taskLaunchs[viewId], false);
			} else {
				this.taskLaunchs[viewId] = null;
			}
		}

		this.viewIdActivate = 0;
		
		UITaskLaunch taskLaunch = getTaskLaunchActivate();
		if ( null != taskLaunch ) {
			triggerMenuChange(taskLaunch);
			triggerTitleChange(taskLaunch);		
		}

		logger.log(Level.FINE, "restoreCurrentStatus End");
	}

	/**
	 * @param viewLayoutAction
	 */
	public void setLayoutAction(ViewLayoutHistory viewLayoutHistory) {

		logger.log(Level.FINE, "setLayoutAction Begin");

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		logger.log(Level.FINE, "setLayoutAction End");
	}

	/**
	 * @param viewLayoutAction
	 * @param restorePreviousViews
	 */
	public void setLayoutAction(ViewLayoutAction viewLayoutAction, boolean restorePreviousViews) {

		logger.log(Level.FINE, "setLayoutAction Begin");

		ViewLayoutHistory viewLayoutHistory = null;

		if (restorePreviousViews && null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		historySnapshot();

		logger.log(Level.FINE, "setLayoutAction End");
	}

	/**
	 * Trigger the TaskHistory UI (Button etc.) Update. Fire the TaskHistory
	 * with TaskType PreviousEnable, PreviousDisable, NextEnable, NextDisable
	 */
	private void triggerHistoryUIUpdate() {

		logger.log(Level.FINE, "triggerHistoryUIUpdate Begin");

		UITaskHistory taskHistoryPrevious = new UITaskHistory();
		taskHistoryPrevious.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryPrevious.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelAccessBar");

		if (this.viewLayoutHistoryMgr.hasPrevious()) {

			logger.log(Level.FINE, "triggerHistoryUIUpdate viewLayoutHistoryMgr has Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousEnable);

		} else {

			logger.log(Level.FINE, "triggerHistoryUIUpdate viewLayoutHistoryMgr haven't Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousDisable);
		}

		logger.log(Level.FINE, "triggerHistoryUIUpdate taskHistoryPrevious fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryPrevious));

		UITaskHistory taskHistoryNext = new UITaskHistory();
		taskHistoryNext.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryNext.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelAccessBar");

		if (this.viewLayoutHistoryMgr.hasNext()) {

			logger.log(Level.FINE, "triggerHistoryUIUpdate viewLayoutHistoryMgr has Next");

			taskHistoryNext.setTaskType(TaskType.NextEnable);
		} else {

			logger.log(Level.FINE, "triggerHistoryUIUpdate viewLayoutHistoryMgr is haven't Next ");

			taskHistoryNext.setTaskType(TaskType.NextDisable);
		}

		logger.log(Level.FINE, "triggerHistoryUIUpdate taskHistoryNext fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryNext));

		logger.log(Level.FINE, "triggerHistoryUIUpdate End");
	}

	/**
	 * Get the History Action from the passing taskHistory and Apply TaskHistory
	 * 
	 * @param taskHistory
	 */
	private void setTaskHistory(UITaskHistory taskHistory) {

		logger.log(Level.FINE, "setTaskHistory Begin");

		ViewLayoutHistory viewLayoutHistory = null;

		if (TaskType.Previous == taskHistory.getTaskType()) {

			logger.log(Level.FINE, "setTaskHistory taskHistory is TaskType.Previous");

			if (viewLayoutHistoryMgr.hasPrevious()) {

				viewLayoutHistory = viewLayoutHistoryMgr.previous();

			} else {

				logger.log(Level.FINE, "setTaskHistory viewLayoutHistoryMgr haven't previous");

			}
		} else if (TaskType.Next == taskHistory.getTaskType()) {

			logger.log(Level.FINE, "setTaskHistory taskHistory is TaskType.Next ");

			if (viewLayoutHistoryMgr.hasNext()) {

				viewLayoutHistory = viewLayoutHistoryMgr.next();

			} else {

				logger.log(Level.FINE, "setTaskHistory viewLayoutHistoryMgr haven't next");

			}
		}

		if (null != viewLayoutHistory) {

			logger.log(Level.FINE, "setTaskHistory viewLayoutHistory is not null");

			setLayoutAction(viewLayoutHistory);
		}

		triggerHistoryUIUpdate();

		logger.log(Level.FINE, "setTaskHistory End");
	}

	/**
	 * @param uiEvent
	 */
	private void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		if (null != uiEvent) {

			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if (UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide)) {

						logger.log(Level.FINE, "onUIEvent taskProvide is TaskLaunch");

						setTaskLaunch((UITaskLaunch) taskProvide, true);

					} else if (UITaskMgr.isInstanceOf(UITaskHistory.class, taskProvide)) {

						logger.log(Level.FINE, "onUIEvent taskProvide is TaskHistory");

						setTaskHistory((UITaskHistory) taskProvide);
					}

				}
			}
		}

		logger.log(Level.FINE, "onUIEvent End");
	}

	/**
	 * @return
	 */
	public ViewLayoutMode getViewMode() {

		logger.log(Level.FINE, "getViewMode Begin/End");

		return this.viewLayoutMode;
	}

	/**
	 * @return
	 */
	public ViewLayoutAction getViewAction() {

		logger.log(Level.FINE, "getViewAction Begin/End");

		return this.viewLayoutAction;
	}

	/**
	 * @param viewIdActivate
	 */
	public void setViewIdActivate(int viewIdActivate) {

		logger.log(Level.FINE, "setViewIdActivate Begin/End");

		this.viewIdActivate = viewIdActivate;
		
		UITaskLaunch taskLaunch = getTaskLaunchActivate();
		if ( null != taskLaunch ) {
			triggerMenuChange(taskLaunch);
			triggerTitleChange(taskLaunch);		
		}
		
		logger.log(Level.FINE, "setViewIdActivate Begin/End");
	}

	/**
	 * Generate Current ViewLayout Snapshot as the Snapshot object
	 * 
	 * @return
	 */
	private ViewLayoutHistory getCurrentAsHistory() {

		logger.log(Level.FINE, "getCurrentAsHistory Begin");

		logger.log(Level.FINE, "getCurrentAsHistory this.taskLaunchs.length[" + this.taskLaunchs.length + "]");

		ViewLayoutHistory history = new ViewLayoutHistory();
		history.setViewLayoutAction(this.viewLayoutAction);
		history.setActivateSelection(this.viewIdActivate);
		history.setViewLayoutMode(this.viewLayoutMode);
		history.setTaskLaunchs(this.taskLaunchs);

		logger.log(Level.FINE, "getCurrentAsHistory End");

		return history;
	}

	/**
	 * Save the current ViewLayout Snapshot into history
	 */
	private void historySnapshot() {

		logger.log(Level.FINE, "historySnapshot Begin");

		ViewLayoutHistory history = getCurrentAsHistory();

		history.debug();

		viewLayoutHistoryMgr.add(history);

		logger.log(Level.FINE, "historySnapshot End");
	}
	
	private UITaskLaunch getTaskLaunchActivate() {
		UITaskLaunch taskLaunch = null;
		int viewIdActivate = this.viewIdActivate;
		if ( null != this.taskLaunchs ) {
			if ( isValidActivateId(viewIdActivate) ) {
				if ( null != this.taskLaunchs[viewIdActivate] ) {
					
					logger.log(Level.FINE, "getTaskLaunchFromTaskLaunchs viewIdActivate["+viewIdActivate+"]");
					
					taskLaunch = this.taskLaunchs[viewIdActivate];
					
				} else {
					logger.log(Level.FINE, "getTaskLaunchFromTaskLaunchs taskLaunchs["+viewIdActivate+"] is null");
				}
			} else {
				logger.log(Level.FINE, "getTaskLaunchFromTaskLaunchs isValidActivateId is false");
			}
		} else {
			logger.log(Level.FINE, "getTaskLaunchFromTaskLaunchs taskLaunchs is null");
			
		}
		return taskLaunch;
	}
	
	private void triggerProfileChange () {
		
		logger.log(Level.FINE, "triggerProfileChange Begin");
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		String profile = opmAuthentication.getCurrentProfile();
		String operator = opmAuthentication.getCurrentOperator();
		
		UITaskProfile taskProfile = new UITaskProfile();
		taskProfile.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskProfile.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar");
		taskProfile.setProfile(profile);
		taskProfile.setOperator(operator);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskProfile));
		
		logger.log(Level.FINE, "triggerProfileChange End");
	}
	
	private void triggerTitleChange ( UITaskLaunch taskLaunch ) {
		
		logger.log(Level.FINE, "triggerTitleChange Begin");
		
		String title = "";
		if ( null != taskLaunch ) {
			title = taskLaunch.getTitle();
		} else {
			logger.log(Level.SEVERE, "triggerTitleChange tasLaunch is null");
		}
		
		logger.log(Level.SEVERE, "triggerMenuChange UITaskTitle title["+title+"]");
		
		UITaskTitle taskTitle = new UITaskTitle();
		taskTitle.setTitle(title);
		taskTitle.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskTitle.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar");
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskTitle));
		
		logger.log(Level.FINE, "triggerTitleChange End");
	}
	
	private void triggerMenuChange( UITaskLaunch taskLaunch ) {
		
		logger.log(Level.FINE, "triggerMenuChange Begin");
		
		if ( null != taskLaunch ) {
		
			UITaskLaunch taskLaunchToSend = new UITaskLaunch(taskLaunch);
			taskLaunchToSend.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunchToSend.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:NavigationMgr");
			UIEvent uiEvent = new UIEvent(taskLaunchToSend);
			
			this.uiNameCard.getUiEventBus().fireEvent(uiEvent);
		} else {
			logger.log(Level.FINE, "triggerMenuChange taskLaunch is null");
		}
		
		logger.log(Level.FINE, "triggerMenuChange End");
	}

}
