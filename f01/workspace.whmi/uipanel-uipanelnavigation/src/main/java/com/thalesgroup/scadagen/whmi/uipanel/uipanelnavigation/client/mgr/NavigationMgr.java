package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr;

import java.util.ArrayList;

import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskMgr;
import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskMgrEvent;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.util.HeaderKeyMapping;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class NavigationMgr implements TaskMgrEvent {
	
	private final String className = UIWidgetUtil.getClassSimpleName(NavigationMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	// Caches
	private ArrayList<UITaskLaunch> taskLaunchs = null;
	private int taskLaunchsParentLevel = 0;
	private String taskLaunchsParentHeader = "";
	
	private final String targetKey = "{}";
	private final String spliter = "\\|";
	
	private TaskMgr taskMgr = null;
	
	private NavigationMgrEvent navigationMgrEvent = null;
	
	private UINameCard uiNameCard = null;
	public NavigationMgr (UINameCard uiNameCard ) {
		final String function = "NavigationMgr";
		
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendMgr(this);
		
		logger.debug(className, function, "this.uiNameCard.getUiPath()[{}]", this.uiNameCard.getUiPath());
		
		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}	
		});
		
		logger.end(className, function);
	}

	public void setNavigationMgrEvent (NavigationMgrEvent navigationMgrEvent) {
		final String function = "setNavigationMgrEvent";
		
		logger.begin(className, function);
		
		this.navigationMgrEvent = navigationMgrEvent;
		
		logger.end(className, function);
	}
		

	public ArrayList<UITaskLaunch> getTasks ( int level, String header ) {
		final String function = "getTasks";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "level[{}] header[{}]", level, header);
		
		ArrayList<UITaskLaunch> menu = null;
		
		if ( null != this.taskLaunchs ) {
		
			logger.debug(className, function, "this.taskLaunchs[{}]", this.taskLaunchs.size());
			
			menu = new ArrayList<UITaskLaunch>();
			
			for ( UITaskLaunch taskLaunch: this.taskLaunchs){
				if ( taskLaunch.getHeader().startsWith(header) ) {
					if ( taskLaunch.getTaskLevel() == level ) {
						menu.add(taskLaunch);
					}
				}				
			}
			
		} else {
			logger.warn(className, function, "this.taskLaunchs is null");
		}
		
		logger.end(className, function);
		
		return menu;
	}
	
	public void initCache(int level, String header) {
		final String function = "initCache";
		
		logger.begin(className, function);
		
		String profile = "";
		String location = "";
		
		this.taskLaunchs = null;
		this.taskLaunchs = new ArrayList<UITaskLaunch>();
		
		this.taskMgr = TaskMgr.getInstance();
		this.taskMgr.setTaskMgrEvent(this);
		this.taskMgr.initTasks(profile, location, level, header);
		
		logger.end(className, function);
		
	}
	
	@Override
	public void ready(Tasks tasks) {
		final String function = "ready";
		
		logger.begin(className, function);
		
		taskLaunchs = null;
		
		taskLaunchs = new ArrayList<UITaskLaunch>();
		
		if ( null != tasks ) {
			
			if ( tasks.size() > 0 ) {

				this.taskLaunchsParentLevel = tasks.getParentLevel();
				this.taskLaunchsParentHeader = tasks.getParentHeader();
				
				logger.debug(className, function, "this.taskLaunchsParentLevel[{}]", this.taskLaunchsParentLevel);
				logger.debug(className, function, "this.taskLaunchsParentHeader[{}]", this.taskLaunchsParentHeader);
				
				for ( Task task: tasks){
					UITaskLaunch taskLaunch = new UITaskLaunch(task);
					this.taskLaunchs.add(taskLaunch);
				}
				logger.debug(className, function, "tasks.size[{}]", this.taskLaunchs.size());
			} else {
				logger.debug(className, function, "tasks is zero size");
			}
		} else {
			logger.debug(className, function, "tasks is null");
		}
		
		logger.debug(className, function, "calling navigationMgrEvent.isReady...");
		
		navigationMgrEvent.isReady(this.taskLaunchsParentLevel, this.taskLaunchsParentHeader);
		
		logger.end(className, function);
	}
	
	@Override
	public void failed() {
		final String function = "failed";
		logger.beginEnd(className, function);
		
	}
	
	private void onUIEvent(UIEvent uiEvent) {
		final String function = "onUIEvent";
		logger.begin(className, function);
		
		if (null != uiEvent) {

			UITask_i taskProvide = uiEvent.getTaskProvide();
			
			if (null != taskProvide) {
				
				logger.debug(className, function, "uiNameCard.getUiScreen()[{}] == taskProvide.getTaskUiScreen()[{}]", uiNameCard.getUiScreen(), taskProvide.getTaskUiScreen());
				logger.debug(className, function, "uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if (uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath())) {

					if ( taskProvide instanceof UITaskLaunch ) {

						logger.debug(className, function, "taskProvide is TaskLaunch");
						
						UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
						
						String newTarget = HeaderKeyMapping.replace(getCurrentHeader(), taskLaunch.getHeader(), spliter, targetKey);
						if ( null != newTarget ) {
							taskLaunch.setHeader(newTarget);
						}
						
						String header = taskLaunch.getHeader();
						boolean execute = Boolean.parseBoolean(taskLaunch.getExecute());
						this.navigationMgrEvent.setMenu(0, "", header, execute);
					}
				}
			}
		}

		logger.end(className, function);
	}
	
	private String currentHeader = null;
	public void setCurrentHeader(String header) { currentHeader = header; }
	public String getCurrentHeader() { return currentHeader; }
	
	public void launchTask(UITaskLaunch taskLaunch) {
		final String function = "launchTask";
		logger.begin(className, function);
		setCurrentHeader(taskLaunch.getHeader());
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		logger.end(className, function);
	}

}
