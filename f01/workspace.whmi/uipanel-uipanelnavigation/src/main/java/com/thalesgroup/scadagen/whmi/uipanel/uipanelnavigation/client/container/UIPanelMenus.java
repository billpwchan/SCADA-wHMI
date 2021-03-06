package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container.widget.UIButtonNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.NavigationMgr;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.NavigationMgrEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelMenus extends UIWidget_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	final static String STR_MENU_NOT_FOUND = "MenuNotFound";
		
	private NavigationMgr navigationMgr = null;

	private Map<Integer, Panel> menus = new HashMap<Integer, Panel>();

	private Map<String, UIButtonNavigation> buttons = new HashMap<String, UIButtonNavigation>();
	
	public void addMenuBar(int index, Panel menu) {
		final String function = "addMenuBar";
		
		logger.begin(function);
		
		Integer iIndex = Integer.valueOf(index);
		logger.debug(function, "addMenuBar index[{}] ==> iIndex[{}]", index, iIndex);
		
		menus.put(iIndex, menu);
		
		logger.end(function);
	}

	public Panel getMenuBar(int index) {
		final String function = "getMenuBar";
		
		logger.begin(function);
		
		Panel panel = menus.get(Integer.valueOf(index));
		
		logger.end(function);
		return panel;
	}

	private UINameCard uiNameCard = null;
	public UIPanelMenus(UINameCard uiNameCard) {
		final String function = "UIPanelMenus";
		
		logger.begin(function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		
		navigationMgr = new NavigationMgr(uiNameCard);
		navigationMgr.setNavigationMgrEvent(new NavigationMgrEvent() {
			
			@Override
			public void isReady(int level, String header) {
				final String function = "isReady";
				
				logger.begin(function);
				
				updateMenu(level, header, null, true);
				
				logger.end(function);
			}
			
			@Override
			public void setMenu(int level, String header, String launchHeader, boolean executeTask) {
				final String function = "setMenu";
				
				logger.begin(function);
				
				logger.debug(function, "setMenu level[{}] header[{}] launchHeader[{}] executeTask[{}]", new Object[]{level, header, launchHeader, executeTask});

				updateMenu(level, header, launchHeader, executeTask);

				logger.end(function);
			}

		});
		
		logger.end(function);
			
	}

	public UIPanelMenus() {
		// TODO Auto-generated constructor stub
	}
	
	public Panel getMenu(String menuLevel, String menuType) {
		final String function = "getMenu";
		
		logger.begin(function);
		logger.debug(function, "level[{}] menuType[{}]", menuLevel, menuType);
		int level = -1;
		try {
			level = Integer.parseInt(menuLevel);
		} catch ( NumberFormatException e) {
			logger.warn(function, "menuLevel[{}] IS INVALID", menuLevel);
			logger.warn(function, "e[{}]", e.toString());
		}
		Panel panel = null;
		
		if ( null != menuType ) {
			if ( 0 == menuType.compareTo("0") ) {
				 // FlowPanel = 0
				 panel = this.getFlowMenu(level);
			} else if ( 0 == menuType.compareTo("1") ) {
				// HorizontalPanel = 1
				panel = this.getHorizontalMenu(level);
			} else if ( 0 == menuType.compareTo("2") ) {
				// VerticalPanel = 2
				panel = this.getVerticalMenu(level);
			} 
		}
		
		if ( null == panel ) {
			panel = this.getFlowMenu(level);
			logger.warn(function, "menuType[{}] IS INVALID", menuType);
			logger.warn(function, "Using Floaw Menu", menuType);
		}
		logger.end(function);
		return panel;
	}

	public HorizontalPanel getHorizontalMenu(int level) {
		final String function = "getHorizontalMenu";
		
		logger.begin(function);
		logger.debug(function, "getHorizontalMenu level[{}]", level);
		
		HorizontalPanel menuBar = new HorizontalPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(function);
		
		return menuBar;
	}

	public VerticalPanel getVerticalMenu(int level) {
		final String function = "getVerticalMenu";
		
		logger.begin(function);
		logger.debug(function, "getVerticalMenu level[{}]", level);
		
		VerticalPanel menuBar = new VerticalPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(function);
		
		return menuBar;
	}

	public FlowPanel getFlowMenu(int level) {
		final String function = "getFlowMenu";
		
		logger.begin(function);
		
		logger.debug(function, "getFlowMenu level[{}]", level);
		
		FlowPanel menuBar = new FlowPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(level, menuBar);
		
		logger.end(function);
		
		return menuBar;
	}

	public void updateMenu(int level, String header, String launchHeader, boolean executeTask) {
		final String function = "updateMenu";
		
		logger.begin(function);
		
		logger.debug(function, "level[{}] header[{}] launchHeader[{}] executeTask[{}]", new Object[]{level, header, launchHeader, executeTask});

		cascadeClearMenu(level);
		
		ArrayList<UITaskLaunch> taskLaunchs = navigationMgr.getTasks(level, header);
		
		if ( null != taskLaunchs ) {
			
			logger.debug(function, "taskLaunchs.size[{}]", taskLaunchs.size());
			
			addTaskToMenu(level, header, taskLaunchs, launchHeader, executeTask);
		} else {
			logger.warn(function, "taskLaunchs is null");
		}

		logger.end(function);
	}

	private void cascadeClearMenu(int panelLevelToClear) {
		final String function = "cascadeClearMenu";
		
		logger.begin(function);
		
		while (menus.containsKey(panelLevelToClear)) {
			ComplexPanel panelToClear = null;
			panelToClear = (ComplexPanel) menus.get(panelLevelToClear);
			panelToClear.clear();
			++panelLevelToClear;

			logger.debug(function, "Level of Menu Bar to clear: [{}]", panelLevelToClear);
		}
		
		logger.end(function);
	}
	
	private void addTaskToMenu(int level, String header, ArrayList<UITaskLaunch> taskLaunchs, String launchHeader, boolean executeTask) {
		final String function = "addTaskToMenu";
		
		logger.begin(function);
		
		logger.debug(function, "level[{}] header[{}] launchHeader[{}] executeTask[{}]", new Object[]{level, header, launchHeader, executeTask});
		
		logger.debug(function, "taskLaunchs.size()[{}]", taskLaunchs.size());
		
		ComplexPanel menuBar = (ComplexPanel) this.getMenuBar(level);
		
		if ( null != menuBar ) {
			
			for (int i = 0; i < taskLaunchs.size(); i++) {
				UITaskLaunch taskLaunch = taskLaunchs.get(i);
				String name = taskLaunchs.get(i).getName();
				
				String enableHTMLName = taskLaunchs.get(i).getEnableHTMLName();
				logger.debug(function, "enableHTMLName[{}]", enableHTMLName);
				if (null != enableHTMLName && 0 == enableHTMLName.compareTo(Boolean.TRUE.toString())) {
					name = UIPanelMenuUtil.backwardConvertXMLTag(name);
				}
				
				String css = taskLaunchs.get(i).getCss();
				String tooltips = taskLaunchs.get(i).getTooltip();
				
				logger.debug(function, "name[{}] css[{}] tooltips[{}]", new Object[]{name, css, tooltips});
				
				UIButtonNavigation btnNew = new UIButtonNavigation(name);
				btnNew.setTitle(tooltips);
				buttons.put(taskLaunch.getHeader(), btnNew);
				btnNew.setTaskLaunch(taskLaunch);
				
				btnNew.addStyleName("project-gwt-button-navigation-"+level);
				
				// If the addidition Css exists add on the widget.
				if ( css != null && css.trim().length() > 0 ) {
					btnNew.addStyleName(css);
				}
				
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
				
				logger.debug(function, "menuBar.getWidgetCount() > 0 Begin");
				UIButtonNavigation navigationMenuButton = null;
				if ( null != launchHeader ) {
					String headers[] = launchHeader.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
					
					logger.debug(function, "headers[{}]", headers);
					
	//				int headersCounter = headers.length;
					String headerCur = headers[level];
					
					logger.debug(function, "level[{}] headerCur[{}]", level, headerCur);
					
					UIButtonNavigation btn = null;
					for ( int i = 0 ; i < menuBar.getWidgetCount(); ++i ) {
						btn = (UIButtonNavigation)menuBar.getWidget(i);
						UITaskLaunch taskLaunch = btn.getTaskLaunch();
						String headerComma = taskLaunch.getHeader();
						String headerCommas[] = headerComma.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
						
						logger.debug(function, "headerComma[{}] headerCommas[{}]", headerComma, headerCommas);
						logger.debug(function, "headerCommas[{}][{}] headerCommas[{}]", new Object[]{level, headerCommas[level], headerCommas});
						
						String btnHeaderElement = headerCommas[level]; 
						
						logger.debug(function, "level[{}] btnHeaderElement[{}] == headerCur[{}]", new Object[]{level, btnHeaderElement, headerCur});
						
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
					logger.warn(function, "menuBar.getWidgetCount() > 0 navigationMenuButton is null");
				}
				
				logger.debug(function, "menuBar.getWidgetCount() > 0 End");
			} else {
				logger.warn(function, "menuBar.getWidgetCount()[{}] <= 0", menuBar.getWidgetCount());
				
				// Call the debug action
				UIButtonNavigation btn = buttons.get(STR_MENU_NOT_FOUND);
				if ( null != btn ) {
					UITaskLaunch taskLaunch = btn.getTaskLaunch();
					if ( null != taskLaunch ) {
						launchTask(taskLaunch);
					} else {
						logger.warn(function, "STR_MENU_NOT_FOUND[{}] taskLaunch is null", STR_MENU_NOT_FOUND);
					}
				} else {
					logger.warn(function, "STR_MENU_NOT_FOUND[{}] btn is null", STR_MENU_NOT_FOUND);
				}
				
			}
		} else {
			logger.warn(function, "menuBar IS NULL");
		}
		
		logger.end(function);

	}// addTaskToMenu
	
	private void onClickAction(UIButtonNavigation btnSel, String launchHeader, boolean executeTask) {
		final String function = "onClickAction";
		logger.begin(function);
		
		logger.debug(function, "btnSel.getText()[{}] launchHeader[{}] executeTask[{}]", new Object[]{btnSel.getText(), launchHeader, executeTask});

		ComplexPanel parent = (ComplexPanel) btnSel.getParent();
		
		UITaskLaunch taskLaunch = btnSel.getTaskLaunch();
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
		
		logger.debug(function, "Selected Button Header[{}]", taskLaunch.getHeader());

		if ( TaskLaunchType.MENU == taskLaunch.getTaskLaunchType()) {
			
			logger.debug(function, "is TaskType.MENU");
			
			updateMenu(levelNext, taskLaunch.getHeader(), launchHeader, executeTask);
			
		} else {
			
			logger.debug(function, "executeTask[{}]", executeTask);
			
			if ( executeTask ) {
			
				launchTask(taskLaunch);
				
			}
		}
		
		logger.end(function);
	}
	
	private void launchTask(UITaskLaunch taskLaunch) {
		final String function = "launchTask";
		logger.begin(function);
	
		if ( taskLaunch.getTaskUiScreen() < 0 ) {
			
			logger.debug(function
					, "Execute taskLaunch.getHeader()[{}] on taskLaunch.getTaskUiScreen()[{}] less then zero, set to current screen[{}]"
					, new Object[]{taskLaunch.getHeader(), taskLaunch.getTaskUiScreen(), this.uiNameCard.getUiScreen()});
			
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
		}
				
		logger.debug(function, "Execute taskLaunch.getHeader()[{}] on taskLaunch.getTaskUiScreen()[{}]", taskLaunch.getHeader(), taskLaunch.getTaskUiScreen());
		
		navigationMgr.launchTask(taskLaunch);
		
		logger.end(function);
	}

	public void readyToGetMenu(String profile, String location, int level, String header) {
		final String function = "readyToGetMenu";
		logger.begin(function);
		
		logger.debug(function, "profile[{}] location[{}] level[{}] header[{}]", new Object[]{profile, location, level, header});
		
		navigationMgr.initCache(level, header);
		
		logger.end(function);
	}

	@Override
	public void init() {
		final String function = "init";
		logger.beginEnd(function);
	}

}
