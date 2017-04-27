package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init.InitCachePropertiesFile;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init.InitOpm;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.init.InitReady_i;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIScreenRootMgr;
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
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIGws.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private Panel root = null;

	protected UINameCard uiNameCard = null;
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.warn(className, "setUINameCard", "uiNameCard IS NULL");
		}
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);		
		
		InitCacheXMLFile.initCacheXMLFile(uiDict, "*.xml", new InitReady_i() {
			public void ready(int received) {
				readyToLoad("UIWidgetGeneric", received);
			}
		});
		InitCachePropertiesFile.initCachePropertiesFile(uiProp, "*.properties");
		InitCacheJsonsFile.initCacheJsonsFile(uiJson, "*.json");
		
		InitOpm.initOpmFactory();
		
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
				
				logger.debug(className, function, "keyLowerCase[{}] value[{}]", keyLowerCase, value);
			}
		}
		// end of parameter override

		// Num Of Screen
		String strNumOfScreen = setting.get("numofscreen");
		if ( null == strNumOfScreen ) {
			setting.set("numofscreen", Integer.toString(1));
		} else {
			boolean valid = false;
			try {
				int numOfScreen = Integer.parseInt(strNumOfScreen);
				if ( numOfScreen > 0 ) valid = true;
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
			logger.debug(className, function, "Debug key[{}] value[{}]", entry.getKey(), entry.getValue());
		}
		// End of Debug

		logger.end(className, function);
	}
	
	public Panel getMainPanel() {
		final String function = "getMainPanel";
		logger.beginEnd(className, function);
		return root;
	}
	
	private String uiDict = null;
	public void setDictionaryFolder ( String uiDict ) {
		this.uiDict = uiDict;
	}
	
	private String uiProp = null;
	public void setPropertyFolder ( String uiProp ) {
		this.uiProp = uiProp;
	}
	
	private String uiJson = null;
	public void setJsonFolder ( String uiJson ) {
		this.uiJson = uiJson;
	}

	private String uiCtrl = null;
	public void setUICtrl(String uiCtrl) {
		this.uiCtrl = uiCtrl;
	}	
	
	private String uiView = null;
	public void setViewXMLFile(String uiView) {
		this.uiView = uiView;
	}
	
	private String uiOpts = null;
	public void setOptsXMLFile(String uiOpts) {
		this.uiOpts = uiOpts;
	}
	
	private String uiElem = null;
	public void setElement(String uiElem) {
		this.uiElem = uiElem;
	}
	
	private HashMap<String, Object> options = null;
	public void setOptions(HashMap<String, Object> options) {
		this.options = options;
	}

	private boolean isCreated = false;
	private void readyToLoad(String folder, int received) {
		final String function = "ready";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "folder[{}] received[{}]", folder, received);

		if ( ! isCreated ) {
			
			UIScreenRootMgr uiPanelFactoryMgr = UIScreenRootMgr.getInstance();
			UIWidget_i uiWidget_i = uiPanelFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
			
			if ( null != uiWidget_i ) {
				Panel panel = uiWidget_i.getMainPanel();
				
				this.root.clear();	
				this.root.add(panel);
			} else {
				logger.warn(className, function, "uiWidget_i IS NULL");
			}

			isCreated = true;
			
			// Try to init the OPM
			InitOpm.initOpm("UIOpmSCADAgen");
			
		} else {
			logger.warn(className, function, "ready folder[{}] received[{}] isCreated", folder, received);
		}

		
		logger.end(className, function);
	}
}
