package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dialog.UIInspectorMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.history.ViewLayoutHistoryMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory.TaskType;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorCoreUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;

/**
 * @author syau
 *
 */
public class ViewLayoutMgr {

	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private ViewLayoutMgrEvent viewLayoutMgrEvent = null;
	private ViewLayoutHistoryMgr viewLayoutHistoryMgr = null;
	private int viewIdActivate = 0;
	private ViewLayoutMode viewLayoutMode;
	private ViewLayoutAction viewLayoutAction;
	private UITaskLaunch taskLaunchs[];
	
	private UINameCard uiNameCard = null;
	
	private final String UIPathUIPanelNavigationMgr = ":UIGws:UIPanelScreen:UIScreenMMI:NavigationMgr";
	private final String UIPathUIPanelAccessBar		= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelAccessBar";
	private final String UIPathUIPanelStatusBar		= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelStatusBar";
	/**
	 * @param viewLayoutMgrEvent
	 * @param uiNameCard
	 */
	public ViewLayoutMgr(ViewLayoutMgrEvent viewLayoutMgrEvent, UINameCard uiNameCard) {
		final String function = "ViewLayoutMgr";
		
		logger.begin(function);

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
		
		logger.end(function);
	}

	/**
	 * Update the Splie Screen UI
	 */
	private void triggerSplitChange() {
		final String function = "triggerSplitChange";
		
		logger.begin(function);
		
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
		
		logger.debug(function, "taskSplit1.getTaskType[{}]", taskSplit1.getTaskType());
		
		logger.debug(function, "taskSplit1 fire");

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
		
		logger.debug(function, "taskSplit2.getTaskType[{}]", taskSplit2.getTaskType());
		
		logger.debug(function, "taskSplit2 fire");

//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit2));

		logger.end(function);
		
	}
	
	public void setSplitScreen(UITaskSplit uiTaskSplit) {
		final String function = "setSplitScreen";
		
		logger.begin(function);
		
		ViewLayoutHistory viewLayoutHistory = null;

		if (null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}
		
		logger.debug(function, "uiTaskSplit.getSplitType()[{}]", uiTaskSplit.getTaskType());
		
		if (UITaskSplit.SplitType.Vertical == uiTaskSplit.getTaskType()) {
			
			logger.debug(function, "UITaskSplit.SplitType.Vertical");

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
		
		
		logger.end(function);
	}

	/**
	 * Set the internal status and update the ui
	 * 
	 * @param viewLayoutMode
	 * @param viewLayoutAction
	 */
	private void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction) {
		final String function = "setLayout";

		logger.begin(function);

		this.viewLayoutMode = viewLayoutMode;

		this.viewLayoutAction = viewLayoutAction;

		this.viewLayoutMgrEvent.setLayout(this.viewLayoutMode, this.viewLayoutAction);

		this.viewIdActivate = 0;

		this.taskLaunchs = null;

		this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];

		for (int i = 0; i < this.taskLaunchs.length; ++i) {
			this.taskLaunchs[i] = null;
		}

		logger.debug(function, "created taskLaunchs.length[{}]", this.taskLaunchs.length);

		logger.end(function);
	}

	private boolean isValidActivateId(int activateId) {
		final String function = "isValidActivateId";
		
		boolean result = false;
		logger.debug(function, "isValidActivate this.viewIdActivate[{}] >= 0 && this.viewIdActivate[{}] < taskLaunchs.length[{}]", new Object[]{this.viewIdActivate, this.viewIdActivate, this.taskLaunchs.length});
		if( this.viewIdActivate >= 0 && this.viewIdActivate < this.taskLaunchs.length ) {
			result = true;
		}
		logger.debug(function, "isValidActivate result[{}]", result);
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
		final String function = "setTaskLaunch";

		logger.begin(function);

		if (taskLaunch.getUiCtrl().equals("ViewSchematicSymbolSelected")) {
			
			Map<String, String> options = taskLaunch.getOptions();
			
			String configurationId	= taskLaunch.getOption("configurationId");
			String hv_id			= taskLaunch.getOption("hv_id");

			logger.debug(function, "configurationId[{}]", configurationId);
			logger.debug(function, "hv_id[{}]", hv_id);
			
			String actionsetkey = hv_id;
			
			UIEventAction uiEventAction = null;
			
			String optsXMLFile = "UISchematic" + "/" + "UISchematic_"+configurationId+".opts.xml";
			String dictionariesCacheName = "UIWidgetGeneric";
			String tag = UIActionEventType.actionset.toString();
			
			logger.debug(function, "optsXMLFile[{}]", optsXMLFile);
			
			logger.debug(function, "dictionariesCacheName[{}]", dictionariesCacheName);
			
			logger.debug(function, "tag[{}]", tag);
			
			logger.debug(function, "Launch the UIEventActionProcessor...");
			
			UIEventActionProcessor_i uiEventActionProcessor_i = null;
			UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
			uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");
			
			uiEventActionProcessor_i.setUINameCard(uiNameCard);
			uiEventActionProcessor_i.setPrefix(className);
//			uiEventActionProcessor.setElement(element);
			uiEventActionProcessor_i.setDictionariesCacheName(dictionariesCacheName);
//			uiEventActionProcessor.setEventBus(eventBus);
			uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//			uiEventActionProcessor.setUIGeneric(uiWidgetGeneric);
			uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
			uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
			uiEventActionProcessor_i.init();

			uiEventAction = uiEventActionProcessor_i.getUIEventActionSetMgr(actionsetkey);

			if ( null != uiEventAction ) {
				
				logger.debug(function, "actionsetkey[{}] IS NOT NULL", actionsetkey);

				uiEventAction.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
				
				logger.debug(function, "uiEventAction IS NOT NULL");
				
				new UIEventActionProcessorCoreUtil().dumpUIEventAction(className, uiEventAction);
				
				logger.debug(function, "Launch the uiEventAction...");
				
				uiEventActionProcessor_i.executeActionSet(uiEventAction, null);

			} else {
				
				logger.debug(function, "actionsetkey[{}] IS NULL", actionsetkey);

				UIInspectorMgr mgr = UIInspectorMgr.getInstance(Integer.toString(uiNameCard.getUiScreen()));
				mgr.closeInspectorDialog();
				mgr.getFunctionLocationAndLaunchInspectorDialog(uiNameCard, options);

			}
			
			uiEventAction = null;
			uiEventActionProcessor_i = null;

		} else {
			
			// Check the current task is not equals to target task
			logger.debug(function, "this.viewIdActivate[{}]", this.viewIdActivate);
			if ( isValidActivateId(this.viewIdActivate) ) {
				
				UITaskLaunch curTaskLaunch = this.taskLaunchs[this.viewIdActivate];
				if ( null != curTaskLaunch ) {
					if ( taskLaunch.isEquals(curTaskLaunch) ) {
						logger.debug(function, "taskLaunch and curTaskLaunch IS EQUAL, skip the launch task..."); 
						return;
					}
				} else {
					logger.warn(function, "curTaskLaunch IS NULL"); 
				}
			} else {
				logger.warn(function, "this.viewIdActivate is INVALUD[{}] taskLaunchs.length[{}]", this.viewIdActivate, this.taskLaunchs.length);
			}
			
			// Kill inspector before open it
			UIInspectorMgr mgr = UIInspectorMgr.getInstance(Integer.toString(uiNameCard.getUiScreen()));
			mgr.closeInspectorDialog();
			
			if (TaskLaunchType.IMAGE == taskLaunch.getTaskLaunchType()) {
				logger.debug(function, "setTaskLaunch TaskType.IMAGE");

				if (ViewLayoutMode.Image != this.viewLayoutMode) {
					logger.debug(function, "setTaskLaunch ViewLayoutMode.Image != [{}]", this.viewLayoutMode);

					setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);

				}

			} else if (TaskLaunchType.PANEL == taskLaunch.getTaskLaunchType()) {
				logger.debug(function, "setTaskLaunch TaskType.PANEL");

				if (ViewLayoutMode.Panel != this.viewLayoutMode) {
					logger.debug(function, "setTaskLaunch ViewLayoutMode.Panel != [{}]", this.viewLayoutMode);

					setLayout(ViewLayoutMode.Panel, ViewLayoutAction.SingleLayout);

				}
			}

			viewLayoutMgrEvent.setTaskLaunch(taskLaunch, this.viewIdActivate);

			logger.debug(function, "this.viewLayoutAction[{}]", this.viewLayoutAction);

			if (null == this.taskLaunchs) {
				logger.debug(function, "this.taskLaunchs is null, creating...");
				this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];
				logger.debug(function, "this.taskLaunchs created this.taskLaunchs.length[{}]", this.taskLaunchs.length);
			}


			if ( isValidActivateId(this.viewIdActivate) ) {
				this.taskLaunchs[this.viewIdActivate] = taskLaunch;
			} else {
				logger.debug(function, "this.viewIdActivate is INVALUD[{}] taskLaunchs.length[{}]", this.viewIdActivate, this.taskLaunchs.length);
			}
			
			triggerTitleChange(taskLaunch);

			logger.debug(function, "hasSnapshot[" + makeSnapshot + "]");

			if (makeSnapshot) {

				historySnapshot();
			}

			triggerSplitChange();

			triggerHistoryChange();
		}

		logger.end(function);

	}

	/**
	 * Restore the ViewLayout Snapshot
	 * 
	 * @param viewLayoutHistory
	 */
	private void restoreCurrentStatus(ViewLayoutHistory viewLayoutHistory) {
		final String function = "restoreCurrentStatus";

		logger.begin(function);

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

		logger.end(function);
	}

	/**
	 * @param viewLayoutAction
	 */
	public void setLayoutAction(ViewLayoutHistory viewLayoutHistory) {
		final String function = "setLayoutAction";

		logger.begin(function);

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		logger.end(function);
	}

	/**
	 * @param viewLayoutAction
	 * @param restorePreviousViews
	 */
	public void setLayoutAction(ViewLayoutAction viewLayoutAction, boolean restorePreviousViews) {
		final String function = "setLayoutAction";

		logger.begin(function);

		ViewLayoutHistory viewLayoutHistory = null;

		if (restorePreviousViews && null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		historySnapshot();

		logger.end(function);
	}

	/**
	 * Trigger the TaskHistory UI (Button etc.) Update. Fire the TaskHistory
	 * with TaskType PreviousEnable, PreviousDisable, NextEnable, NextDisable
	 */
	private void triggerHistoryChange() {
		final String function = "triggerHistoryChange";

		logger.begin(function);

		UITaskHistory taskHistoryPrevious = new UITaskHistory();
		taskHistoryPrevious.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryPrevious.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasPrevious()) {

			logger.debug(function, "viewLayoutHistoryMgr has Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousEnable);

		} else {

			logger.debug(function, "viewLayoutHistoryMgr haven't Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousDisable);
		}

		logger.debug(function, "taskHistoryPrevious fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryPrevious));

		UITaskHistory taskHistoryNext = new UITaskHistory();
		taskHistoryNext.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryNext.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasNext()) {

			logger.debug(function, "viewLayoutHistoryMgr has Next");

			taskHistoryNext.setTaskType(TaskType.NextEnable);
		} else {

			logger.debug(function, "viewLayoutHistoryMgr is haven't Next ");

			taskHistoryNext.setTaskType(TaskType.NextDisable);
		}

		logger.debug(function, "taskHistoryNext fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryNext));

		logger.end(function);
	}

	/**
	 * Get the History Action from the passing taskHistory and Apply TaskHistory
	 * 
	 * @param taskHistory
	 */
	private void setTaskHistory(UITaskHistory taskHistory) {
		final String function = "setTaskHistory";

		logger.begin(function);

		ViewLayoutHistory viewLayoutHistory = null;

		if (TaskType.Previous == taskHistory.getTaskType()) {

			logger.debug(function, "taskHistory is TaskType.Previous");

			if (viewLayoutHistoryMgr.hasPrevious()) {

				viewLayoutHistory = viewLayoutHistoryMgr.previous();

			} else {

				logger.debug(function, "viewLayoutHistoryMgr haven't previous");

			}
		} else if (TaskType.Next == taskHistory.getTaskType()) {

			logger.debug(function, "taskHistory is TaskType.Next ");

			if (viewLayoutHistoryMgr.hasNext()) {

				viewLayoutHistory = viewLayoutHistoryMgr.next();

			} else {

				logger.debug(function, "viewLayoutHistoryMgr haven't next");

			}
		}

		if (null != viewLayoutHistory) {

			logger.debug(function, "viewLayoutHistory is not null");

			setLayoutAction(viewLayoutHistory);
		}

		triggerHistoryChange();

		logger.end(function);
	}

	/**
	 * @param uiEvent
	 */
	private void onUIEvent(UIEvent uiEvent) {
		final String function = "onUIEvent";

		logger.begin(function);
		
		if ( null != uiNameCard ) {
			logger.debug(function, "uiNameCard.getUiScreen()[{}]", uiNameCard.getUiScreen());
			if ( null == uiNameCard.getUiPath() ) {
				logger.warn(function, "uiNameCard.getUiPath()[{}] IS NULL", uiNameCard.getUiPath());
			}			
		} else {
			logger.warn(function, "uiNameCard IS NULL");
		}
		
		if ( null != uiEvent ) {
			logger.debug(function, "uiEvent.getTaskProvide().getTaskUiScreen()[{}]", uiEvent.getTaskProvide().getTaskUiScreen());
			UITask_i uiTask = uiEvent.getTaskProvide();
			if ( null != uiTask ) {
					if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
							&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {
						if ( uiTask instanceof UITaskLaunch ) {

							logger.debug(function, "taskProvide is TaskLaunch");

							setTaskLaunch((UITaskLaunch) uiTask, true);
						} else {
							if ( uiTask instanceof UITaskHistory ) {

								logger.debug(function, "taskProvide is TaskHistory");
	
								setTaskHistory((UITaskHistory) uiTask);
							} else if ( uiTask instanceof UITaskSplit ) {
								
								logger.debug(function, "taskProvide is UITaskSplit");
								
								setSplitScreen((UITaskSplit) uiTask);
							}
						}
					}
			} else {
				logger.warn(function, "uiEvent.getTaskProvide().getUiPath()[{}] IS NULL", uiEvent.getTaskProvide().getUiPath() );
			}
		} else {
			logger.warn(function, "uiEvent IS NULL");
		}

		logger.end(function);
	}

	/**
	 * @return
	 */
	public ViewLayoutMode getViewMode() {
		final String function = "getViewMode";

		logger.beginEnd(function);

		return this.viewLayoutMode;
	}

	/**
	 * @return
	 */
	public ViewLayoutAction getViewAction() {
		final String function = "getViewAction";

		logger.beginEnd(function);

		return this.viewLayoutAction;
	}

	/**
	 * @param viewIdActivate
	 */
	public void setViewIdActivate(int viewIdActivate) {
		final String function = "setViewIdActivate";

		logger.begin(function);

		this.viewIdActivate = viewIdActivate;
		
		UITaskLaunch taskLaunch = getTaskLaunchActivate();
		if ( null != taskLaunch ) {
			triggerMenuChange(taskLaunch);
			triggerTitleChange(taskLaunch);		
		}
		
		logger.end(function);
	}

	/**
	 * Generate Current ViewLayout Snapshot as the Snapshot object
	 * 
	 * @return
	 */
	private ViewLayoutHistory getCurrentAsHistory() {
		final String function = "getCurrentAsHistory";

		logger.begin(function);

		logger.debug(function, "this.taskLaunchs.length[{}]", this.taskLaunchs.length);

		ViewLayoutHistory history = new ViewLayoutHistory();
		history.setViewLayoutAction(this.viewLayoutAction);
		history.setActivateSelection(this.viewIdActivate);
		history.setViewLayoutMode(this.viewLayoutMode);
		history.setTaskLaunchs(this.taskLaunchs);

		logger.end(function);

		return history;
	}

	/**
	 * Save the current ViewLayout Snapshot into history list
	 */
	private void historySnapshot() {
		final String function = "historySnapshot";

		logger.begin(function);

		ViewLayoutHistory history = getCurrentAsHistory();

		viewLayoutHistoryMgr.debug("BF");

		viewLayoutHistoryMgr.add(history);
		
		viewLayoutHistoryMgr.debug("AF");

		logger.end(function);
	}
	
	private UITaskLaunch getTaskLaunchActivate() {
		final String function = "getTaskLaunchActivate";
		
		UITaskLaunch taskLaunch = null;
		int viewIdActivate = this.viewIdActivate;
		if ( null != this.taskLaunchs ) {
			if ( isValidActivateId(viewIdActivate) ) {
				if ( null != this.taskLaunchs[viewIdActivate] ) {
					
					logger.debug(function, "viewIdActivate[{}]", viewIdActivate);
					
					taskLaunch = this.taskLaunchs[viewIdActivate];
					
				} else {
					logger.debug(function, "taskLaunchs[{}] is null", viewIdActivate);
				}
			} else {
				logger.debug(function, "isValidActivateId is false");
			}
		} else {
			logger.debug(function, "taskLaunchs is null");
		}
		
		logger.end(function);
		return taskLaunch;
	}

	private void triggerTitleChange ( UITaskLaunch taskLaunch ) {
		final String function = "triggerTitleChange";
		
		logger.begin(function);
		
		String title = "";
		if ( null != taskLaunch ) {
			title = taskLaunch.getTitle();
		} else {
			logger.debug(function, "tasLaunch is null");
		}
		
		logger.debug(function, "UITaskTitle title[{}]", title);
		
		UITaskTitle taskTitle = new UITaskTitle();
		taskTitle.setTitle(title);
		taskTitle.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskTitle.setUiPath(UIPathUIPanelStatusBar);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskTitle));
		
		logger.end(function);
	}
	
	private void triggerMenuChange( UITaskLaunch taskLaunch ) {
		final String function = "";
		
		logger.begin(function);
		
		if ( null != taskLaunch ) {
		
			UITaskLaunch taskLaunchToSend = new UITaskLaunch(taskLaunch);
			taskLaunchToSend.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunchToSend.setUiPath(UIPathUIPanelNavigationMgr);
			UIEvent uiEvent = new UIEvent(taskLaunchToSend);
			
			this.uiNameCard.getUiEventBus().fireEvent(uiEvent);
		} else {
			logger.debug(function, "taskLaunch is null");
		}
		
		logger.end(function);
	}

}
