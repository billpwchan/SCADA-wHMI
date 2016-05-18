package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCacheEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIPanelScreen;

public class UIGws {

	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private static Logger logger = Logger.getLogger("");

	private SplitLayoutPanel root = null;
	private HorizontalPanel main = null;

	public SplitLayoutPanel getMainPanel() {
		this.root = new SplitLayoutPanel();
		this.main = new HorizontalPanel();
		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		initCache();
		initCaches();

		return getMainPanel(new UINameCard(0, "", RESETABLE_EVENT_BUS));
	}

	private UINameCard uiNameCard = null;
	public SplitLayoutPanel getMainPanel(UINameCard uiNameCard) { 

		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		/**
		 * Configuration.
		 */
		// start of parameter override
		Settings setting = Settings.getInstance();
		Map<String, List<String>> paramsMap = Window.Location.getParameterMap();
		for ( String key: paramsMap.keySet() ) {
			List<String> values = paramsMap.get(key);
			if ( values.size() > 0 ) {
				String value = values.get(0);
				setting.set(key.toLowerCase(), value);
			}
		}
		// end of parameter override
		
		String strLogModule = setting.get("logmodule");
		String strLogModuleLevel = setting.get("logmodulelevel");
		
		if ( null != strLogModule && null != strLogModuleLevel ) {
			Level level = Level.FINE;
			if ( 0 == strLogModule.compareToIgnoreCase("ALL") ) {
				level = Level.ALL;
			} else if ( 0 == strLogModule.compareToIgnoreCase("FINE") ) {
				level = Level.FINE;
			} else if ( 0 == strLogModule.compareToIgnoreCase("INFO") ) {
				level = Level.INFO;
			} else if ( 0 == strLogModule.compareToIgnoreCase("CONFIG") ) {
				level = Level.CONFIG;
			} else if ( 0 == strLogModule.compareToIgnoreCase("WARNING") ) {
				level = Level.WARNING;
			} else if ( 0 == strLogModule.compareToIgnoreCase("SEVERE") ) {
				level = Level.SEVERE;
			}
			
			String strLogModules [] = strLogModule.split("\\|");
			for ( String s: strLogModules ) {
				Logger.getLogger(s).setLevel(level);
			}
		}
		
		
		// Num Of Screen
		String strNumOfScreen = setting.get("numofscreen");
		
		if ( null == strNumOfScreen ) {
			setting.set("numofscreen", Integer.toString(1));
		} else {
			boolean valid = false;
			try {
				int numOfScreen = Integer.parseInt(strNumOfScreen);
				if ( numOfScreen >= 1 && numOfScreen <= 3 ) {
					valid = true;
				}
			} catch ( NumberFormatException e) {
				logger.log(Level.SEVERE, "getMainPanel NumberFormatException e["+e.toString()+"]");
			}
			if ( ! valid ) {
				setting.set("numofscreen", Integer.toString(1));
			}
		}
		
		this.root = new SplitLayoutPanel();
		
		String strDebug = setting.get("debug");

		if ( null != strDebug && 0==strDebug.compareToIgnoreCase("true") && LogConfiguration.loggingIsEnabled() ) {
			int hight = 200;
			VerticalPanel logArea = new VerticalPanel();
			logArea.addStyleName("project-gwt-panel-gws-logarea");
			ScrollPanel scrollPanel = new ScrollPanel(logArea);
			scrollPanel.addStyleName("project-gwt-panel-gws-logarea-scrollpanel");
			this.root.addSouth(scrollPanel, hight);
			logger.addHandler(new HasWidgetsLogHandler(logArea));
		}
		
		this.root.add(main);
		this.main.addStyleName("project-gwt-panel-gws-main");
		this.main.add(new UIGwsCache().getMainPanel(this.uiNameCard));

		// Debug
		HashMap<String, String> hashMap = setting.getMaps();
		for ( Map.Entry<String, String> entry : hashMap.entrySet() ) {
			logger.log(Level.SEVERE, "getMainPanel Debug key["+entry.getKey()+"] value["+entry.getValue()+"]");
		}
		// End of Debug

		logger.log(Level.FINE, "getMainPanel End");
				
		return root;
	}
	
	public void initCaches () {
		
		logger.log(Level.FINE, "initCaches Begin");
		
		{
			DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIWidgetGeneric");
			
			String module = null;
			
			String folder = "UIPanelGeneric";
			
			String extention = ".xml";
			
			dictionariesCache.add(folder, extention);
			
			dictionariesCache.init(module, new DictionariesCacheEvent() {
				
				@Override
				public void dictionariesCacheEventReady(int received) {
					logger.log(Level.SEVERE, "dictionariesCacheEventReady");
					
//					ready(received);
					
				}
			});
		}
		
		{
			DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UICaches");
			
			String header = "header";
			String option = "option";
			
			//Login Panel
			uiPanelSettingCache.add("hvid_alias.xml", header);
			uiPanelSettingCache.add("hvid_alias.xml", option);
			
			String module = null;
			
			uiPanelSettingCache.init(module, "UICaches", new DictionaryCacheEvent() {
				
				@Override
				public void dictionaryCacheEventReady(int received) {

					logger.log(Level.SEVERE, "dictionaryCacheEventReady");
					
				}
			});
		}
		
		logger.log(Level.FINE, "initCaches End");
	}
	
	public void initCache () {
		
		logger.log(Level.FINE, "initCache Begin");
		
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
		
		String header = "header";
		String option = "option";
		
		//Login Panel
		uiPanelSettingCache.add("UIPanelLogin.xml", header);
		uiPanelSettingCache.add("UIPanelLogin.xml", option);
		
		uiPanelSettingCache.add("UIPanelLoginLogo.xml", header);
		uiPanelSettingCache.add("UIPanelLoginLogo.xml", option);
		
		uiPanelSettingCache.add("UIPanelLoginInfo.xml", header);
		uiPanelSettingCache.add("UIPanelLoginInfo.xml", option);
		
		uiPanelSettingCache.add("UIPanelLoginButton.xml", header);
		uiPanelSettingCache.add("UIPanelLoginButton.xml", option);
		
		//Alarm Banner
		uiPanelSettingCache.add("UIPanelAlarmBanner.xml", header);
		uiPanelSettingCache.add("UIPanelAlarmBanner.xml", option);		

		uiPanelSettingCache.add("UIPanelAlarmBannerButton.xml", header);
		uiPanelSettingCache.add("UIPanelAlarmBannerButton.xml", option);		
		
		uiPanelSettingCache.add("UIPanelOlsCounter.xml", header);
		uiPanelSettingCache.add("UIPanelOlsCounter.xml", option);
		
		//Static Bar
		uiPanelSettingCache.add("UIPanelStatusBar.xml", header);
		uiPanelSettingCache.add("UIPanelStatusBar.xml", option);		
		
		uiPanelSettingCache.add("UIPanelCompany.xml", header);
		uiPanelSettingCache.add("UIPanelCompany.xml", option);
		
		uiPanelSettingCache.add("UIPanelCompanyTitle.xml", header);
		uiPanelSettingCache.add("UIPanelCompanyTitle.xml", option);
		
		uiPanelSettingCache.add("UIPanelTitle.xml", header);
		uiPanelSettingCache.add("UIPanelTitle.xml", option);
		
		uiPanelSettingCache.add("UIPanelOperatorProfile.xml", header);
		uiPanelSettingCache.add("UIPanelOperatorProfile.xml", option);
		
		uiPanelSettingCache.add("UIPanelDateTime.xml", header);
		uiPanelSettingCache.add("UIPanelDateTime.xml", option);
		
		//Access Bar
		uiPanelSettingCache.add("UIPanelAccessBarButton.xml", header);
		uiPanelSettingCache.add("UIPanelAccessBarButton.xml", option);
		
		//UIPanelVerticalSpliter
		uiPanelSettingCache.add("UIPanelVerticalSpliter.xml", header);
		uiPanelSettingCache.add("UIPanelVerticalSpliter.xml", option);
		
		//UIScreenMMI
		uiPanelSettingCache.add("UIScreenMMI.xml", header);
		uiPanelSettingCache.add("UIScreenMMI.xml", option);
		
		uiPanelSettingCache.add("UIScreenMMINorthPanel.xml", header);
		uiPanelSettingCache.add("UIScreenMMINorthPanel.xml", option);
		
		uiPanelSettingCache.add("UIScreenMMIEastPanel.xml", header);
		uiPanelSettingCache.add("UIScreenMMIEastPanel.xml", option);
		
		uiPanelSettingCache.add("UIScreenMMISouthPanel.xml", header);
		uiPanelSettingCache.add("UIScreenMMISouthPanel.xml", option);
		
		uiPanelSettingCache.add("UIScreenMMIWestPanel.xml", header);
		uiPanelSettingCache.add("UIScreenMMIWestPanel.xml", option);
		
		// UIPanelMenu
		uiPanelSettingCache.add("UIPanelNavigation_0.xml", header);
		uiPanelSettingCache.add("UIPanelNavigation_0.xml", option);
		
		uiPanelSettingCache.add("UIPanelNavigation_1.xml", header);
		uiPanelSettingCache.add("UIPanelNavigation_1.xml", option);
		
		uiPanelSettingCache.add("UIPanelNavigation_2.xml", header);
		uiPanelSettingCache.add("UIPanelNavigation_2.xml", option);
		
		uiPanelSettingCache.add("UIPanelNavigation_3.xml", header);
		uiPanelSettingCache.add("UIPanelNavigation_3.xml", option);
		
		String module = null;
		
		uiPanelSettingCache.init(module, "UIPanelGeneric", new DictionaryCacheEvent() {
			
			@Override
			public void dictionaryCacheEventReady(int received) {

				logger.log(Level.SEVERE, "dictionaryCacheEventReady");
					
				ready(received);
			}
		});
		
		logger.log(Level.FINE, "initCache End");
	}
	
	private void ready(int received) {
		logger.log(Level.FINE, "ready Begin");
		
		logger.log(Level.FINE, "ready BF");
		
//		logger.log(Level.SEVERE, "ready BF");
//		SimpleRemoteLogHandler handler = new SimpleRemoteLogHandler();
//		handler.setLevel(Level.FINE);
//		Logger.getLogger("").addHandler(handler);
//		logger.log(Level.SEVERE, "ready AF");
		
		logger.log(Level.FINE, "ready AF");

		
		this.main.clear();
		this.main.add(new UIPanelScreen().getMainPanel(this.uiNameCard));
		logger.log(Level.FINE, "ready End");
	}
}
