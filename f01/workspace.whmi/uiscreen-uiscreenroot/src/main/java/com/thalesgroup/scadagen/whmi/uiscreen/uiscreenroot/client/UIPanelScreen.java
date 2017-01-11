package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.HashMap;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
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

public class UIPanelScreen extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelScreen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strCssRoot				= "project-gwt-panel-root";
	
//	private final String strCssRootContainer	= "project-gwt-panel-root-container";

	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private final String strNumOfScreen			= "numofscreen";
	
//	private final String strUIScreenLogin		= "UIScreenLogin";
	
	private final String strUIScreenMMI			= "UIScreenMMI";
	
	private final String strUIScreenLogout		= "UIScreenLogout";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		this.rootPanel = new HorizontalPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
		
//		Settings setting = Settings.getInstance();
//		String strNumOfScreen = setting.get(this.strNumOfScreen);
//		if ( null != strNumOfScreen ) {
//		}
		
		String uiCtrl = "UILayoutEntryPoint";
		
		if ( viewXMLFile.equals("UIScreenMMI.view.xml") ) {
			uiCtrl = "UIScreenMMI";
		}
		
		UITaskLaunch uiTaskLaunch = new UITaskLaunch();
		uiTaskLaunch.setTaskUiScreen(0);
		uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
		uiTaskLaunch.setUiCtrl(uiCtrl);
		uiTaskLaunch.setUiView(viewXMLFile);
		uiTaskLaunch.setUiOpts(optsXMLFile);
		uiTaskLaunch.setUiPanel(viewXMLFile);
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
							
							String uiCtrl = taskLaunch.getUiCtrl();
							String uiView = taskLaunch.getUiView();
							String uiOpts = taskLaunch.getUiOpts();
							String uiDict = taskLaunch.getUiDict();
							String uiElem = taskLaunch.getUiElem();
							HashMap<String, Object> options = new HashMap<String, Object>();
							
							logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

							UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
							UIWidget_i uiWidget_i = uiPanelFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);						
							
							if ( uiWidget_i != null ) {
								
								Panel panel = uiWidget_i.getMainPanel();
								
								rootPanel.add(panel);
								
//								if ( null != strCssRootContainer ) {
//									int screen = 0;
//									String cssClassName = strCssRootContainer+"-"+screen;
//									logger.info(className, function, "strCssRootContainer[{}] cssClassName[{}]", strCssRootContainer, cssClassName);
//									DOM.getParent(panel.getElement()).setClassName(cssClassName);
//								}
								
								Settings setting = Settings.getInstance();
								String strNumOfScreen = setting.get(this.strNumOfScreen);
								if ( null != strNumOfScreen ) {
									int intNumOfScreen = 1;
									try {
										intNumOfScreen = Integer.parseInt(strNumOfScreen);
									} catch (NumberFormatException e) {
										logger.warn(className, function, "onUIEvent Number format exception!");
									}
									if ( ! strUIScreenMMI.equals(taskLaunch.getUiPanel()) ) {
										intNumOfScreen=1;
									}
									Panel panelOthers = null;
									for ( int screen=1;screen<intNumOfScreen;++screen){
										UINameCard uiNameCard = new UINameCard(this.uiNameCard);
										uiNameCard.setUiScreen(screen);
										
										String uiCtrl_s = taskLaunch.getUiPanel();
										String uiView_s = taskLaunch.getUiView();
										String uiOpts_s = taskLaunch.getUiOpts();
										String uiElem_s = taskLaunch.getUiElem();
										String uiDict_s = taskLaunch.getUiDict();
										HashMap<String, Object> options_s = new HashMap<String, Object>();
											
										if ( ! strUIScreenMMI.equals(taskLaunch.getUiPanel())  ) {
											uiCtrl_s = strUIScreenEmpty;
										}
										
										logger.info(className, function, "intNumOfScreen[{}] screen[{}] taskLaunch.getUiPanel()[{}] uiCtrl_s[{}] ", new Object[]{intNumOfScreen, screen, taskLaunch.getUiPanel(), uiCtrl_s});
											
										UIWidget_i uiWidget_i_s = uiPanelFactoryMgr.getUIWidget(uiCtrl_s, uiView_s, uiNameCard, uiOpts_s, uiElem_s, uiDict_s, options_s);
										if ( uiWidget_i_s != null ) {
											panelOthers = uiWidget_i_s.getMainPanel();
										}

										rootPanel.add(panelOthers);
										
//										if ( null != strCssRootContainer ) {
//											String cssClassName = strCssRootContainer+"-"+screen;
//											logger.info(className, function, "strCssRootContainer[{}] cssClassName[{}]", strCssRootContainer, cssClassName);
//											DOM.getParent(panelOthers.getElement()).setClassName(cssClassName);
//										}
									}
								}
							} else {
								logger.warn(className, function, "taskProvide IS UNKNOW");
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
