package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.HashMap;

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
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessor;
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

	private final String className = UIWidgetUtil.getClassSimpleName(ViewLayoutMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

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
		
		logger.begin(className, function);

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
		
		logger.end(className, function);
	}

	/**
	 * Update the Splie Screen UI
	 */
	private void triggerSplitChange() {
		final String function = "triggerSplitChange";
		
		logger.begin(className, function);
		
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
		
		logger.debug(className, function, "taskSplit1.getTaskType[{}]", taskSplit1.getTaskType());
		
		logger.debug(className, function, "taskSplit1 fire");

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
		
		logger.debug(className, function, "taskSplit2.getTaskType[{}]", taskSplit2.getTaskType());
		
		logger.debug(className, function, "taskSplit2 fire");

//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit2));

		logger.end(className, function);
		
	}
	
	public void setSplitScreen(UITaskSplit uiTaskSplit) {
		final String function = "setSplitScreen";
		
		logger.begin(className, function);
		
		ViewLayoutHistory viewLayoutHistory = null;

		if (null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}
		
		logger.debug(className, function, "uiTaskSplit.getSplitType()[{}]", uiTaskSplit.getTaskType());
		
		if (UITaskSplit.SplitType.Vertical == uiTaskSplit.getTaskType()) {
			
			logger.debug(className, function, "UITaskSplit.SplitType.Vertical");

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
		
		
		logger.end(className, function);
	}

	/**
	 * Set the internal status and update the ui
	 * 
	 * @param viewLayoutMode
	 * @param viewLayoutAction
	 */
	private void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction) {
		final String function = "setLayout";

		logger.begin(className, function);

		this.viewLayoutMode = viewLayoutMode;

		this.viewLayoutAction = viewLayoutAction;

		this.viewLayoutMgrEvent.setLayout(this.viewLayoutMode, this.viewLayoutAction);

		this.viewIdActivate = 0;

		this.taskLaunchs = null;

		this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];

		for (int i = 0; i < this.taskLaunchs.length; ++i) {
			this.taskLaunchs[i] = null;
		}

		logger.debug(className, function, "created taskLaunchs.length[{}]", this.taskLaunchs.length);

		logger.end(className, function);
	}

	private boolean isValidActivateId(int activateId) {
		final String function = "isValidActivateId";
		
		boolean result = false;
		logger.debug(className, function, "isValidActivate this.viewIdActivate[{}] >= 0 && this.viewIdActivate[{}] < taskLaunchs.length[{}]", new Object[]{this.viewIdActivate, this.viewIdActivate, this.taskLaunchs.length});
		if( this.viewIdActivate >= 0 && this.viewIdActivate < this.taskLaunchs.length ) {
			result = true;
		}
		logger.debug(className, function, "isValidActivate result[{}]", result);
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

		logger.begin(className, function);

		if (taskLaunch.getUiPanel().equals("ViewSchematicSymbolSelected")) {
			
			HashMap<String, String> options = taskLaunch.getOptions();
			
			String configurationId	= taskLaunch.getOption("configurationId");
			String hv_id			= taskLaunch.getOption("hv_id");

			logger.debug(className, function, "configurationId[{}]", configurationId);
			logger.debug(className, function, "hv_id[{}]", hv_id);
			
			String actionsetkey = hv_id;
			
			UIEventAction uiEventAction = null;
			
			String optsXMLFile = "UISchematic" + "/" + "UISchematic_"+configurationId+".opts.xml";
			String dictionariesCacheName = "UIWidgetGeneric";
			String tag = UIActionEventType.actionset.toString();
			
			logger.debug(className, function, "optsXMLFile[{}]", optsXMLFile);
			
			logger.debug(className, function, "dictionariesCacheName[{}]", dictionariesCacheName);
			
			logger.debug(className, function, "tag[{}]", tag);
			
			logger.debug(className, function, "Launch the UIEventActionProcessor...");
			
			UIEventActionProcessor_i uiEventActionProcessor_i = null;
			UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
			uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");
			
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
				
				logger.debug(className, function, "actionsetkey[{}] IS NOT NULL", actionsetkey);

				uiEventAction.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
				
				logger.debug(className, function, "uiEventAction IS NOT NULL");
				
				UIEventActionProcessor.dumpUIEventAction(className, uiEventAction);
				
				logger.debug(className, function, "Launch the uiEventAction...");
				
				uiEventActionProcessor_i.executeActionSet(uiEventAction, null);

			} else {
				
				logger.debug(className, function, "actionsetkey[{}] IS NULL", actionsetkey);

				UIInspectorMgr mgr = UIInspectorMgr.getInstance(Integer.toString(uiNameCard.getUiScreen()));
				mgr.closeInspectorDialog();
				mgr.getFunctionLocationAndLaunchInspectorDialog(uiNameCard, options);

			}
			
			uiEventAction = null;
			uiEventActionProcessor_i = null;

		} else {
			
			
			// Check the current task is not equals to target task
			
			logger.debug(className, function, "this.viewIdActivate[{}]", this.viewIdActivate);
			if ( isValidActivateId(this.viewIdActivate) ) {
				
				UITaskLaunch curTaskLaunch = this.taskLaunchs[this.viewIdActivate];
				if ( null != curTaskLaunch ) {
					int validCounter = 0;
					int validCount = 3;
					
					logger.debug(className, function, "taskLaunch.getTaskLaunchType()[{}] == curTaskLaunch.getTaskLaunchType()[{}]", taskLaunch.getTaskLaunchType(), curTaskLaunch.getTaskLaunchType());
					if ( taskLaunch.getTaskLaunchType() == curTaskLaunch.getTaskLaunchType() ) {
						validCounter++;
					}
					
					logger.debug(className, function, "taskLaunch.getUiPanel()[{}] == curTaskLaunch.getUiPanel()[{}]", taskLaunch.getUiPanel(), curTaskLaunch.getUiPanel());
					if ( taskLaunch.getUiPanel().equals(curTaskLaunch.getUiPanel()) ) {
						validCounter++;
					}
					
					logger.debug(className, function, "taskLaunch.getUiView()[{}] == curTaskLaunch.getUiView()[{}]", taskLaunch.getUiView(), curTaskLaunch.getUiView());
					if ( taskLaunch.getUiView().equals(curTaskLaunch.getUiView()) ) {
						validCounter++;
					}
					
					if ( validCounter >= validCount ) {
						logger.debug(className, function, "validCounter[{}] validCount[{}]", validCounter, validCount);
						return;
					}
				} else {
					logger.warn(className, function, "curTaskLaunch IS NULL"); 
				}
			} else {
				logger.warn(className, function, "this.viewIdActivate is INVALUD[{}] taskLaunchs.length[{}]", this.viewIdActivate, this.taskLaunchs.length);
			}
			
			
			// Kill inspector before open it
			UIInspectorMgr mgr = UIInspectorMgr.getInstance(Integer.toString(uiNameCard.getUiScreen()));
			mgr.closeInspectorDialog();
			
			if (TaskLaunchType.IMAGE == taskLaunch.getTaskLaunchType()) {
				logger.debug(className, function, "setTaskLaunch TaskType.IMAGE");

				if (ViewLayoutMode.Image != this.viewLayoutMode) {
					logger.debug(className, function, "setTaskLaunch ViewLayoutMode.Image != [{}]", this.viewLayoutMode);

					setLayout(ViewLayoutMode.Image, ViewLayoutAction.SingleLayout);

				}

			} else if (TaskLaunchType.PANEL == taskLaunch.getTaskLaunchType()) {
				logger.debug(className, function, "setTaskLaunch TaskType.PANEL");

				if (ViewLayoutMode.Panel != this.viewLayoutMode) {
					logger.debug(className, function, "setTaskLaunch ViewLayoutMode.Panel != [{}]", this.viewLayoutMode);

					setLayout(ViewLayoutMode.Panel, ViewLayoutAction.SingleLayout);

				}
			}

			viewLayoutMgrEvent.setTaskLaunch(taskLaunch, this.viewIdActivate);

			logger.debug(className, function, "this.viewLayoutAction[{}]", this.viewLayoutAction);

			if (null == this.taskLaunchs) {
				logger.debug(className, function, "this.taskLaunchs is null, creating...");
				this.taskLaunchs = new UITaskLaunch[this.viewLayoutAction.getValue()];
				logger.debug(className, function, "this.taskLaunchs created this.taskLaunchs.length[{}]", this.taskLaunchs.length);
			}


			if ( isValidActivateId(this.viewIdActivate) ) {
				this.taskLaunchs[this.viewIdActivate] = taskLaunch;
			} else {
				logger.debug(className, function, "this.viewIdActivate is INVALUD[{}] taskLaunchs.length[{}]", this.viewIdActivate, this.taskLaunchs.length);
			}
			
			triggerTitleChange(taskLaunch);

			logger.debug(className, function, "hasSnapshot[" + makeSnapshot + "]");

			if (makeSnapshot) {

				historySnapshot();
			}

			triggerSplitChange();

			triggerHistoryChange();
		}

		logger.end(className, function);

	}

	/**
	 * Restore the ViewLayout Snapshot
	 * 
	 * @param viewLayoutHistory
	 */
	private void restoreCurrentStatus(ViewLayoutHistory viewLayoutHistory) {
		final String function = "restoreCurrentStatus";

		logger.begin(className, function);

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

		logger.end(className, function);
	}

	/**
	 * @param viewLayoutAction
	 */
	public void setLayoutAction(ViewLayoutHistory viewLayoutHistory) {
		final String function = "setLayoutAction";

		logger.begin(className, function);

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		logger.end(className, function);
	}

	/**
	 * @param viewLayoutAction
	 * @param restorePreviousViews
	 */
	public void setLayoutAction(ViewLayoutAction viewLayoutAction, boolean restorePreviousViews) {
		final String function = "setLayoutAction";

		logger.begin(className, function);

		ViewLayoutHistory viewLayoutHistory = null;

		if (restorePreviousViews && null != this.taskLaunchs) {
			// Copy the previous views
			viewLayoutHistory = getCurrentAsHistory();
		}

		setLayout(viewLayoutHistory.getViewLayoutMode(), viewLayoutHistory.getViewLayoutAction());

		restoreCurrentStatus(viewLayoutHistory);

		historySnapshot();

		logger.end(className, function);
	}

	/**
	 * Trigger the TaskHistory UI (Button etc.) Update. Fire the TaskHistory
	 * with TaskType PreviousEnable, PreviousDisable, NextEnable, NextDisable
	 */
	private void triggerHistoryChange() {
		final String function = "triggerHistoryChange";

		logger.begin(className, function);

		UITaskHistory taskHistoryPrevious = new UITaskHistory();
		taskHistoryPrevious.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryPrevious.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasPrevious()) {

			logger.debug(className, function, "viewLayoutHistoryMgr has Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousEnable);

		} else {

			logger.debug(className, function, "viewLayoutHistoryMgr haven't Previous");

			taskHistoryPrevious.setTaskType(TaskType.PreviousDisable);
		}

		logger.debug(className, function, "taskHistoryPrevious fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryPrevious));

		UITaskHistory taskHistoryNext = new UITaskHistory();
		taskHistoryNext.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskHistoryNext.setUiPath(UIPathUIPanelAccessBar);

		if (this.viewLayoutHistoryMgr.hasNext()) {

			logger.debug(className, function, "viewLayoutHistoryMgr has Next");

			taskHistoryNext.setTaskType(TaskType.NextEnable);
		} else {

			logger.debug(className, function, "viewLayoutHistoryMgr is haven't Next ");

			taskHistoryNext.setTaskType(TaskType.NextDisable);
		}

		logger.debug(className, function, "taskHistoryNext fire");

		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistoryNext));

		logger.end(className, function);
	}

	/**
	 * Get the History Action from the passing taskHistory and Apply TaskHistory
	 * 
	 * @param taskHistory
	 */
	private void setTaskHistory(UITaskHistory taskHistory) {
		final String function = "setTaskHistory";

		logger.begin(className, function);

		ViewLayoutHistory viewLayoutHistory = null;

		if (TaskType.Previous == taskHistory.getTaskType()) {

			logger.debug(className, function, "taskHistory is TaskType.Previous");

			if (viewLayoutHistoryMgr.hasPrevious()) {

				viewLayoutHistory = viewLayoutHistoryMgr.previous();

			} else {

				logger.debug(className, function, "viewLayoutHistoryMgr haven't previous");

			}
		} else if (TaskType.Next == taskHistory.getTaskType()) {

			logger.debug(className, function, "taskHistory is TaskType.Next ");

			if (viewLayoutHistoryMgr.hasNext()) {

				viewLayoutHistory = viewLayoutHistoryMgr.next();

			} else {

				logger.debug(className, function, "viewLayoutHistoryMgr haven't next");

			}
		}

		if (null != viewLayoutHistory) {

			logger.debug(className, function, "viewLayoutHistory is not null");

			setLayoutAction(viewLayoutHistory);
		}

		triggerHistoryChange();

		logger.end(className, function);
	}

	/**
	 * @param uiEvent
	 */
	private void onUIEvent(UIEvent uiEvent) {
		final String function = "onUIEvent";

		logger.begin(className, function);
		
		if ( null != uiNameCard ) {
			logger.debug(className, function, "uiNameCard.getUiScreen()[{}]", uiNameCard.getUiScreen());
			if ( null == uiNameCard.getUiPath() ) {
				logger.warn(className, function, "uiNameCard.getUiPath()[{}] IS NULL", uiNameCard.getUiPath());
			}			
		} else {
			logger.warn(className, function, "uiNameCard IS NULL");
		}
		
		if ( null != uiEvent ) {
			logger.debug(className, function, "uiEvent.getTaskProvide().getTaskUiScreen()[{}]", uiEvent.getTaskProvide().getTaskUiScreen());
			UITask_i uiTask = uiEvent.getTaskProvide();
			if ( null != uiTask ) {
					if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
							&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {
						if ( uiTask instanceof UITaskLaunch ) {

							logger.debug(className, function, "taskProvide is TaskLaunch");

							setTaskLaunch((UITaskLaunch) uiTask, true);
						} else {
							if ( uiTask instanceof UITaskHistory ) {

								logger.debug(className, function, "taskProvide is TaskHistory");
	
								setTaskHistory((UITaskHistory) uiTask);
							} else if ( uiTask instanceof UITaskSplit ) {
								
								logger.debug(className, function, "taskProvide is UITaskSplit");
								
								setSplitScreen((UITaskSplit) uiTask);
							}
						}
					}
			} else {
				logger.warn(className, function, "uiEvent.getTaskProvide().getUiPath()[{}] IS NULL", uiEvent.getTaskProvide().getUiPath() );
			}
		} else {
			logger.warn(className, function, "uiEvent IS NULL");
		}

		logger.end(className, function);
	}

	/**
	 * @return
	 */
	public ViewLayoutMode getViewMode() {
		final String function = "getViewMode";

		logger.beginEnd(className, function);

		return this.viewLayoutMode;
	}

	/**
	 * @return
	 */
	public ViewLayoutAction getViewAction() {
		final String function = "getViewAction";

		logger.beginEnd(className, function);

		return this.viewLayoutAction;
	}

	/**
	 * @param viewIdActivate
	 */
	public void setViewIdActivate(int viewIdActivate) {
		final String function = "setViewIdActivate";

		logger.begin(className, function);

		this.viewIdActivate = viewIdActivate;
		
		UITaskLaunch taskLaunch = getTaskLaunchActivate();
		if ( null != taskLaunch ) {
			triggerMenuChange(taskLaunch);
			triggerTitleChange(taskLaunch);		
		}
		
		logger.end(className, function);
	}

	/**
	 * Generate Current ViewLayout Snapshot as the Snapshot object
	 * 
	 * @return
	 */
	private ViewLayoutHistory getCurrentAsHistory() {
		final String function = "getCurrentAsHistory";

		logger.begin(className, function);

		logger.debug(className, function, "this.taskLaunchs.length[{}]", this.taskLaunchs.length);

		ViewLayoutHistory history = new ViewLayoutHistory();
		history.setViewLayoutAction(this.viewLayoutAction);
		history.setActivateSelection(this.viewIdActivate);
		history.setViewLayoutMode(this.viewLayoutMode);
		history.setTaskLaunchs(this.taskLaunchs);

		logger.end(className, function);

		return history;
	}

	/**
	 * Save the current ViewLayout Snapshot into history list
	 */
	private void historySnapshot() {
		final String function = "historySnapshot";

		logger.begin(className, function);

		ViewLayoutHistory history = getCurrentAsHistory();

		viewLayoutHistoryMgr.debug("BF");

		viewLayoutHistoryMgr.add(history);
		
		viewLayoutHistoryMgr.debug("AF");

		logger.end(className, function);
	}
	
	private UITaskLaunch getTaskLaunchActivate() {
		final String function = "getTaskLaunchActivate";
		
		UITaskLaunch taskLaunch = null;
		int viewIdActivate = this.viewIdActivate;
		if ( null != this.taskLaunchs ) {
			if ( isValidActivateId(viewIdActivate) ) {
				if ( null != this.taskLaunchs[viewIdActivate] ) {
					
					logger.debug(className, function, "viewIdActivate[{}]", viewIdActivate);
					
					taskLaunch = this.taskLaunchs[viewIdActivate];
					
				} else {
					logger.debug(className, function, "taskLaunchs[{}] is null", viewIdActivate);
				}
			} else {
				logger.debug(className, function, "isValidActivateId is false");
			}
		} else {
			logger.debug(className, function, "taskLaunchs is null");
		}
		
		logger.end(className, function);
		return taskLaunch;
	}

	private void triggerTitleChange ( UITaskLaunch taskLaunch ) {
		final String function = "triggerTitleChange";
		
		logger.begin(className, function);
		
		String title = "";
		if ( null != taskLaunch ) {
			title = taskLaunch.getTitle();
		} else {
			logger.debug(className, function, "tasLaunch is null");
		}
		
		logger.debug(className, function, "UITaskTitle title[{}]", title);
		
		UITaskTitle taskTitle = new UITaskTitle();
		taskTitle.setTitle(title);
		taskTitle.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskTitle.setUiPath(UIPathUIPanelStatusBar);
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskTitle));
		
		logger.end(className, function);
	}
	
	private void triggerMenuChange( UITaskLaunch taskLaunch ) {
		final String function = "";
		
		logger.begin(className, function);
		
		if ( null != taskLaunch ) {
		
			UITaskLaunch taskLaunchToSend = new UITaskLaunch(taskLaunch);
			taskLaunchToSend.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunchToSend.setUiPath(UIPathUIPanelNavigationMgr);
			UIEvent uiEvent = new UIEvent(taskLaunchToSend);
			
			this.uiNameCard.getUiEventBus().fireEvent(uiEvent);
		} else {
			logger.debug(className, function, "taskLaunch is null");
		}
		
		logger.end(className, function);
	}

}
