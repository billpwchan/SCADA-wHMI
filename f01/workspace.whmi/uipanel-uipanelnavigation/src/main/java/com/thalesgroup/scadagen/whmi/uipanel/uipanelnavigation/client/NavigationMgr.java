package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.thalesgroup.scadagen.whmi.config.config.shared.Config;
import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ConfigMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskMgr;
import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskMgrEvent;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;


public class NavigationMgr implements TaskMgrEvent, ConfigMgrEvent {
	
	private static Logger logger = Logger.getLogger(NavigationMgr.class.getName());
	
	// Caches
	private ArrayList<UITaskLaunch> taskLaunchs = null;
	private int taskLaunchsParentLevel = 0;
	private String taskLaunchsParentHeader = "";
	
	private TaskMgr taskMgr = null;
	
	
	private ConfigMgr configMgr = null;
//	private ArrayList<UITaskLaunchDictionary> uiTaskLaunchDictionarys = null;
//	private int uiTaskLaunchDictionarysParentLevel = 0;
//	private String uiTaskLaunchDictionarysParentHeader = "";

	private NavigationMgrEvent navigationMgrEvent = null;
	
	private UINameCard uiNameCard = null;
	public NavigationMgr (UINameCard uiNameCard ) {
		
		logger.log(Level.FINE, "NavigationMgr Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendMgr(this);
		
		logger.log(Level.FINE, "NavigationMgr this.uiNameCard.getUiPath()["+this.uiNameCard.getUiPath()+"]");
		
		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}	
		});
		
		logger.log(Level.FINE, "NavigationMgr End");
	}


	public void setNavigationMgrEvent (NavigationMgrEvent navigationMgrEvent) {
		
		logger.log(Level.FINE, "setNavigationMgrEvent Begin");
		
		this.navigationMgrEvent = navigationMgrEvent;
		
		logger.log(Level.FINE, "setNavigationMgrEvent End");
	}
		

	public ArrayList<UITaskLaunch> getTasks ( int level, String header ) {
		
		logger.log(Level.FINE, "getTasks Begin");
		
		logger.log(Level.FINE, "getTaskLaunchs level["+level+"] header["+header+"]");
		
		ArrayList<UITaskLaunch> menu = null;
		
		if ( null != this.taskLaunchs ) {
		
			logger.log(Level.FINE, "getTaskLaunchs this.taskLaunchs["+this.taskLaunchs.size()+"]");
			
			menu = new ArrayList<UITaskLaunch>();
			
			for ( UITaskLaunch taskLaunch: this.taskLaunchs){
				if ( taskLaunch.getHeader().startsWith(header) ) {
					if ( taskLaunch.getTaskLevel() == level ) {
						menu.add(taskLaunch);
					}
				}				
			}
			
		} else {
			logger.log(Level.FINE, "getTaskLaunchs this.taskLaunchs is null");
		}
		
		logger.log(Level.FINE, "getTasks End");
		
		return menu;
	}
	
	public void initCache(int level, String header) {
		
		logger.log(Level.FINE, "initCache Begin");
		
		String profile = "";
		String location = "";
		
		this.taskLaunchs = null;
		this.taskLaunchs = new ArrayList<UITaskLaunch>();
		
		this.taskMgr = TaskMgr.getInstance();
		this.taskMgr.setTaskMgrEvent(this);
		this.taskMgr.initTasks(profile, location, level, header);
		
		this.configMgr = new ConfigMgr();
		String module = GWT.getModuleName();
		this.configMgr.getConfigs(module, "navigationMenu.xml", "option", this);
		
		logger.log(Level.FINE, "initCache End");
		
	}
	
	
	@Override
	public void ready(Tasks tasks) {
		
		logger.log(Level.FINE, "ready Begin");
		
		taskLaunchs = null;
		
		taskLaunchs = new ArrayList<UITaskLaunch>();
		
		if ( null != tasks ) {
			
			if ( tasks.size() > 0 ) {

				this.taskLaunchsParentLevel = tasks.getParentLevel();
				this.taskLaunchsParentHeader = tasks.getParentHeader();
				
				logger.log(Level.FINE, "ready this.taskLaunchsParentLevel["+this.taskLaunchsParentLevel+"]");
				logger.log(Level.FINE, "ready this.taskLaunchsParentHeader["+this.taskLaunchsParentHeader+"]");
				
				for ( Task task: tasks){
					UITaskLaunch taskLaunch = new UITaskLaunch(task);
					this.taskLaunchs.add(taskLaunch);
				}
				
				logger.log(Level.FINE, "ready tasks.size["+this.taskLaunchs.size()+"]");
				
			} else {
				
				logger.log(Level.FINE, "ready tasks is zero size");
				
			}
			
		} else {
			
			logger.log(Level.FINE, "ready tasks is null");
			
		}
		
		logger.log(Level.FINE, "ready calling navigationMgrEvent.isReady...");
		
		navigationMgrEvent.isReady(this.taskLaunchsParentLevel, this.taskLaunchsParentHeader);
		
		logger.log(Level.FINE, "ready End");
	}
	
	@Override
	public void failed() {
		
		logger.log(Level.FINE, "failed Begin/End");
		
	}
	
	private void onUIEvent(UIEvent uiEvent){
		
		logger.log(Level.FINE, "onUIEvent Begin");
		
		if (null != uiEvent) {

			UITask_i taskProvide = uiEvent.getTaskProvide();
			
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if (UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide)) {

						logger.log(Level.FINE, "onUIEvent taskProvide is TaskLaunch");
						
						UITaskLaunch tasklaunch = (UITaskLaunch)taskProvide;

						this.navigationMgrEvent.setMenu(0, "", tasklaunch.getHeader(), false);
//						this.navigationMgrEvent.setSelected(tasklaunch.getTaskLevel(), tasklaunch.getHeader());

					}
				}
			}
		}

		logger.log(Level.FINE, "onUIEvent End");
	}


	@Override
	public void ready(Configs configs) {
		logger.log(Level.FINE, "ready configs");
		
//		this.uiTaskLaunchDictionarys = null;
//		
//		this.uiTaskLaunchDictionarys = new ArrayList<UITaskLaunchDictionary>();
		
		if ( null != configs ) {
			String xml = (String) configs.getHeader("XmlFile");
			logger.log(Level.FINE, "ready xml["+xml+"]");
			for ( int i = 0 ; i < configs.getObjectSize() ; ++i ) {
				Config cfg = (Config) configs.getConfig(i);
				String attrubuteKey = "key";
				String attrubuteValue = (String) cfg.getValue("key");
				logger.log(Level.FINE, "ready attrubuteKey["+attrubuteKey+"] attrubuteValue["+attrubuteValue+"]");
				
				for ( String key: cfg.getValueKeys() ) {
					String content = (String) cfg.getValue(key);
					logger.log(Level.FINE, "ready key["+key+"] content["+content+"]");
				}

			}
		} else {
			logger.log(Level.FINE, "ready configs is null");
		}
	}


	@Override
	public void failed(String xmlFile) {
		logger.log(Level.FINE, "failed xmlFile["+xmlFile+"]");
	}

}
