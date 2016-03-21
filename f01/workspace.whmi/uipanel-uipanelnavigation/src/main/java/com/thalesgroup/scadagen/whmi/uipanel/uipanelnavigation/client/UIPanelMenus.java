package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch.TaskLaunchType;

public class UIPanelMenus implements NavigationMgrEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelMenus.class.getName());
		
	private NavigationMgr navigationMgr = null;

	private HashMap<Integer, Panel> menus = new HashMap<Integer, Panel>();

	private HashMap<String, NavigationMenuButton> buttons = new HashMap<String, NavigationMenuButton>();
	
	public void addMenuBar(int index, Panel menu) {
		
		logger.log(Level.FINE, "addMenuBar Begin");
		
		menus.put(Integer.valueOf(index), menu);
		
		logger.log(Level.FINE, "addMenuBar End");
	}

	public Panel getMenuBar(int index) {
		
		logger.log(Level.FINE, "getMenuBar Begin/End");
		
		return menus.get(Integer.valueOf(index));
	}

	private UINameCard uiNameCard = null;
	public UIPanelMenus(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "UIPanelMenus Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		
		navigationMgr = new NavigationMgr(uiNameCard);
		navigationMgr.setNavigationMgrEvent(this);
		
		logger.log(Level.FINE, "UIPanelMenus End");
			
	}

	public HorizontalPanel getHorizontalMenu(int level) {
		
		logger.log(Level.FINE, "getHorizontalMenu Begin");
		
		HorizontalPanel menuBar = new HorizontalPanel();
		menuBar.setWidth("160px");
//		menuBar.getElement().getStyle().setPadding(10, Unit.PX);
		menuBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(new Integer(level), menuBar);
		
		logger.log(Level.FINE, "getHorizontalMenu End");
		
		return menuBar;
	}

	public VerticalPanel getVerticalMenu(int level) {
		
		logger.log(Level.FINE, "getVerticalMenu Begin");
		
		VerticalPanel menuBar = new VerticalPanel();
		menuBar.setWidth("160px");
		menuBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(new Integer(level), menuBar);
		
		logger.log(Level.FINE, "getVerticalMenu End");
		
		return menuBar;
	}

	public FlowPanel getFlowMenu(int level) {
		
		logger.log(Level.FINE, "setMenu Begin");
		
		FlowPanel menuBar = new FlowPanel();
		menuBar.addStyleName("project-gwt-panel-navigation-"+level);
		addMenuBar(new Integer(level), menuBar);
		
		logger.log(Level.FINE, "getFlowMenu End");
		
		return menuBar;
	}

	@Override
	public void setMenu(int level, String header, String launchHeader, boolean executeTask) {
		
		logger.log(Level.SEVERE, "setMenu Begin");
		
		logger.log(Level.SEVERE, "setMenu level["+level+"] header["+header+"] launchHeader["+launchHeader+"] executeTask["+executeTask+"]");

		cascadeClearMenu(level);
		
		ArrayList<UITaskLaunch> taskLaunchs = navigationMgr.getTasks(level, header);
		
		if ( null != taskLaunchs ) {
			
			logger.log(Level.FINE, "setMenu taskLaunchs.size["+taskLaunchs.size()+"]");
			
			addTaskToMenu(level, header, taskLaunchs, launchHeader, executeTask);
			
		} else {
			
			logger.log(Level.FINE, "setMenu is null");
			
		}

		logger.log(Level.SEVERE, "setMenu End");
	}

	private void cascadeClearMenu(int panelLevelToClear) {
		
		logger.log(Level.FINE, "cascadeClearMenu Begin");
		
		while (menus.containsKey(panelLevelToClear)) {
			ComplexPanel panelToClear = null;
			panelToClear = (ComplexPanel) menus.get(panelLevelToClear);
			panelToClear.clear();
			++panelLevelToClear;

			logger.log(Level.FINE, "cascadeClearMenu Level of Menu Bar to clear:"+panelLevelToClear);
		}
		
		logger.log(Level.FINE, "cascadeClearMenu End");
	}

	private void addTaskToMenu(int level, String header, ArrayList<UITaskLaunch> taskLaunchs, String launchHeader, boolean executeTask) {
		
		logger.log(Level.FINE, "addTaskToMenu Begin");
		
		logger.log(Level.FINE, "addTaskToMenu level["+level+"] header["+header+"] launchHeader["+launchHeader+"] executeTask["+executeTask+"]");
		
		ComplexPanel menuBar = (ComplexPanel) this.getMenuBar(level);
		
		logger.log(Level.FINE, "addTaskToMenu taskLaunchs.size()["+taskLaunchs.size()+"]");

		for (int i = 0; i < taskLaunchs.size(); i++) {
			UITaskLaunch taskLaunch = taskLaunchs.get(i);
//			String name = taskLaunchs.get(i).getNameWithSpace();
			String name = taskLaunchs.get(i).getName();
			NavigationMenuButton btnNew = new NavigationMenuButton(name);
			buttons.put(taskLaunch.getHeader(), btnNew);
			btnNew.setTaskLaunch(taskLaunch);
			btnNew.addStyleName("project-gwt-button-navigation-"+level);
			
			btnNew.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					NavigationMenuButton btnSel = (NavigationMenuButton) event.getSource();
					onClickAction(btnSel, null, true);
				}
			});// ClickHandler

			menuBar.add(btnNew);

		} // for

		// Auto select menu item by first expand
		if (menuBar.getWidgetCount() > 0) {
			
			logger.log(Level.FINE, "addTaskToMenu menuBar.getWidgetCount() > 0 Begin");
			NavigationMenuButton navigationMenuButton = null;
			if ( null != launchHeader ) {
				String headers[] = launchHeader.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
				
				logger.log(Level.FINE, "addTaskToMenu headers["+headers+"]");
				
//				int headersCounter = headers.length;
				String headerCur = headers[level];
				
				logger.log(Level.FINE, "addTaskToMenu level["+level+"] headerCur["+headerCur+"]");
				
				NavigationMenuButton btn = null;
				for ( int i = 0 ; i < menuBar.getWidgetCount(); ++i ) {
					btn = (NavigationMenuButton)menuBar.getWidget(i);
					UITaskLaunch taskLaunch = btn.getTaskLaunch();
					String headerComma = taskLaunch.getHeader();
					String headerCommas[] = headerComma.split("\\"+String.valueOf(UITaskLaunch.getSplite()));
					
					logger.log(Level.FINE, "addTaskToMenu headerComma["+headerComma+"] headerCommas["+headerCommas+"]");
					logger.log(Level.FINE, "addTaskToMenu headerCommas["+level+"]["+headerCommas[level]+"] headerCommas["+headerCommas+"]");
					
					String btnHeaderElement = headerCommas[level]; 
					
					logger.log(Level.FINE, "addTaskToMenu level["+level+"] btnHeaderElement["+btnHeaderElement+"] == headerCur["+headerCur+"]");
					
					if ( 0 == btnHeaderElement.compareToIgnoreCase(headerCur) ) {
						navigationMenuButton = btn;
						onClickAction(navigationMenuButton, launchHeader, false);
					}
				}
			} else {
				navigationMenuButton = (NavigationMenuButton)menuBar.getWidget(0);
			}
			
			if ( null != navigationMenuButton ) {
				onClickAction(navigationMenuButton, launchHeader, executeTask);
			} else {
				logger.log(Level.FINE, "addTaskToMenu menuBar.getWidgetCount() > 0 navigationMenuButton is null");
			}
			
			logger.log(Level.FINE, "addTaskToMenu menuBar.getWidgetCount() > 0 End");
		}
		
		logger.log(Level.FINE, "addTaskToMenu End");

	}// addTaskToMenu
	
	private void onClickAction(NavigationMenuButton btnSel, String launchHeader, boolean executeTask) {
		
		logger.log(Level.SEVERE, "onClickAction Begin");
		
		logger.log(Level.SEVERE, "onClickAction btnSel.getText()["+btnSel.getText()+"] launchHeader["+launchHeader+"] executeTask["+executeTask+"]");

		ComplexPanel parent = (ComplexPanel) btnSel.getParent();
		
		UITaskLaunch task = btnSel.getTaskLaunch();
		int level = btnSel.getTaskLaunch().getTaskLevel();
		int levelNext = level + 1;
		
		cascadeClearMenu(levelNext);
		
		for ( int c = 0 ; c < parent.getWidgetCount(); ++c ) {
			NavigationMenuButton btn = (NavigationMenuButton) parent.getWidget(c);
			if (btn != btnSel) {
				btn.setHightLight(false);
			}
		}
		
		btnSel.setHightLight(true);
		
		logger.log(Level.FINE, "onClickAction Selected Button Header["+task.getHeader()+"]");

		if ( TaskLaunchType.MENU == task.getTaskLaunchType()) {
			
			logger.log(Level.FINE, "onClickAction is TaskType.MENU");
			
			setMenu(levelNext, task.getHeader(), launchHeader, executeTask);
			
		} else {
			
			logger.log(Level.SEVERE, "onClickAction executeTask["+executeTask+"]");
			
			if ( executeTask ) {
			
				task.setTaskUiScreen(this.uiNameCard.getUiScreen());
				
				logger.log(Level.SEVERE, "onClickAction Execute Task["+task.getHeader()+"] on Screen["+task.getTaskUiScreen()+"]");
				
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(task));
			}
		}
		
		logger.log(Level.SEVERE, "onClickAction End");
	}

	public void readyToGetMenu(String profile, String location, int level, String header) {
		
		logger.log(Level.FINE, "getMenu Begin");
		
		logger.log(Level.FINE, "getMenu profile["+profile+"] location["+location+"] level["+level+"] header["+header+"]");
		
		navigationMgr.initCache(level, header);
		
		logger.log(Level.FINE, "getMenu End");
	}
	
	@Override
	public void isReady(int level, String header) {
		
		logger.log(Level.FINE, "isReady Begin");
		
		setMenu(level, header, null, true);
		
		logger.log(Level.FINE, "isReady End");
	}

}
