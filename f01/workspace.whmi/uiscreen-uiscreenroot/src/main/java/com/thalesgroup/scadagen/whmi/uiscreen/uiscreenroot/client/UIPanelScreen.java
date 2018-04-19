package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client.UIScreenMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelScreen extends UIWidget_i {
	
	private final String className_ = UIPanelScreen.class.getSimpleName();
	private UILogger logger_ = UILoggerFactory.getInstance().getLogger(UIPanelScreen.class.getName());
	
	private final String strUIWidgetGeneric = "UIWidgetGeneric";
	private final String strHeader = "header";
	
	private final String strCssRoot				= "project-"+className_+"-root";
	private final String strCssContainer		= strCssRoot+"-container-";

	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private final String strNumOfScreen			= "numofscreen";
	
	private final String strUILayoutEntryPoint	= "UILayoutEntryPoint";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";
	
	private final String strUIScreenMMIPath		= "UIScreenMMI/UIScreenMMI.view.xml";
	
	private final String strUIScreenMMI			= "UIScreenMMI";
	
	private String singleScreenViews = "";
	private String singleScreenCtrls = "";
	
	private UIWidget_i uiWidgets [] = null;

	@Override
	public void init() {
		final String function = "init";
		logger_.begin(className_, function);

		this.rootPanel = new FlowPanel();
		this.rootPanel.addStyleName(strCssRoot);
 
    	resetEventBus();
		
		String uiCtrl = "UILayoutEntryPoint";
		
		if ( viewXMLFile.equals(strUIScreenMMIPath) ) {
			uiCtrl = strUIScreenMMI;
		}
		
		logger_.debug(className_, function, "UIPathUIPanelScreen[{}]", UIPathUIPanelScreen);
		logger_.debug(className_, function, "uiCtrl[{}]", uiCtrl);
		logger_.debug(className_, function, "viewXMLFile[{}]", viewXMLFile);
		logger_.debug(className_, function, "optsXMLFile[{}]", optsXMLFile);
		
		final UITaskLaunch uiTaskLaunch = new UITaskLaunch();
		uiTaskLaunch.setTaskUiScreen(0);
		uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
		uiTaskLaunch.setUiCtrl(uiCtrl);
		uiTaskLaunch.setUiView(viewXMLFile);
		uiTaskLaunch.setUiOpts(optsXMLFile);
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));		

    	logger_.end(className_, function);
	}

	private void loadConfiguration() {
		final String f = "loadConfiguration";
		logger_.begin(className_, f);
		
		final String strOptsXml = className_+"/"+className_+".opts.xml";
		logger_.debug(className_, f, "strOptsXml[{}]", new Object[]{strOptsXml});
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			logger_.debug(className_, f, "SINGLE_SCREEN_VIEWS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strOptsXml);
			singleScreenViews = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strHeader);
			logger_.debug(className_, f, "singleScreenViews[{}]", singleScreenViews);
			
			logger_.debug(className_, f, "SINGLE_SCREEN_CTRLS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.SINGLE_SCREEN_CTRLS.toString(), strOptsXml);
			singleScreenCtrls = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.SINGLE_SCREEN_CTRLS.toString(), strHeader);
			logger_.debug(className_, f, "singleScreenCtrls[{}]", singleScreenCtrls);
		}
		logger_.end(className_, f);
	}
	
	private int getNumOfScreen() {
		final String f = "getNumOfScreen";
		logger_.begin(className_, f);
		
		String strNumOfScreenValue = UICookies.getCookies(strNumOfScreen);
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
	
	private boolean inSingleScreenList(final String items, final String item) {
		final String f = "inSingleScreenList";
		boolean ret = false;
		logger_.debug(className_, f, "items[{}] item[{}]", new Object[]{items, item});
		if(null!=item&&!item.isEmpty()) {
			if (null!=items&&!items.isEmpty()) {
				String [] vs = items.split(",");
				for(int i=0;i<vs.length;++i){
					String v = vs[i];
					logger_.debug(className_, f, "v[{}] view[{}]", v, item);
					if(0==v.compareTo(item)){
						ret = true;
					}
				}
			}
		}
		logger_.debug(className_, f, "items[{}] item[{}] ret[{}]", new Object[]{items, item, ret});
		return ret;
	}
	
	private void loadTask(UITaskLaunch taskProvide) {
		final String f = "loadTask";
		logger_.begin(className_, f);
		
		// Clean-up
		logger_.debug(className_, f, "Clean-up...");
		
		if ( null != uiWidgets ) {
			logger_.debug(className_, f, "Clean-up uiWidgets uiWidgets.length[{}]...", uiWidgets.length);
			for ( int i = 0 ; i < uiWidgets.length ; ++i ) {
				logger_.debug(className_, f, "Clean-up uiWidgets[{}] terminate...", i);
				UIWidget_i uiWidget = uiWidgets[i];
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
		
		// Apply Number of Screen from cookies
		int intNumOfScreen = getNumOfScreen();
		
		// Load Configuration
		loadConfiguration();
		
		logger_.debug(className_, f, "strNumOfScreen[{}] intNumOfScreen[{}]", strNumOfScreen, intNumOfScreen);
		
		final UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
		
		logger_.debug(className_, f, "switchPanel taskLaunch.getUiCtrl()[{}]", taskLaunch.getUiCtrl());
		
		uiWidgets = new UIWidget_i[intNumOfScreen];
		for ( int screen = 0 ; screen < intNumOfScreen ; ++screen ) {
			
			logger_.debug(className_, f, "Prepare screen[{}]", screen);
			
			final UINameCard uiNameCard = new UINameCard(this.uiNameCard);
			uiNameCard.setUiScreen(screen);
		
			String uiCtrl = taskLaunch.getUiCtrl();
			String uiView = taskLaunch.getUiView();
			String uiOpts = taskLaunch.getUiOpts();
			String uiDict = taskLaunch.getUiDict();
			String uiElem = taskLaunch.getUiElem();
			final Map<String, Object> options = new HashMap<String, Object>();
			
			logger_.debug(className_, f, "taskLaunch uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]"
					, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

			if ( 
				screen > 0 
				&& 
				(
					inSingleScreenList(singleScreenCtrls, uiCtrl) 
					|| inSingleScreenList(singleScreenViews, uiView )
				)
			) {
				logger_.debug(className_, f, "uiCtrl[{}] uiView[{}] Is Single Screen, make another panel strUIScreenEmpty[{}]"
						, new Object[]{uiCtrl, uiView, strUIScreenEmpty});
				
				uiCtrl = strUIScreenEmpty;
				uiView = null;
				uiOpts = null;
				uiDict = null;
				uiElem = null;
				
				logger_.debug(className_, f, "uiCtrl[{}]", uiCtrl);
			}

			final UIScreenMgr uiPanelFactoryMgr = UIScreenMgr.getInstance();
			final UIWidget_i uiWidget_i = uiPanelFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);						
			uiWidgets[screen] = uiWidget_i;
			
			if ( uiWidget_i != null ) {
				
				final Panel panel = uiWidget_i.getMainPanel();
				
				if ( null != panel ) {
					
					rootPanel.add(panel);
					
					String cssContainer = strCssContainer+screen;
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
	
	private void onUIEvent( UIEvent uiEvent ) {
		final String f = "onUIEvent";
		logger_.begin(className_, f);
		
		if ( null != uiEvent ) {

			final UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger_.debug(className_, f, "this.uiNameCard.getUiScreen()[{}] == taskProvide.getTaskUiScreen()[{}]", this.uiNameCard.getUiScreen(), taskProvide.getTaskUiScreen());
				
				logger_.debug(className_, f, "this.uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", this.uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if ( this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {

					if ( taskProvide instanceof UITaskLaunch ) {
					
						loadTask((UITaskLaunch)taskProvide);

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
