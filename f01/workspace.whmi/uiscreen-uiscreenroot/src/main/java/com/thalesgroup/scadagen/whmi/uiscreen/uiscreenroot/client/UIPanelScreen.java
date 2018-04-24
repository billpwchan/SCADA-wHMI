package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.loader.LoadByTask;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelScreen extends UIWidget_i {
	
	private final String className_ = this.getClass().getSimpleName();
	private final UILogger logger_ = UILoggerFactory.getInstance().getLogger(this.getClass().getName());

	private final String strCssRoot				= "project-"+className_+"-root";
	private final String strCssContainer		= strCssRoot+"-container-";

	private final String strNumOfScreen			= "numofscreen";
	
	private UIWidget_i uiWidgets [] = null;

	@Override
	public void init() {
		final String function = "init";
		logger_.begin(className_, function);

		this.rootPanel = new FlowPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
		
    	new LoadByTask(className_).init(this.uiNameCard, viewXMLFile, optsXMLFile);

    	logger_.end(className_, function);
	}

	private int getNumOfScreen() {
		final String f = "getNumOfScreen";
		logger_.begin(className_, f);
		
		final String strNumOfScreenValue = UICookies.getCookies(strNumOfScreen);
		logger_.debug(className_, f, "strNumOfScreenValue[{}]", strNumOfScreenValue);
		
		int numOfScreen = 1;
		if ( null != strNumOfScreenValue ) {
			try {
				numOfScreen = Integer.parseInt(strNumOfScreenValue);
			} catch ( NumberFormatException ex ) {
				logger_.warn(className_, f, ""+ex.toString());
			}
		} else {
			logger_.warn(className_, f, "strNumOfScreenValue IS NULL");
		}
		
		logger_.debug(className_, f, "strNumOfScreenValue[{}] numOfScreen[{}]", strNumOfScreenValue, numOfScreen);
		logger_.end(className_, f);
		return numOfScreen;
	}
	
	private void resetEventBus() {
		final String f = "resetEventBus";
		logger_.begin(className_, f);
		
		this.uiNameCard.getUiEventBus().removeHandlers();
		
    	this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler(){
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent( uiEvent );
			}
    	});

		logger_.end(className_, f);
	}

	private void reload(final Map<String, Object> [] params) {
		cleanup();
		setScreens(params);
	}
	
	private void setScreens(final Map<String, Object> [] params) {
		final String f = "loadScreen";
		logger_.begin(className_, f);
		
		final int numOfScreen = params.length;
		
		uiWidgets = new UIWidget_i[numOfScreen];
		for(int screen = 0 ; screen < params.length ; ++screen) {
			
			final Map<String, Object> param = params[screen];
			
			final UINameCard uiNameCard = new UINameCard(this.uiNameCard);
			uiNameCard.setUiScreen(screen);

			final UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
			final UIWidget_i uiWidget_i = uiPanelFactoryMgr.getUIWidget(
					(String) param.get(IUIPanelScreen.Parameters.uiCtrl.toString())
					, (String) param.get(IUIPanelScreen.Parameters.uiView.toString())
					, uiNameCard
					, (String) param.get(IUIPanelScreen.Parameters.uiOpts.toString())
					, (String) param.get(IUIPanelScreen.Parameters.uiElem.toString())
					, (String) param.get(IUIPanelScreen.Parameters.uiDict.toString())
					, new HashMap<String, Object>());						
			uiWidgets[screen] = uiWidget_i;
			
			if ( uiWidget_i != null ) {
				
				final Panel panel = uiWidget_i.getMainPanel();
				
				if ( null != panel ) {
					
					rootPanel.add(panel);
					
					final String cssContainer = strCssContainer+screen;
					logger_.debug(className_, f, "cssClassName[{}]", cssContainer);
					if(null!=DOM.getParent(panel.getElement())) DOM.getParent(panel.getElement()).setClassName(cssContainer);

				} else {
					logger_.warn(className_, f, "uiCtrl[{}] panel IS NULL");
				}
			} else {
				logger_.warn(className_, f, "uiWidget_i IS NULL");
			}
		}
		
		logger_.end(className_, f);
	}
	
	private void cleanup() {
		final String f = "cleanup";
		logger_.begin(className_, f);
		
		if ( null != uiWidgets ) {
			logger_.debug(className_, f, "Clean-up uiWidgets uiWidgets.length[{}]...", uiWidgets.length);
			for ( int i = 0 ; i < uiWidgets.length ; ++i ) {
				logger_.debug(className_, f, "Clean-up uiWidgets[{}] terminate...", i);
				final UIWidget_i uiWidget = uiWidgets[i];
				if ( null != uiWidget ) {
					uiWidget.terminate();
					uiWidgets[i] = null;
				}
			}
		}
		uiWidgets = null;
		
		logger_.debug(className_, f, "Clean-up root Panel...");
		rootPanel.clear();
		
		logger_.debug(className_, f, "Clean-up event bus...");
		resetEventBus();
		
		logger_.end(className_, f);
	}
	
	private void onUIEvent( UIEvent uiEvent ) {
		final String f = "onUIEvent";
		logger_.begin(className_, f);
		
		if ( null != uiEvent ) {

			final UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger_.debug(className_, f, "this.uiNameCard.getUiScreen()[{}] == taskProvide.getTaskUiScreen()[{}]", this.uiNameCard.getUiScreen(), taskProvide.getTaskUiScreen());
				logger_.debug(className_, f, "this.uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", this.uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if ( 
						this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 
						0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {
					
					if ( taskProvide instanceof UITaskLaunch ) {
						final UITaskLaunch task = (UITaskLaunch)taskProvide;
						reload(new LoadByTask(className_).load(task, getNumOfScreen()));
					} else {
						logger_.warn(className_, f, "taskProvide IS UNKNOW");
					}
				}
			} else {
				logger_.warn(className_, f, "taskProvide IS NULL");
			}
		} else {
			logger_.warn(className_, f, "uiEvent IS NULL");
		}
		logger_.end(className_, f);
	}

}
