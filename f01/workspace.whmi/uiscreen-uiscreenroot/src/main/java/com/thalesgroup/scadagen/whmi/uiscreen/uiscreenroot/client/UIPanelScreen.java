package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.HashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelScreen extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelScreen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strCssRoot				= "project-UIPanelScreen-root";
	
	private final String strCssRootContainer	= strCssRoot+"-container-";

	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private final String strNumOfScreen			= "numofscreen";
	
	private final String strUILayoutEntryPoint	= "UILayoutEntryPoint";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";

	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);

		this.rootPanel = new HorizontalPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
		
		String uiCtrl = "UILayoutEntryPoint";
		
		if ( viewXMLFile.equals("UILayoutEntryPointScreenMMISummary/UIScreenMMI.view.xml") ) {
			uiCtrl = "UIScreenMMI";
		}
		
		logger.debug(className, function, "UIPathUIPanelScreen[{}]", UIPathUIPanelScreen);
		logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
		logger.debug(className, function, "viewXMLFile[{}]", viewXMLFile);
		logger.debug(className, function, "optsXMLFile[{}]", optsXMLFile);
		
		UITaskLaunch uiTaskLaunch = new UITaskLaunch();
		uiTaskLaunch.setTaskUiScreen(0);
		uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
		uiTaskLaunch.setUiCtrl(uiCtrl);
		uiTaskLaunch.setUiView(viewXMLFile);
		uiTaskLaunch.setUiOpts(optsXMLFile);
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));		

    	logger.end(className, function);
	}
	
	private int getNumOfScreen() {
		final String function = "resetEventBus";
		logger.begin(className, function);
		
		String strNumOfScreenValue = UICookies.getCookies(strNumOfScreen);
		
		int numOfScreen = 1;
		if ( null != strNumOfScreenValue ) {
			try {
				numOfScreen = Integer.parseInt(strNumOfScreenValue);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, ""+ex.toString());
			}
		} else {
			logger.warn(className, function, "strNumOfScreenValue IS NULL");
		}
		
		logger.debug(className, function, "strNumOfScreenValue[{}] numOfScreen[{}]", strNumOfScreenValue, numOfScreen);
		
		logger.end(className, function);
		
		return numOfScreen;
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

		logger.end(className, function);
	}

	private UIWidget_i uiWidgets [] = null;
	private void loadTask(UITaskLaunch taskProvide) {
		final String function = "loadTask";
		logger.begin(className, function);
		
		// Clean-up
		logger.debug(className, function, "Clean-up...");
		
		if ( null != uiWidgets ) {
			logger.debug(className, function, "Clean-up uiWidgets uiWidgets.length[{}]...", uiWidgets.length);
			for ( int i = 0 ; i < uiWidgets.length ; ++i ) {
				logger.debug(className, function, "Clean-up uiWidgets[{}] terminate...", i);
				UIWidget_i uiWidget = uiWidgets[i];
				if ( null != uiWidget ) {
					uiWidget.terminate();
					uiWidgets[i] = null;
				}
			}
		}
		uiWidgets = null;
		
		logger.debug(className, function, "Clean-up root Panel...");
		rootPanel.clear();
		
		logger.debug(className, function, "Clean-up event bus...");
		resetEventBus();
		
		// Apply Number of Screen from cookies
		int intNumOfScreen = getNumOfScreen();
		
		logger.debug(className, function, "strNumOfScreen[{}] intNumOfScreen[{}]", strNumOfScreen, intNumOfScreen);
		
		UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
		
		logger.debug(className, function, "switchPanel taskLaunch.getUiCtrl()[{}]", taskLaunch.getUiCtrl());
		
		uiWidgets = new UIWidget_i[intNumOfScreen];
		for ( int screen = 0 ; screen < intNumOfScreen ; ++screen ) {
			
			logger.debug(className, function, "Prepare screen[{}]", screen);
			
			UINameCard uiNameCard = new UINameCard(this.uiNameCard);
			uiNameCard.setUiScreen(screen);
		
			String uiCtrl = taskLaunch.getUiCtrl();
			String uiView = taskLaunch.getUiView();
			String uiOpts = taskLaunch.getUiOpts();
			String uiDict = taskLaunch.getUiDict();
			String uiElem = taskLaunch.getUiElem();
			HashMap<String, Object> options = new HashMap<String, Object>();
			
			logger.debug(className, function, "taskLaunch data uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

			if ( screen > 0 && ( null != uiCtrl && uiCtrl.equals(strUILayoutEntryPoint) ) ) {
				
				logger.debug(className, function, "uiCtrl[{}] IS strUILayoutEntryPoint[{}], make another panel strUIScreenEmpty[{}]", new Object[]{uiCtrl, strUILayoutEntryPoint, strUIScreenEmpty});
				
				uiCtrl = strUIScreenEmpty;
				uiView = null;
				uiOpts = null;
				uiDict = null;
				uiElem = null;
				
				logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
			}

			UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
			UIWidget_i uiWidget_i = uiPanelFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);						
			uiWidgets[screen] = uiWidget_i;
			
			if ( uiWidget_i != null ) {
				
				Panel panel = uiWidget_i.getMainPanel();
				
				if ( null != panel ) {
					
					rootPanel.add(panel);
					
					if ( null != strCssRootContainer ) {
					
						String cssClassName = strCssRootContainer+screen;
						logger.debug(className, function, "strCssRootContainer[{}] cssClassName[{}]", strCssRootContainer, cssClassName);
						DOM.getParent(panel.getElement()).setClassName(cssClassName);
					}
				} else {
					logger.warn(className, function, "uiCtrl[{}] panel IS NULL");
				}
			} else {
				logger.warn(className, function, "uiWidget_i IS NULL");
			}
		}
		
		logger.end(className, function);
	}
	
	private void onUIEvent( UIEvent uiEvent ) {
		final String function = "onUIEvent";
		logger.begin(className, function);
		
		if ( null != uiEvent ) {

			UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger.debug(className, function, "this.uiNameCard.getUiScreen()[{}] == taskProvide.getTaskUiScreen()[{}]", this.uiNameCard.getUiScreen(), taskProvide.getTaskUiScreen());
				
				logger.debug(className, function, "this.uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", this.uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if ( this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {

					if ( taskProvide instanceof UITaskLaunch ) {
					
						loadTask((UITaskLaunch)taskProvide);

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
