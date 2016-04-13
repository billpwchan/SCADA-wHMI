package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;

public class UIPanelScreen {
	
	private static Logger logger = Logger.getLogger(UIPanelScreen.class.getName());
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";

	private HorizontalPanel hp;
	
	private UINameCard uiNameCard = null;
	public HorizontalPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin.");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	  
		this.hp = new HorizontalPanel();
    	this.hp.setWidth("100%");
    	this.hp.setHeight("100%");
 
    	resetEventBus();
    	
    	UITaskLaunch taskLaunch = new UITaskLaunch();
    	taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
    	taskLaunch.setUiPath(UIPathUIPanelScreen);
    	taskLaunch.setUiPanel("UIScreenLogin");
    	this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
    	
    	logger.log(Level.FINE, "getMainPanel End");
		
    	return this.hp;
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
	
	private HorizontalPanel onUIEvent( UIEvent uiEvent ) {
		
		logger.log(Level.FINE, "onUIEvent Begin");
		
		if ( null != uiEvent ) {

			UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger.log(Level.FINE, "onUIEvent this.uiNameCard.getUiScreen()["+this.uiNameCard.getUiScreen()+"] == taskProvide.getTaskUiScreen()["+taskProvide.getTaskUiScreen()+"]");
				
				logger.log(Level.FINE, "onUIEvent this.uiNameCard.getUiPath()["+this.uiNameCard.getUiPath()+"] == taskProvide.getUiPath()["+taskProvide.getUiPath()+"]");
				
				if ( this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {

					if ( UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide)) {
					
						UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
						
						logger.log(Level.FINE, "switchPanel taskLaunch.getUiPanel()["+taskLaunch.getUiPanel()+"]");
	
						hp.clear();
							
						resetEventBus();
							
						UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
						ComplexPanel complexPanel = uiPanelFactoryMgr.getMainPanel(taskLaunch.getUiPanel(), this.uiNameCard);
						complexPanel.setWidth("100%");
						complexPanel.setHeight("100%");
							
						hp.add(complexPanel);
						
						Settings setting = Settings.getInstance();
						String strNumOfScreen = setting.get("numofscreen");
						if ( null != strNumOfScreen ) {
							int intNumOfScreen = 1;
							try {
								intNumOfScreen = Integer.parseInt(strNumOfScreen);
							} catch (NumberFormatException e) {
								logger.log(Level.FINE, "onUIEvent Number format exception!");
							}
							if ( 0 == "UIScreenLogin".compareTo(taskLaunch.getUiPanel()) ) {
								intNumOfScreen=1;
							}
							ComplexPanel complexPanelOthers = null;
							for ( int screen=1;screen<intNumOfScreen;++screen){
								UINameCard uiNameCard = new UINameCard(this.uiNameCard);
								uiNameCard.setUiScreen(screen);
								if ( 0 == "UIScreenLogin".compareTo(taskLaunch.getUiPanel()) 
										|| 0 == "UIScreenOPM".compareTo(taskLaunch.getUiPanel()) ) {
									complexPanelOthers = uiPanelFactoryMgr.getMainPanel("UIScreenEmpty", uiNameCard);
								} else {
									complexPanelOthers = uiPanelFactoryMgr.getMainPanel(taskLaunch.getUiPanel(), uiNameCard);
								}
								complexPanelOthers.setWidth("100%");
								complexPanelOthers.setHeight("100%");
								hp.add(complexPanelOthers);
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
		
		return hp;
	}

}
