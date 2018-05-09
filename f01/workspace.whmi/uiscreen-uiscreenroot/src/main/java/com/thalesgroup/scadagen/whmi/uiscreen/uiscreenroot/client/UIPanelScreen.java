package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
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
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());

	private final String strCssRoot				= "project-"+className+"-root";
	private final String strCssContainer		= strCssRoot+"-container-";

	private final String strUIWidgetGeneric		= "UIWidgetGeneric";
	private final String strHeader				= "header";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";
	
	private String singleScreenViews			= "";
	private String terminateTimerViews			= "";
	private int delayMillis						= 250;
	
	private final String strNumOfScreen			= "numofscreen";
	
	private UIWidget_i uiWidgets [] = null;

	@Override
	public void init() {
		final String f = "init";
		logger.begin(className, f);
		
		final String strOptsXml = className+"/"+className+".opts.xml";
		logger.debug(className, f, "strOptsXml[{}]", new Object[]{strOptsXml});
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			logger.debug(className, f, "SINGLE_SCREEN_VIEWS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strOptsXml);
			singleScreenViews = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strHeader);
			logger.debug(className, f, "singleScreenViews[{}]", singleScreenViews);
			
			logger.debug(className, f, "TERMINATE_TIMER_VIEWS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.TERMINATE_TIMER_VIEWS.toString(), strOptsXml);
			terminateTimerViews = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.TERMINATE_TIMER_VIEWS.toString(), strHeader);
			logger.debug(className, f, "terminateTimerViews[{}]", terminateTimerViews);
			
			String strDelayMillis		= dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.DelayMillis.toString(), strHeader);
			
			if(null!=strDelayMillis){
				logger.debug(className, f, "strDelayMillis[{}]", strDelayMillis);
				try{
					delayMillis = Integer.parseInt(strDelayMillis);	
				} catch (NumberFormatException ex) {
					logger.warn(className, f, ex.toString());
				}
			} else {
				logger.warn(className, f
						, "IUIPanelScreen.ParameterName.DelayMillis.toString()[{}] is configuration is missing, using default[{}]"
						, IUIPanelScreen.ParameterName.DelayMillis.toString(), delayMillis);
			}
		}

		this.rootPanel = new FlowPanel();
		this.rootPanel.addStyleName(strCssRoot);

		loadDefault();

    	logger.end(className, f);
	}
	
	private void loadDefault() {
		final String f = "loadDefault";
		logger.begin(className, f);
		
		final String uiPath					= ":UIGws:UIPanelScreen";
		
		final String strUIScreenMMIPath		= "UIScreenMMI/UIScreenMMI.view.xml";
		
		final String strUIScreenMMI			= "UIScreenMMI";
	
		final int numOfScreen				= 1;
		
		String uiCtrl = "UILayoutEntryPoint";
		
		if ( viewXMLFile.equals(strUIScreenMMIPath) ) {
			uiCtrl = strUIScreenMMI;
		}
		
		logger.debug(className, f, "uiPath[{}] uiCtrl[{}] viewXMLFile[{}] optsXMLFile[{}]", new Object[]{uiPath, uiCtrl, viewXMLFile, optsXMLFile});

		Map<String, Object>[] params = new Map[numOfScreen];
		
		final Map<String, Object> param = new HashMap<String, Object>();
		param.put(IUIPanelScreen.Parameters.uiCtrl.toString(), uiCtrl);
		param.put(IUIPanelScreen.Parameters.uiView.toString(), viewXMLFile);
		param.put(IUIPanelScreen.Parameters.uiOpts.toString(), optsXMLFile);
//		param.put(IUIPanelScreen.Parameters.uiDict.toString(), uiDict);
//		param.put(IUIPanelScreen.Parameters.uiElem.toString(), uiElem);
		
		params[0] = param;
		
		reloadPanels(params);
		
		logger.end(className, f);
	}

	private boolean inList(final String items, final String item) {
		final String f = "inList";
		boolean ret = false;
		logger.debug(className, f, "items[{}] item[{}]", new Object[]{items, item});
		if(null!=item&&!item.isEmpty()) {
			if (null!=items&&!items.isEmpty()) {
				String [] vs = items.split(",");
				for(int i=0;i<vs.length;++i){
					String v = vs[i];
					logger.debug(className, f, "v[{}] view[{}]", v, item);
					if(0==v.compareTo(item)){
						ret = true;
					}
				}
			}
		}
		logger.debug(className, f, "items[{}] item[{}] ret[{}]", new Object[]{items, item, ret});
		return ret;
	}
	
	public Map<String, Object>[] load(final UITaskLaunch task, final int numOfScreen) {
		final String f = "load";
		logger.begin(className, f);
	
		logger.debug(className, f, "Prepare numOfScreen[{}]", numOfScreen);
		
		Map<String, Object>[] params = null;
		
		if(null!=task) {
			params = new Map[numOfScreen];
			
			logger.debug(className, f, "task.getUiCtrl()[{}]", task.getUiCtrl());
			
			for ( int screen = 0 ; screen < numOfScreen ; ++screen ) {
				
				logger.debug(className, f, "Prepare screen[{}]", screen);
				
				String uiCtrl = task.getUiCtrl();
				String uiView = task.getUiView();
				String uiOpts = task.getUiOpts();
				String uiDict = task.getUiDict();
				String uiElem = task.getUiElem();
				
				logger.debug(className, f, "task uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]"
						, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});
				
				boolean isSingleViews = inList(singleScreenViews, uiView);
				
				logger.debug(className, f, "task screen[{}] isSingleViews[{}]"
						, new Object[]{screen, isSingleViews});
				
				if ( 
					screen > 0 && (isSingleViews)
				) {
					logger.debug(className, f, "uiCtrl[{}] uiView[{}] Is Single Screen, make another panel strUIScreenEmpty[{}]"
							, new Object[]{uiCtrl, uiView, strUIScreenEmpty});
					
					uiCtrl = strUIScreenEmpty;
					uiView = null;
					uiOpts = null;
					uiDict = null;
					uiElem = null;
					
					logger.debug(className, f, "uiCtrl[{}]", uiCtrl);
				}
				
				final Map<String, Object> param = new HashMap<String, Object>();
				param.put(IUIPanelScreen.Parameters.uiCtrl.toString(), uiCtrl);
				param.put(IUIPanelScreen.Parameters.uiView.toString(), uiView);
				param.put(IUIPanelScreen.Parameters.uiOpts.toString(), uiOpts);
				param.put(IUIPanelScreen.Parameters.uiDict.toString(), uiDict);
				param.put(IUIPanelScreen.Parameters.uiElem.toString(), uiElem);
				
				params[screen] = param;
			}
		}
		logger.end(className, f);
		return params;
	}
	
	private int getNumOfScreen() {
		final String f = "getNumOfScreen";
		logger.begin(className, f);
		
		final String strNumOfScreenValue = UICookies.getCookies(strNumOfScreen);
		logger.debug(className, f, "strNumOfScreenValue[{}]", strNumOfScreenValue);
		
		int numOfScreen = 1;
		if ( null != strNumOfScreenValue ) {
			try {
				numOfScreen = Integer.parseInt(strNumOfScreenValue);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, f, ""+ex.toString());
			}
		} else {
			logger.warn(className, f, "strNumOfScreenValue IS NULL");
		}
		
		logger.debug(className, f, "strNumOfScreenValue[{}] numOfScreen[{}]", strNumOfScreenValue, numOfScreen);
		logger.end(className, f);
		return numOfScreen;
	}

	private void terminatePanels() {
		final String f = "terminatePanels";
		logger.begin(className, f);
		
		if ( null != uiWidgets ) {
			logger.debug(className, f, "Clean-up uiWidgets uiWidgets.length[{}]...", uiWidgets.length);
			for ( int i = 0 ; i < uiWidgets.length ; ++i ) {
				logger.debug(className, f, "Clean-up uiWidgets({}) terminate...", i);
				UIWidget_i uiWidget = uiWidgets[i];
				logger.debug(className, f, "Clean-up uiWidgets[{}] terminate...", uiWidget);
				if ( null != uiWidget ) {
					uiWidget.terminate();
					uiWidgets[i] = null;
				}
			}
		}
		uiWidgets = null;

		logger.end(className, f);
	}
	
	private void cleanPanels() {
		final String f = "cleanPanels";
		logger.begin(className, f);

		logger.debug(className, f, "Clean-up root Panel...");
		rootPanel.clear();

		logger.end(className, f);
	}
	
	private void resetEventBus() {
		final String f = "resetEventBus";
		logger.begin(className, f);
		
		this.uiNameCard.getUiEventBus().removeHandlers();
		
    	this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler(){
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent( uiEvent );
			}
    	});

		logger.end(className, f);
	}
	
	private void applyPanels(final Map<String, Object> [] params) {
		final String f = "applyPanels";
		logger.begin(className, f);
		
		int numOfScreen = params.length;
		logger.debug(className, f, "numOfScreen[{}]", new Object[]{numOfScreen});
		
		uiWidgets = null;
		uiWidgets = new UIWidget_i[numOfScreen];
		for(int screen = 0 ; screen < params.length ; ++screen) {
			
			logger.debug(className, f, "numOfScreen[{}] screen[{}]", new Object[]{numOfScreen, screen});
			
			uiWidgets[screen] = null;
			
			final Map<String, Object> param = params[screen];
			
			final String uiCtrl = null != param.get(IUIPanelScreen.Parameters.uiCtrl.toString()) ? (String) param.get(IUIPanelScreen.Parameters.uiCtrl.toString()) : null;
			final String uiView = null != param.get(IUIPanelScreen.Parameters.uiView.toString()) ? (String) param.get(IUIPanelScreen.Parameters.uiView.toString()) : null;
			final String uiOpts = null != param.get(IUIPanelScreen.Parameters.uiOpts.toString()) ? (String) param.get(IUIPanelScreen.Parameters.uiOpts.toString()) : null;
			final String uiDict = null != param.get(IUIPanelScreen.Parameters.uiDict.toString()) ? (String) param.get(IUIPanelScreen.Parameters.uiDict.toString()) : null;
			final String uiElem = null != param.get(IUIPanelScreen.Parameters.uiElem.toString()) ? (String) param.get(IUIPanelScreen.Parameters.uiElem.toString()) : null;

			logger.debug(className, f, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}] uiElem[{}]"
					, new Object[]{
							uiCtrl
							, uiView
							, uiOpts
							, uiDict
							, uiElem
							});
			
			UINameCard uiNameCard = new UINameCard(this.uiNameCard);
			uiNameCard.setUiScreen(screen);

			UIWidget_i uiWidget_i = UIScreenMgr.getInstance().getUIWidget(
					uiCtrl
					, uiView
					, uiNameCard
					, uiOpts
					, uiElem
					, uiDict
					, new HashMap<String, Object>());						
			uiWidgets[screen] = uiWidget_i;
			
			if ( uiWidget_i != null ) {
				
				Panel panel = uiWidget_i.getMainPanel();
				
				if ( null != panel ) {
					
					rootPanel.add(panel);
					
					final String cssContainer = strCssContainer+screen;
					logger.debug(className, f, "cssClassName[{}]", cssContainer);
					if(null!=DOM.getParent(panel.getElement())) DOM.getParent(panel.getElement()).setClassName(cssContainer);

					if(0==screen) {
						viewXMLFile = uiCtrl;
						optsXMLFile = uiOpts;
							
						logger.debug(className, f, "viewXMLFile[{}] optsXMLFile[{}]", viewXMLFile, optsXMLFile);
					}

				} else {
					logger.warn(className, f, "uiCtrl[{}] panel IS NULL");
				}
			} else {
				logger.warn(className, f, "uiWidget_i IS NULL");
			}
		}
		
		logger.end(className, f);
	}
	
	private void reloadPanels(final Map<String, Object> [] params) {
		final String f = "reloadPanels";
		logger.begin(className, f);

//		boolean isTerminateTimerViews = false;
//		if(null != uiWidgets && uiWidgets.length > 0) {
//			UIWidget_i uiWidget = uiWidgets[0];
//			if(null != uiWidget) {
//				final String uiView = uiWidget.getViewXMLFile();
//				logger.debug(className, f, "uiView[{}]", uiView);
//				if(null!=uiView) {
//					logger.debug(className, f, "isTerminateTimerViews[{}] uiView[{}]", isTerminateTimerViews, uiView);
//					isTerminateTimerViews = inList(terminateTimerViews, uiView);
//				}
//			}
//		}
		boolean isTerminateTimerViews = true;
		
		logger.debug(className, f, "isTerminateTimerViews[{}]", isTerminateTimerViews);
		
		if(isTerminateTimerViews) {
			logger.debug(className, f, "Timer applied for Clean-up and apply...");
			
			logger.debug(className, f, "Clean-up widgets and panel...");
			terminatePanels();
			
			logger.debug(className, f, "Run Clean-up, apply widgets and panel...");
			new Timer() {
				
				@Override
				public void run() {
					logger.debug(className, f, "Run Clean-up, apply widgets and panel...");
			
					logger.debug(className, f, "Clean-up widgets and panel...");
					cleanPanels();

					logger.debug(className, f, "Reset Event bus...");
					resetEventBus();
					
					logger.debug(className, f, "Apply Panel to screen...");
					applyPanels(params);
				}
			}.schedule(delayMillis);
		} else {
			logger.debug(className, f, "Timer didn't applied for Clean-up and apply...");
			
			logger.debug(className, f, "Run Clean-up, apply widgets and panel...");
			
			logger.debug(className, f, "Clean-up widgets and panel...");
			terminatePanels();
			
			logger.debug(className, f, "Clean-up widgets and panel...");
			cleanPanels();

			logger.debug(className, f, "Reset Event bus...");
			resetEventBus();
			
			logger.debug(className, f, "Apply Panel to screen...");
			applyPanels(params);
		}
		
		logger.end(className, f);
	}

	private void onUIEvent( UIEvent uiEvent ) {
		final String f = "onUIEvent";
		logger.begin(className, f);
		
		if ( null != uiEvent ) {

			UITask_i taskProvide = uiEvent.getTaskProvide();

			if ( null != taskProvide ) {
				
				logger.debug(className, f, "this.uiNameCard.getUiPath()[{}] == taskProvide.getUiPath()[{}]", this.uiNameCard.getUiPath(), taskProvide.getUiPath());
				
				if ( 
						this.uiNameCard.getUiScreen() == taskProvide.getTaskUiScreen()
						&& 
						0 == this.uiNameCard.getUiPath().compareToIgnoreCase(taskProvide.getUiPath()) ) {
					
					if ( taskProvide instanceof UITaskLaunch ) {
						reloadPanels(load((UITaskLaunch)taskProvide, getNumOfScreen()));
					} else {
						logger.warn(className, f, "taskProvide IS UNKNOW");
					}
				}
			} else {
				logger.warn(className, f, "taskProvide IS NULL");
			}
		} else {
			logger.warn(className, f, "uiEvent IS NULL");
		}
		logger.end(className, f);
	}

}
