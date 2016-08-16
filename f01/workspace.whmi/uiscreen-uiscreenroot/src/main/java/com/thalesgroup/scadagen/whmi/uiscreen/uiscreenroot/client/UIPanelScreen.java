package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelScreen extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(UIPanelScreen.class.getName());
	
	private final String strCssRoot				= "project-gwt-panel-root";

	private final String strCssRootContainer	= "project-gwt-panel-root-container";
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private final String strNumOfScreen			= "numofscreen";
	
	private final String strUIScreenLogin		= "UIScreenLogin";
	
	private final String strUIScreenOPM			= "UIScreenOPM";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";

	@Override
	public void init() {
		logger.log(Level.FINE, "init Begin.");

		this.rootPanel = new HorizontalPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
    	
    	UITaskLaunch taskLaunch = new UITaskLaunch();
    	taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
    	taskLaunch.setUiPath(UIPathUIPanelScreen);
    	taskLaunch.setUiPanel(strUIScreenLogin);
    	this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
    	
    	logger.log(Level.FINE, "init End");
	}
	
	private void resetEventBus() {
		
		logger.log(Level.FINE, "resetEventBus Begin");
		
		this.uiNameCard.getUiEventBus().removeHandlers();
		
    	this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler(){
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent( uiEvent );
			}
    	});
    	
		// Debug Usage
		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent event) {
				onUIEventDebug(event);
			}
		});
		
		logger.log(Level.FINE, "resetEventBus End");
	}
	
	private void onUIEventDebug(UIEvent uiEvent) {
		
		logger.log(Level.FINE, "onUIEventDebug Begin");
		
		logger.log(Level.FINE, "onUIEventDebug End");
	}
	
	private void onUIEvent( UIEvent uiEvent ) {
		
		logger.log(Level.FINE, "onUIEvent Begin");
		
		if ( null != uiEvent ) {

			UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger.log(Level.FINE, "onUIEvent this.uiNameCard.getUiScreen()["+this.uiNameCard.getUiScreen()+"] == taskProvide.getTaskUiScreen()["+taskProvide.getTaskUiScreen()+"]");
				
				logger.log(Level.FINE, "onUIEvent this.uiNameCard.getUiPath()["+this.uiNameCard.getUiPath()+"] == taskProvide.getUiPath()["+taskProvide.getUiPath()+"]");
				
				if ( this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {

					if ( taskProvide instanceof UITaskLaunch ) {
					
						UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
						
						logger.log(Level.FINE, "switchPanel taskLaunch.getUiPanel()["+taskLaunch.getUiPanel()+"]");
	
						rootPanel.clear();
							
						resetEventBus();
							
						UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
						Panel panel = uiPanelFactoryMgr.getMainPanel(taskLaunch.getUiPanel(), this.uiNameCard);
							
						rootPanel.add(panel);
						
						if ( null != strCssRootContainer ) {
							Element container = DOM.getParent(panel.getElement());
							container.setClassName(strCssRootContainer + "-0");
						}
						
						Settings setting = Settings.getInstance();
						String strNumOfScreen = setting.get(this.strNumOfScreen);
						if ( null != strNumOfScreen ) {
							int intNumOfScreen = 1;
							try {
								intNumOfScreen = Integer.parseInt(strNumOfScreen);
							} catch (NumberFormatException e) {
								logger.log(Level.FINE, "onUIEvent Number format exception!");
							}
							if ( 0 == strUIScreenLogin.compareTo(taskLaunch.getUiPanel()) ) {
								intNumOfScreen=1;
							}
							Panel complexPanelOthers = null;
							for ( int screen=1;screen<intNumOfScreen;++screen){
								UINameCard uiNameCard = new UINameCard(this.uiNameCard);
								uiNameCard.setUiScreen(screen);
								if ( 0 == strUIScreenLogin.compareTo(taskLaunch.getUiPanel()) 
										|| 0 == strUIScreenOPM.compareTo(taskLaunch.getUiPanel()) ) {
									complexPanelOthers = uiPanelFactoryMgr.getMainPanel(strUIScreenEmpty, uiNameCard);
								} else {
									complexPanelOthers = uiPanelFactoryMgr.getMainPanel(taskLaunch.getUiPanel(), uiNameCard);
								}
								rootPanel.add(complexPanelOthers);
								
								if ( null != strCssRootContainer ) {
									Element container = DOM.getParent(complexPanelOthers.getElement());
									container.setClassName(strCssRootContainer+"-"+strNumOfScreen);
								}
							}
						}
					
					} else {
						logger.log(Level.FINE, "onUIEvent UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide) IS NOT");
					}
				
				} else {
					logger.log(Level.FINE, "onUIEvent UIScreen and UIPath IS NOT EQUAL");
				}
			} else {
				logger.log(Level.FINE, "onUIEvent taskProvide IS NULL");
			}
		} else {
			logger.log(Level.FINE, "onUIEvent uiEvent IS NULL");
		}
		logger.log(Level.FINE, "onUIEvent End");

	}

}
