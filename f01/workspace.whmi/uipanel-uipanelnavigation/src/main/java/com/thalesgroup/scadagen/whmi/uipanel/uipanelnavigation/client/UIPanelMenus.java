package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelMenus extends UIWidget_i implements NavigationMgrEvent {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelMenus.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
		
	private NavigationMgr navigationMgr = null;

	private HashMap<Integer, Panel> menus = new HashMap<Integer, Panel>();

	private HashMap<String, UIButtonNavigation> buttons = new HashMap<String, UIButtonNavigation>();
	
	public void addMenuBar(int index, Panel menu) {
		final String function = "addMenuBar";
		
		logger.begin(className, function);
		
		Integer iIndex = Integer.valueOf(index);
		logger.info(className, function, "addMenuBar index[{}] ==> iIndex[{}]", index, iIndex);
		
		menus.put(iIndex, menu);
		
		logger.end(className, function);
	}

	public Panel getMenuBar(int index) {
		final String function = "getMenuBar";
		
		logger.begin(className, function);
		
		Panel panel = menus.get(Integer.valueOf(index));
		
		logger.end(className, function);
		return panel;
	}

	private UINameCard uiNameCard = null;
	public UIPanelMenus(UINameCard uiNameCard) {
		final String function = "UIPanelMenus";
		
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		
		navigationMgr = new NavigationMgr(uiNameCard);
		navigationMgr.setNavigationMgrEvent(this);
		
		logger.end(className, function);
			
	}

	public UIPanelMenus() {
		// TODO Auto-generated constructor stub
	}
	
	public Panel getMenu(String menuLevel, String menuType) {
		final String function = "getMenu";
		
		logger.begin(className, function);
		logger.info(className, function, "level[{}] menuType[{}]", menuLevel, menuType);
		int level = -1;
		try {
			level = Integer.parseInt(menuLevel);
		} catch ( NumberFormatException e) {
			logger.warn(className, function, "menuLevel[{}] IS INVALID", menuLevel);
			logger.warn(className, function, "e[{}]", e.toString());
		}
		Panel panel = null;
		
		 if ( 0 == menuType.compareTo("0") ) {
			 // FlowPanel = 0
			 panel = this.getFlowMenu(level);
		} else if ( 0 == menuType.compareTo("1") ) {
			// HorizontalPanel = 1
			panel = this.getHorizontalMenu(level);
		} else if ( 0 == menuType.compareTo("2") ) {
			// VerticalPanel = 2
			panel = this.getVerticalMenu(level);
		} else {
			logger.warn(className, function, "menuType[{}] IS INVALID", menuType);
		}
		logger.end(className, function);
		return panel;
	}

	public HorizontalPanel getHorizontalMenu(int level) {
		final String function = "getHorizontalMenu";
		
		logger.begin(className, function);
		logger.info(className, function, "getHorizontalMenu level[{}]", level);
		
		HorizontalPanel menuBar = new HorizontalPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(className, function);
		
		return menuBar;
	}

	public VerticalPanel getVerticalMenu(int level) {
		final String function = "getVerticalMenu";
		
		logger.begin(className, function);
		logger.info(className, function, "getVerticalMenu level[{}]", level);
		
		VerticalPanel menuBar = new VerticalPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(className, function);
		
		return menuBar;
	}

	public FlowPanel getFlowMenu(int level) {
		final String function = "getFlowMenu";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getFlowMenu level[{}]", level);
		
		FlowPanel menuBar = new FlowPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(className, function);
		
		return menuBar;
	}

	@Override
	public void setMenu(int level, String header, String launchHeader, boolean executeTask) {
		final String function = "setMenu";
		
		logger.begin(className, function);
		
		logger.info(className, function, "setMenu level[{}] header[{}] launchHeader[{}] executeTask[{}]", new Object[]{level, header, launchHeader, executeTask});

		cascadeClearMenu(level);
		
		ArrayList<UITaskLaunch> taskLaunchs = navigationMgr.getTasks(level, header);
		
		if ( null != taskLaunchs ) {
			
			logger.info(className, function, "taskLaunchs.size[{}]", taskLaunchs.size());
			
			addTaskToMenu(level, header, taskLaunchs, launchHeader, executeTask);
		} else {
			logger.warn(className, function, "setMenu is null");
		}

		logger.end(className, function);
	}

	private void cascadeClearMenu(int panelLevelToClear) {
		final String function = "cascadeClearMenu";
		
		logger.begin(className, function);
		
		while (menus.containsKey(panelLevelToClear)) {
			ComplexPanel panelToClear = null;
			panelToClear = (ComplexPanel) menus.get(panelLevelToClear);
			panelToClear.clear();
			++panelLevelToClear;

			logger.info(className, function, "Level of Menu Bar to clear: [{}]", panelLevelToClear);
		}
		
		logger.end(className, function);
	}

	private void addTaskToMenu(int level, String header, ArrayList<UITaskLaunch> taskLaunchs, String launchHeader, boolean executeTask) {
		final String function = "addTaskToMenu";
		
		logger.begin(className, function);
		
		logger.info(className, function, "level[{}] header[{}] launchHeader[{}] executeTask[{}]", new Object[]{level, header, launchHeader, executeTask});
		
		logger.info(className, function, "taskLaunchs.size()[{}]", taskLaunchs.size());
		
		ComplexPanel menuBar = (ComplexPanel) this.getMenuBar(level);
		
		if ( null != menuBar ) {
			
			for (int i = 0; i < taskLaunchs.size(); i++) {
				UITaskLaunch taskLaunch = taskLaunchs.get(i);
				String name = taskLaunchs.get(i).getName();
				UIButtonNavigation btnNew = new UIButtonNavigation(name);
				buttons.put(taskLaunch.getHeader(), btnNew);
				btnNew.setTaskLaunch(taskLaunch);
				btnNew.addStyleName("project-gwt-button-navigation-"+level);
				
				btnNew.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						UIButtonNavigation btnSel = (UIButtonNavigation) event.getSource();
						onClickAction(btnSel, null, true);
					}
				});// ClickHandler
				
				menuBar.add(btnNew);
	
			} // for
	
			// Auto select menu item by first expand
			if (menuBar.getWidgetCount() > 0) {
				
				logger.info(className, function, "menuBar.getWidgetCount() > 0 Begin");
				UIButtonNavigation navigationMenuButton = null;
				if ( null != launchHeader ) {
					String headers[] = launchHeader.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
					
					logger.info(className, function, "headers[{}]", headers);
					
	//				int headersCounter = headers.length;
					String headerCur = headers[level];
					
					logger.info(className, function, "level[{}] headerCur[{}]", level, headerCur);
					
					UIButtonNavigation btn = null;
					for ( int i = 0 ; i < menuBar.getWidgetCount(); ++i ) {
						btn = (UIButtonNavigation)menuBar.getWidget(i);
						UITaskLaunch taskLaunch = btn.getTaskLaunch();
						String headerComma = taskLaunch.getHeader();
						String headerCommas[] = headerComma.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
						
						logger.info(className, function, "headerComma[{}] headerCommas[{}]", headerComma, headerCommas);
						logger.info(className, function, "headerCommas[{}][{}] headerCommas[{}]", new Object[]{level, headerCommas[level], headerCommas});
						
						String btnHeaderElement = headerCommas[level]; 
						
						logger.info(className, function, "level[{}] btnHeaderElement[{}] == headerCur[{}]", new Object[]{level, btnHeaderElement, headerCur});
						
						if ( 0 == btnHeaderElement.compareToIgnoreCase(headerCur) ) {
							navigationMenuButton = btn;
							onClickAction(navigationMenuButton, launchHeader, false);
						}
					}
				} else {
					navigationMenuButton = (UIButtonNavigation)menuBar.getWidget(0);
				}
				
				if ( null != navigationMenuButton ) {
					onClickAction(navigationMenuButton, launchHeader, executeTask);
				} else {
					logger.warn(className, function, "menuBar.getWidgetCount() > 0 navigationMenuButton is null");
				}
				
				logger.info(className, function, "menuBar.getWidgetCount() > 0 End");
			} else {
				logger.warn(className, function, "menuBar.getWidgetCount()[{}] <= 0", menuBar.getWidgetCount());
			}
		} else {
			logger.warn(className, function, "menuBar IS NULL");
		}
		
		logger.end(className, function);

	}// addTaskToMenu
	
	private void onClickAction(UIButtonNavigation btnSel, String launchHeader, boolean executeTask) {
		final String function = "onClickAction";
		
		logger.begin(className, function);
		
		logger.info(className, function, "btnSel.getText()[{}] launchHeader[{}] executeTask[{}]", new Object[]{btnSel.getText(), launchHeader, executeTask});

		ComplexPanel parent = (ComplexPanel) btnSel.getParent();
		
		UITaskLaunch task = btnSel.getTaskLaunch();
		int level = btnSel.getTaskLaunch().getTaskLevel();
		int levelNext = level + 1;
		
		cascadeClearMenu(levelNext);
		
		for ( int c = 0 ; c < parent.getWidgetCount(); ++c ) {
			UIButtonNavigation btn = (UIButtonNavigation) parent.getWidget(c);
			if (btn != btnSel) {
				btn.setHightLight(false);
			}
		}
		
		btnSel.setHightLight(true);
		
		logger.info(className, function, "Selected Button Header[{}]", task.getHeader());

		if ( TaskLaunchType.MENU == task.getTaskLaunchType()) {
			
			logger.info(className, function, "is TaskType.MENU");
			
			setMenu(levelNext, task.getHeader(), launchHeader, executeTask);
			
		} else {
			
			logger.info(className, function, "executeTask[{}]", executeTask);
			
			if ( executeTask ) {
			
				task.setTaskUiScreen(this.uiNameCard.getUiScreen());
				
				logger.info(className, function, "Execute Task[{}] on Screen[{}]", task.getHeader(), task.getTaskUiScreen());
				
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(task));
			}
		}
		
		logger.end(className, function);
	}

	public void readyToGetMenu(String profile, String location, int level, String header) {
		final String function = "readyToGetMenu";
		
		logger.begin(className, function);
		
		logger.info(className, function, "profile[{}] location[{}] level[{}] header[{}]", new Object[]{profile, location, level, header});
		
		navigationMgr.initCache(level, header);
		
		logger.end(className, function);
	}
	
	@Override
	public void isReady(int level, String header) {
		final String function = "isReady";
		
		logger.begin(className, function);
		
		setMenu(level, header, null, true);
		
		logger.end(className, function);
	}

	@Override
	public void init() {
		final String function = "init";
		logger.beginEnd(className, function);
	}

}
