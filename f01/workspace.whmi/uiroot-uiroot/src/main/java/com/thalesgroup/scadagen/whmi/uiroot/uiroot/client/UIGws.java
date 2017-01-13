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
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgr_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIPanelScreen;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
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

	protected UINameCard uiNameCard = null;
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.warn(className, "setUINameCard", "uiNameCard IS NULL");
		}
	}
	
	public Panel getMainPanel() {
		final String function = "getMainPanel";

		logger.begin(className, function);
		
		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);		
		
		initCacheXMLFile();
		initCachePropertiesFile();
		
		setUINameCard(new UINameCard(0, "", RESETABLE_EVENT_BUS));
		
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
				String keyLowerCase = key.toLowerCase();
				setting.set(keyLowerCase, value);
				
				logger.info(className, function, "keyLowerCase[{}] value[{}]", keyLowerCase, value);
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
	
	public void initCacheXMLFile() {
		final String function = "initCacheXMLFile";
		
		logger.begin(className, function);
		
		final String header			= "header";
		final String option			= "option";
		final String action			= "action";
		final String actionset		= "actionset";
		final String [] tags = {header, option, action, actionset};
		String mode = ConfigurationType.XMLFile.toString();
		String module = null;
		String folder = dictionaryFolder;
		String extention = ".xml";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		for(String tag : tags ) {
			dictionariesCache.add(folder, extention, tag);
		}
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.info(className, function, "dictionaryCacheEventReady received[{}]", received);
				ready("UIWidgetGeneric", received);
			}
		});
		
		logger.end(className, function);
	}
	
	public void initCachePropertiesFile () {
		final String function = "initCachePropertiesFile";
		
		logger.begin(className, function);

		String mode = ConfigurationType.PropertiesFile.toString();
		String module = null;
		String folder = propertyFolder;
		String extention = ".properties";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		dictionariesCache.add(folder, extention, null);
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.info(className, function, "dictionaryCacheEventReady received[{}]", received);
			}
		});

		logger.end(className, function);
	}
	
	private String dictionaryFolder = null;
	public void setDictionaryFolder ( String dictionaryFolder ) {
		this.dictionaryFolder = dictionaryFolder;
	}
	
	private String propertyFolder = null;
	public void setPropertyFolder ( String propertyFolder ) {
		this.propertyFolder = propertyFolder;
	}

	private String viewXMLFile = null;
	public void setViewXMLFile(String viewXMLFile) {
		this.viewXMLFile = viewXMLFile;
	}
	
	private String optsXMLFile = null;
	public void setOptsXMLFile(String optsXMLFile) {
		this.optsXMLFile = optsXMLFile;
	}

	private boolean isCreated = false;
	private void ready(String folder, int received) {
		final String function = "ready";
		
		logger.begin(className, function);
		
		logger.info(className, function, "folder[{}] received[{}]", folder, received);

		if ( ! isCreated ) {
			
			String key = UIWidgetUtil.getClassSimpleName(UIDialogMsg.class.getName());
			UIDialogMgr.getInstance().addDialogs(key, new UIDialogMgr_i() {
				@Override
				public UIDialog_i getDialog() {
					return new UIDialogMsg();
				}
			});
			
			UIWidget_i uiWidget_i = new UIPanelScreen();
			uiWidget_i.setDictionaryFolder(dictionaryFolder);
			uiWidget_i.setViewXMLFile(viewXMLFile);
			uiWidget_i.setOptsXMLFile(optsXMLFile);
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
			Panel panel = uiWidget_i.getMainPanel();
			
			this.root.clear();	
			this.root.add(panel);
			
			isCreated = true;
			
		} else {
			logger.warn(className, function, "ready folder[{}] received[{}] isCreated", folder, received);
		}

		
		logger.end(className, function);
	}
}
