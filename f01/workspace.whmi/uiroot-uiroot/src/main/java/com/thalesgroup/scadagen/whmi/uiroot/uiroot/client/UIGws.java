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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIPanelScreen;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

/**
 * @author syau
 *
 */
public class UIGws {

	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
//	private Logger jul_logger = Logger.getLogger("");
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIGws.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private SimplePanel root = null;

	public Panel getMainPanel() {
		final String function = "getMainPanel";
		
		logger.begin(className, function);

		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
//		initCache();
		initCaches();

		Panel panel = getMainPanel(new UINameCard(0, "", RESETABLE_EVENT_BUS));
		
		logger.end(className, function);
		
		return panel;
	}

	private UINameCard uiNameCard = null;
	public Panel getMainPanel(UINameCard uiNameCard) {
		final String function = "getMainPanel";

		logger.begin(className, function);
		
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
		
		logger.info(className, function, "getMainPanel Debug strLogModule[{}]", strLogModule);
		logger.info(className, function, "getMainPanel Debug strLogModuleLevel[{}]", strLogModuleLevel);
		
		if ( null != strLogModule && null != strLogModuleLevel ) {
			Level level = Level.FINE;
			if ( Level.ALL.getName().equals(strLogModuleLevel) ) {
				level = Level.ALL;
			} else if ( Level.FINE.getName().equals(strLogModuleLevel) ) {
				level = Level.FINE;
			} else if ( Level.INFO.getName().equals(strLogModuleLevel) ) {
				level = Level.INFO;
			} else if ( Level.CONFIG.getName().equals(strLogModuleLevel) ) {
				level = Level.CONFIG;
			} else if ( Level.WARNING.getName().equals(strLogModuleLevel) ) {
				level = Level.WARNING;
			} else if ( Level.SEVERE.getName().equals(strLogModuleLevel) ) {
				level = Level.SEVERE;
			}
			
			Logger.getLogger(strLogModule).setLevel(level);
			
//			String strLogModules [] = strLogModule.split("\\|");
//			for ( String s: strLogModules ) {
//				Logger.getLogger(s).setLevel(level);
//			}
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
				logger.warn(className, function, "getMainPanel NumberFormatException e[{}]", e.toString());
			}
			if ( ! valid ) {
				setting.set("numofscreen", Integer.toString(1));
			}
		}

		this.root = new SimplePanel();
		this.root.addStyleName("project-gwt-panel-gws-main");
		this.root.add(new UIGwsCache().getMainPanel(this.uiNameCard));

		// Debug
		HashMap<String, String> hashMap = setting.getMaps();
		for ( Map.Entry<String, String> entry : hashMap.entrySet() ) {
			logger.info(className, function, "Debug key[{}] value[{}]", entry.getKey(), entry.getValue());
		}
		// End of Debug

		logger.end(className, function);
				
		return root;
	}
	
	public void initCaches () {
		final String function = "initCaches";

		
		logger.begin(className, function);
		
		{
			final String header = "header";
			final String option = "option";			
			
			DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIWidgetGeneric");
			
			String mode = ConfigurationType.XMLFile.toString();
			
			String module = null;
			
			String folder = "UIPanelGeneric";
			
			String extention = ".xml";
			
			dictionariesCache.add(folder, extention, header);
			dictionariesCache.add(folder, extention, option);
			
			dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
				
				@Override
				public void dictionariesCacheEventReady(int received) {
					logger.info(className, function, "dictionaryCacheEventReady received[{}]", received);
					
					ready(received);
					
				}
			});
		}
		
		
		{
			DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIInspectorPanel");
			
			String mode = ConfigurationType.PropertiesFile.toString();
			
			String module = null;
			
			String folder = "UIInspectorPanel";
			
			String extention = ".properties";
			
			dictionariesCache.add(folder, extention, null);
			
			dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
				
				@Override
				public void dictionariesCacheEventReady(int received) {
					logger.info(className, function, "dictionaryCacheEventReady received[{}]", received);
					
					ready(received);
					
				}
			});
		}
		
		logger.end(className, function);
	}

//	public void initCache () {
//		final String function = "initCache";
//		
//		logger.begin(className, function);
//		
//		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
//		
//		String header = "header";
//		String option = "option";
//		
//		//Login Panel
//		uiPanelSettingCache.add("UIPanelLogin.xml", header);
//		uiPanelSettingCache.add("UIPanelLogin.xml", option);
//		String mode = ConfigurationType.XMLFile.toString();
//		String module = null;
//		
//		uiPanelSettingCache.init(mode, module, "UIPanelGeneric", new DictionaryCacheEvent() {
//			
//			@Override
//			public void dictionaryCacheEventReady(int received) {
//
//				logger.info(className, function, "dictionaryCacheEventReady received[{}]", received);
//					
//				ready(received);
//			}
//		});
//		
//		logger.end(className, function);
//	}
	
	private void ready(int received) {
		final String function = "ready";
		
		logger.begin(className, function);

		UIWidget_i uiWidget_i = new UIPanelScreen();
		uiWidget_i.setUINameCard(this.uiNameCard);
		uiWidget_i.init();
		Panel panel = uiWidget_i.getMainPanel();
		
		this.root.clear();	
		this.root.add(panel);
		
		logger.end(className, function);
	}
}
