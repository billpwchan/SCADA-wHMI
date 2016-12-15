package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIPanelScreen extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelScreen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strCssRoot				= "project-gwt-panel-root";

	private final String strCssRootContainer	= "project-gwt-panel-root-container";
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private final String strNumOfScreen			= "numofscreen";
	
	private final String strUIScreenLogin		= "UIScreenLogin";
	
	private final String strUIScreenLogout		= "UIScreenLogout";
	
	private final String strUIScreenOPM			= "UIScreenOPM";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";
	
	private String entryPointUIWidget = null;
	public void setEntryPointUIWidget(String entryPointUIWidget) {
		this.entryPointUIWidget = entryPointUIWidget;
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		this.rootPanel = new HorizontalPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
    	
    	String operator = "";
    	String profile = "";
    	
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		opmAuthentication.setCurrentOperator(operator);
		opmAuthentication.setCurrentProfile(profile);
		
		UITaskLaunch uiTaskLaunch = new UITaskLaunch();
		uiTaskLaunch.setTaskUiScreen(0);
		uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
		uiTaskLaunch.setUiPanel(entryPointUIWidget);
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));		

    	logger.end(className, function);
	}
	
	private void resetEventBus() {
		final String function = "resetEventBus";
		
		logger.begin(className, function);
		
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
		
		logger.end(className, function);
	}
	
	private void onUIEventDebug(UIEvent uiEvent) {
	}
	
	private void onUIEvent( UIEvent uiEvent ) {
		final String function = "onUIEvent";
		
		logger.begin(className, function);
		
		if ( null != uiEvent ) {

			UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger.info(className, function, "this.uiNameCard.getUiScreen()[{}] == taskProvide.getTaskUiScreen()[{}]", this.uiNameCard.getUiScreen(), taskProvide.getTaskUiScreen());
				
				logger.info(className, function, "this.uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", this.uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if ( this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {

					if ( taskProvide instanceof UITaskLaunch ) {
					
						UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
						
						logger.info(className, function, "switchPanel taskLaunch.getUiPanel()[{}]", taskLaunch.getUiPanel());
	
						rootPanel.clear();
							
						resetEventBus();
						
						if ( strUIScreenLogout.equals(taskLaunch.getUiPanel()) ) {
							
							OpmMgr.getInstance("UIOpmSCADAgen").logout();
							
						} else {
							
							UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
							Panel panel = uiPanelFactoryMgr.getMainPanel(taskLaunch.getUiPanel(), this.uiNameCard);
								
							rootPanel.add(panel);
							
							if ( null != strCssRootContainer ) {
								int screen = 0;
								String cssClassName = strCssRootContainer+"-"+screen;
								logger.info(className, function, "strCssRootContainer[{}] cssClassName[{}]", strCssRootContainer, cssClassName);
								DOM.getParent(panel.getElement()).setClassName(cssClassName);
							}
							
							Settings setting = Settings.getInstance();
							String strNumOfScreen = setting.get(this.strNumOfScreen);
							if ( null != strNumOfScreen ) {
								int intNumOfScreen = 1;
								try {
									intNumOfScreen = Integer.parseInt(strNumOfScreen);
								} catch (NumberFormatException e) {
									logger.warn(className, function, "onUIEvent Number format exception!");
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
										String cssClassName = strCssRootContainer+"-"+screen;
										logger.info(className, function, "strCssRootContainer[{}] cssClassName[{}]", strCssRootContainer, cssClassName);
										DOM.getParent(complexPanelOthers.getElement()).setClassName(cssClassName);
									}
								}
							}
						}
					
					} else {
						logger.warn(className, function, "taskProvide IS UNKNOW");
					}
				}
			} else {
				logger.warn(className, function, "taskProvide IS NULL");
			}
		} else {
			logger.warn(className, function, "uiEvent IS NULL");
		}
		logger.end(className, function);

	}

}
