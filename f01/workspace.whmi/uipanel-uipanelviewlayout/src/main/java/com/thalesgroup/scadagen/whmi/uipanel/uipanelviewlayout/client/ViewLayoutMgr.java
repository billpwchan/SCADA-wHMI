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
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;

public class ViewLayoutMgr {

	private Logger logger = Logger.getLogger(ViewLayoutMgr.class.getName());
	
	private final String logPrefix = "[ViewLayoutMgr] ";

	private ViewLayoutMgrEvent viewLayoutMgrEvent = null;
	private ViewLayoutHistoryMgr viewLayoutHistoryMgr = null;
	private int viewIdActivate = 0;
	private ViewLayoutMode viewLayoutMode;
	private ViewLayoutAction viewLayoutAction;
	private UITaskLaunch taskLaunchs[];

	private UINameCard uiNameCard = null;
	
	private final String UIPathUIPanelAccessBar		= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelAccessBar";
	private final String UIPathUIPanelStatusBar		= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar";
	private final String UIPathUIPanelViewLayout	= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

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
	private void triggerSplitChange() {
		
		logger.log(Level.FINE, logPrefix+"triggerSplitChange Begin");
		
		UITaskSplit taskSplit1 = new UITaskSplit();
		taskSplit1.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskSplit1.setUiPath(UIPathUIPanelAccessBar);

		if ( this.getViewMode() == ViewLayoutMode.Panel ) {

			taskSplit1.setTaskType(UITaskSplit.SplitType.HorizontalDisable);
			
		} else if ( this.getViewAction() == ViewLayoutAction.HDoubleLayout ){
			
			taskSplit1.setTaskType(UITaskSplit.SplitType.HorizontalHightLight);
			
		} else {
			
			taskSplit1.setTaskType(UITaskSplit.SplitType.HorizontalEnable);
			
		}
		
		logger.log(Level.FINE, logPrefix+"triggerSplitChange taskSplit1.getTaskType["+taskSplit1.getTaskType()+"]");
		
		logger.log(Level.FINE, logPrefix+"triggerSplitChange taskSplit1 fire");

//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit1));
		
		UITaskSplit taskSplit2 = new UITaskSplit();
		taskSplit2.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskSplit2.setUiPath(UIPathUIPanelAccessBar);
		
		if ( this.getViewMode() == ViewLayoutMode.Panel ) {

			taskSplit2.setTaskType(UITaskSplit.SplitType.VerticalDisable);
			
		} else if ( this.getViewAction() == ViewLayoutAction.VDoubleLayout ){
			
			taskSplit2.setTaskType(UITaskSplit.SplitType.VerticalHightLight);
			
		} else {
			
			taskSplit2.setTaskType(UITaskSplit.SplitType.VerticalEnable);
			
		}
		
		logger.log(Level.FINE, logPrefix+"triggerSplitChange taskSplit2.getTaskType["+taskSplit2.getTaskType()+"]");
		
		logger.log(Level.FINE, logPrefix+"triggerSplitChange taskSplit2 fire");

//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit2));

		logger.log(Level.FINE, logPrefix+"triggerSplitChange End");
		
	}
	
	public void setSplitScreen(UITaskSplit uiTaskSplit) {
		
		logger.log(Level.FINE, logPrefix+"setSplitScreen Begin");
		
		ViewLayoutHistory viewLayoutHistory = null;

		if (null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}
		
		logger.log(Level.FINE, logPrefix+"setSplitScreen uiTaskSplit.getSplitType()["+uiTaskSplit.getTaskType()+"]");
		
		if (UITaskSplit.SplitType.Vertical == uiTaskSplit.getTaskType()) {
			
			logger.log(Level.FINE, logPrefix+"setSplitScreen UITaskSplit.SplitType.Vertical");

			if (ViewLayoutAction.VDoubleLayout == this.viewLayoutAction) {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);
			} else {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.VDoubleLayout);
			}

		} else if (UITaskSplit.SplitType.Horizontal == uiTaskSplit.getTaskType()) {

			if (ViewLayoutAction.HDoubleLayout == this.viewLayoutAction) {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);
			} else {
				setLayout(ViewLayoutMode.Image, ViewLayoutAction.HDoubleLayout);
			}

		}

		if (null != viewLayoutHistory) {
			restoreCurrentStatus(viewLayoutHistory);
		}

		triggerSplitChange();

		historySnapshot();
		
		
		logger.log(Level.FINE, logPrefix+"setSplitScreen End");
	}

	/**
	 * Set the internal status and update the ui
	 * 
	 * @param viewLayoutMode
	 * @param viewLayoutAction
	 */
	private void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction) {

		logger.log(Level.FINE, logPrefix+"setLayout Begin");

		this.viewLayoutMode = viewLayoutMode;

		this.viewLayoutAction = viewLayoutAction;

		this.viewLayoutMgrEvent.setLayout(this.viewLayoutMode, this.viewLayoutAction);

		this.viewIdActivate = 0;

		this.taskLaunchs = null;

		this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];

		for (int i = 0; i < this.taskLaunchs.length; ++i) {
			this.taskLaunchs[i] = null;
		}

		logger.log(Level.FINE, logPrefix+"setLayout created taskLaunchs.length[" + this.taskLaunchs.length + "]");

		logger.log(Level.FINE, logPrefix+"setLayout End");
	}

	private boolean isValidActivateId(int activateId) {
		boolean result = false;
		logger.log(Level.FINE,
				"isValidActivate this.viewIdActivate[" + this.viewIdActivate + "] >= 0 && this.viewIdActivate["
						+ this.viewIdActivate + "] < taskLaunchs.length[" + this.taskLaunchs.length + "]");
		if( this.viewIdActivate >= 0 && this.viewIdActivate < this.taskLaunchs.length ) {
			result = true;
		}
		logger.log(Level.FINE, logPrefix+"isValidActivate result[" + result + "]");
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

		logger.log(Level.FINE, logPrefix+"setTaskLaunch Begin");

		// boolean change = false;

		if (TaskLaunchType.IMAGE == taskLaunch.getTaskLaunchType()) {
			logger.log(Level.FINE, logPrefix+"setTaskLaunch TaskType.IMAGE");

			if (ViewLayoutMode.Image != this.viewLayoutMode) {
				logger.log(Level.FINE, logPrefix+"setTaskLaunch ViewLayoutMode.Image != [" + this.viewLayoutMode + "]");

				setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);

			}

		} else if (TaskLaunchType.PANEL == taskLaunch.getTaskLaunchType()) {
			logger.log(Level.FINE, logPrefix+"setTaskLaunch TaskType.PANEL");

			if (ViewLayoutMode.Panel != this.viewLayoutMode) {
				logger.log(Level.FINE, logPrefix+"setTaskLaunch ViewLayoutMode.Panel != [" + this.viewLayoutMode + "]");

				setLayout(ViewLayoutMode.Panel, ViewLayoutAction.SingleLayout);

			}
		}

		viewLayoutMgrEvent.setTaskLaunch(taskLaunch, this.viewIdActivate);

		logger.log(Level.FINE, logPrefix+"setTaskLaunch this.viewLayoutAction[" + this.viewLayoutAction + "]");

		if (null == this.taskLaunchs) {
			logger.log(Level.FINE, logPrefix+"setTaskLaunch this.taskLaunchs is null, creating...");
			this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];
			logger.log(Level.FINE,
					"setTaskLaunch this.taskLaunchs created this.taskLaunchs.length[" + this.taskLaunchs.length + "]");
		}


		if ( isValidActivateId(this.viewIdActivate) ) {
			this.taskLaunchs[this.viewIdActivate] = taskLaunch;
		} else {
			logger.log(Level.FINE, logPrefix+"setTaskLaunch this.viewIdActivate is INVALUD[" + this.viewIdActivate
					+ "] taskLaunchs.length[" + this.taskLaunchs.length + "]");
		}
		
		triggerTitleChange(taskLaunch);

		triggerProfileChange();

		logger.log(Level.FINE, logPrefix+"setTaskLaunch hasSnapshot[" + makeSnapshot + "]");

		if (makeSnapshot) {

			historySnapshot();
		}

		triggerSplitChange();

		triggerHistoryChange();

		logger.log(Level.FINE, logPrefix+"setTaskLaunch End");

	}

	/**
	 * Restore the ViewLayout Snapshot
	 * 
	 * @param viewLayoutHistory
	 */
	private void restoreCurrentStatus(ViewLayoutHistory viewLayoutHistory) {

		logger.log(Level.FINE, logPrefix+"restoreCurrentStatus Begin");

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
			triggerSplitChange();
		}

		logger.log(Level.FINE, logPrefix+"restoreCurrentStatus End");
	}

	/**
	 * @param viewLayoutAction
	 */
	public void setLayoutAction(ViewLayoutHistory viewLayoutHistory) {

		logger.log(Level.FINE, logPrefix+"setLayoutAction Begin");

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		logger.log(Level.FINE, logPrefix+"setLayoutAction End");
	}

	/**
	 * @param viewLayoutAction
	 * @param restorePreviousViews
	 */
	public void setLayoutAction(ViewLayoutAction viewLayoutAction, boolean restorePreviousViews) {

		logger.log(Level.FINE, logPrefix+"setLayoutAction Begin");

		ViewLayoutHistory viewLayoutHistory = null;

		if (restorePreviousViews && null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		historySnapshot();

		logger.log(Level.FINE, logPrefix+"setLayoutAction End");
	}

	/**
	 * Trigger the TaskHistory UI (Button etc.) Update. Fire the TaskHistory
	 * with TaskType PreviousEnable, PreviousDisable, NextEnable, NextDisable
	 */
	private void triggerHistoryChange() {

		logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate Begin");

		UITaskHistory taskHistoryPrevious = new UITaskHistory();
		taskHistoryPrevious.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryPrevious.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasPrevious()) {

			logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate viewLayoutHistoryMgr has Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousEnable);

		} else {

			logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate viewLayoutHistoryMgr haven't Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousDisable);
		}

		logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate taskHistoryPrevious fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryPrevious));

		UITaskHistory taskHistoryNext = new UITaskHistory();
		taskHistoryNext.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryNext.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasNext()) {

			logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate viewLayoutHistoryMgr has Next");

			taskHistoryNext.setTaskType(TaskType.NextEnable);
		} else {

			logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate viewLayoutHistoryMgr is haven't Next ");

			taskHistoryNext.setTaskType(TaskType.NextDisable);
		}

		logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate taskHistoryNext fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryNext));

		logger.log(Level.FINE, logPrefix+"triggerHistoryUIUpdate End");
	}

	/**
	 * Get the History Action from the passing taskHistory and Apply TaskHistory
	 * 
	 * @param taskHistory
	 */
	private void setTaskHistory(UITaskHistory taskHistory) {

		logger.log(Level.FINE, logPrefix+"setTaskHistory Begin");

		ViewLayoutHistory viewLayoutHistory = null;

		if (TaskType.Previous == taskHistory.getTaskType()) {

			logger.log(Level.FINE, logPrefix+"setTaskHistory taskHistory is TaskType.Previous");

			if (viewLayoutHistoryMgr.hasPrevious()) {

				viewLayoutHistory = viewLayoutHistoryMgr.previous();

			} else {

				logger.log(Level.FINE, logPrefix+"setTaskHistory viewLayoutHistoryMgr haven't previous");

			}
		} else if (TaskType.Next == taskHistory.getTaskType()) {

			logger.log(Level.FINE, logPrefix+"setTaskHistory taskHistory is TaskType.Next ");

			if (viewLayoutHistoryMgr.hasNext()) {

				viewLayoutHistory = viewLayoutHistoryMgr.next();

			} else {

				logger.log(Level.FINE, logPrefix+"setTaskHistory viewLayoutHistoryMgr haven't next");

			}
		}

		if (null != viewLayoutHistory) {

			logger.log(Level.FINE, logPrefix+"setTaskHistory viewLayoutHistory is not null");

			setLayoutAction(viewLayoutHistory);
		}

		triggerHistoryChange();

		logger.log(Level.FINE, logPrefix+"setTaskHistory End");
	}

	/**
	 * @param uiEvent
	 */
	private void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, logPrefix+"onUIEvent Begin");
		
		if ( null != uiNameCard ) {
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"]");
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiNameCard.getUiPath()["+uiNameCard.getUiPath()+"]");
		} else {
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiNameCard IS NULL");
		}
		
		if ( null != uiEvent ) {
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiEvent.getTaskProvide().getTaskUiScreen()["+uiEvent.getTaskProvide().getTaskUiScreen()+"]");
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiEvent.getTaskProvide().getUiPath()["+uiEvent.getTaskProvide().getUiPath()+"]");
		} else {
			logger.log(Level.SEVERE, logPrefix+"onUIEvent uiEvent IS NULL");
		}

		if (null != uiEvent) {

			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if ( taskProvide instanceof UITaskLaunch ) {

						logger.log(Level.FINE, logPrefix+"onUIEvent taskProvide is TaskLaunch");

						setTaskLaunch((UITaskLaunch) taskProvide, true);

					} else if ( taskProvide instanceof UITaskHistory ) {

						logger.log(Level.FINE, logPrefix+"onUIEvent taskProvide is TaskHistory");

						setTaskHistory((UITaskHistory) taskProvide);
					} else if ( taskProvide instanceof UITaskSplit ) {
						
						logger.log(Level.FINE, logPrefix+"onUIEvent taskProvide is UITaskSplit");
						
						setSplitScreen((UITaskSplit) taskProvide);
						
					}

				}
			}
		}

		logger.log(Level.FINE, logPrefix+"onUIEvent End");
	}

	/**
	 * @return
	 */
	public ViewLayoutMode getViewMode() {

		logger.log(Level.FINE, logPrefix+"getViewMode Begin/End");

		return this.viewLayoutMode;
	}

	/**
	 * @return
	 */
	public ViewLayoutAction getViewAction() {

		logger.log(Level.FINE, logPrefix+"getViewAction Begin/End");

		return this.viewLayoutAction;
	}

	/**
	 * @param viewIdActivate
	 */
	public void setViewIdActivate(int viewIdActivate) {

		logger.log(Level.FINE, logPrefix+"setViewIdActivate Begin/End");

		this.viewIdActivate = viewIdActivate;
		
		UITaskLaunch taskLaunch = getTaskLaunchActivate();
		if ( null != taskLaunch ) {
			triggerMenuChange(taskLaunch);
			triggerTitleChange(taskLaunch);		
		}
		
		logger.log(Level.FINE, logPrefix+"setViewIdActivate Begin/End");
	}

	/**
	 * Generate Current ViewLayout Snapshot as the Snapshot object
	 * 
	 * @return
	 */
	private ViewLayoutHistory getCurrentAsHistory() {

		logger.log(Level.FINE, logPrefix+"getCurrentAsHistory Begin");

		logger.log(Level.FINE, logPrefix+"getCurrentAsHistory this.taskLaunchs.length[" + this.taskLaunchs.length + "]");

		ViewLayoutHistory history = new ViewLayoutHistory();
		history.setViewLayoutAction(this.viewLayoutAction);
		history.setActivateSelection(this.viewIdActivate);
		history.setViewLayoutMode(this.viewLayoutMode);
		history.setTaskLaunchs(this.taskLaunchs);

		logger.log(Level.FINE, logPrefix+"getCurrentAsHistory End");

		return history;
	}

	/**
	 * Save the current ViewLayout Snapshot into history list
	 */
	private void historySnapshot() {

		logger.log(Level.FINE, logPrefix+"historySnapshot Begin");

		ViewLayoutHistory history = getCurrentAsHistory();

		viewLayoutHistoryMgr.debug("BF");

		viewLayoutHistoryMgr.add(history);
		
		viewLayoutHistoryMgr.debug("AF");

		logger.log(Level.FINE, logPrefix+"historySnapshot End");
	}
	
	private UITaskLaunch getTaskLaunchActivate() {
		UITaskLaunch taskLaunch = null;
		int viewIdActivate = this.viewIdActivate;
		if ( null != this.taskLaunchs ) {
			if ( isValidActivateId(viewIdActivate) ) {
				if ( null != this.taskLaunchs[viewIdActivate] ) {
					
					logger.log(Level.FINE, logPrefix+"getTaskLaunchFromTaskLaunchs viewIdActivate["+viewIdActivate+"]");
					
					taskLaunch = this.taskLaunchs[viewIdActivate];
					
				} else {
					logger.log(Level.FINE, logPrefix+"getTaskLaunchFromTaskLaunchs taskLaunchs["+viewIdActivate+"] is null");
				}
			} else {
				logger.log(Level.FINE, logPrefix+"getTaskLaunchFromTaskLaunchs isValidActivateId is false");
			}
		} else {
			logger.log(Level.FINE, logPrefix+"getTaskLaunchFromTaskLaunchs taskLaunchs is null");
			
		}
		return taskLaunch;
	}
	
	private void triggerProfileChange () {
		
		logger.log(Level.FINE, logPrefix+"triggerProfileChange Begin");
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		String profile = opmAuthentication.getCurrentProfile();
		String operator = opmAuthentication.getCurrentOperator();
		
		UITaskProfile taskProfile = new UITaskProfile();
		taskProfile.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskProfile.setUiPath(UIPathUIPanelStatusBar);
		taskProfile.setProfile(profile);
		taskProfile.setOperator(operator);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskProfile));
		
		logger.log(Level.FINE, logPrefix+"triggerProfileChange End");
	}
	
	private void triggerTitleChange ( UITaskLaunch taskLaunch ) {
		
		logger.log(Level.FINE, logPrefix+"triggerTitleChange Begin");
		
		String title = "";
		if ( null != taskLaunch ) {
			title = taskLaunch.getTitle();
		} else {
			logger.log(Level.FINE, logPrefix+"triggerTitleChange tasLaunch is null");
		}
		
		logger.log(Level.FINE, logPrefix+"triggerMenuChange UITaskTitle title["+title+"]");
		
		UITaskTitle taskTitle = new UITaskTitle();
		taskTitle.setTitle(title);
		taskTitle.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskTitle.setUiPath(UIPathUIPanelStatusBar);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskTitle));
		
		logger.log(Level.FINE, logPrefix+"triggerTitleChange End");
	}
	
	private void triggerMenuChange( UITaskLaunch taskLaunch ) {
		
		logger.log(Level.FINE, logPrefix+"triggerMenuChange Begin");
		
		if ( null != taskLaunch ) {
		
			UITaskLaunch taskLaunchToSend = new UITaskLaunch(taskLaunch);
			taskLaunchToSend.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunchToSend.setUiPath(UIPathUIPanelViewLayout);
			UIEvent uiEvent = new UIEvent(taskLaunchToSend);
			
//			this.uiNameCard.getUiEventBus().fireEvent(uiEvent);
		} else {
			logger.log(Level.FINE, logPrefix+"triggerMenuChange taskLaunch is null");
		}
		
		logger.log(Level.FINE, logPrefix+"triggerMenuChange End");
	}

}
