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
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIScreenRootMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCachePropertiesFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitOpm;

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
	
	private Map<String, Object> params = new HashMap<String, Object>();
	public void setParameter(String key, Object value) {
		params.put(key, value);
	}
	public void setParameters(Map<String, Object> params) {
		this.params.clear();
		for ( String key : params.keySet() ) {
			this.params.put(key, params.get(key));
		}
	}
	
	private String uiDict = null, uiProp = null, uiJson = null, uiCtrl = null, uiView = null, uiOpts = null, uiElem = null;
	private String getStringParameter(String key) {
		String value = null;
		if ( null != params) {
			Object obj = params.get(key);
			if ( null != obj ) {
				if ( obj instanceof String ) {
					value = (String)obj;
    			} else {
    				logger.warn(className, "getParameter", "key[{}] obj IS NOT A String", key);
    			}
    		} else {
    			logger.warn(className, "getParameter", "key[{}] framework obj IS NULL", key);
    		}
		}
		return value;
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);

		uiDict = getStringParameter(UIGws_i.Parameters.uiDict.toString());
		uiProp = getStringParameter(UIGws_i.Parameters.uiProp.toString());
		uiJson = getStringParameter(UIGws_i.Parameters.uiJson.toString());
		uiCtrl = getStringParameter(UIGws_i.Parameters.uiCtrl.toString());
		uiView = getStringParameter(UIGws_i.Parameters.uiView.toString());
		uiOpts = getStringParameter(UIGws_i.Parameters.uiOpts.toString());
		uiElem = getStringParameter(UIGws_i.Parameters.uiElem.toString());
		
		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		Map<String, Object> params = null;
		
		InitProcess_i initProcess = new InitProcess_i() {

			@Override
			public void process(final Map<String, Object> params,
					final InitReady_i initReady) {

				InitCacheXMLFile.getInstance().initCacheXMLFile(uiDict, "*.xml", new InitReady_i() {
					
					public void ready(final Map<String, Object> params) {

						if ( null != initReady ) initReady.ready(params);
					}
				});
				
				InitCachePropertiesFile.getInstance().initCachePropertiesFile(uiProp, "*.properties");
				
				InitCacheJsonsFile.getInstance().initCacheJsonsFile(uiJson, "*.json");
				
				InitOpm.getInstance().initOpmFactory();

			}
		};
		
		InitReady_i initReady = new InitReady_i() {
			
			@Override
			public void ready(final Map<String, Object> params) {
				
				int received = 0;
				if ( null != params) {
					Object obj = params.get("received");
					if ( null != obj && obj instanceof Integer ) {
						received = (Integer)obj;
					}
				}
				
				readyToLoad("UIWidgetGeneric", received);
			}
		};
		
		UIWidgetEntryPoint.init(params, initProcess, initReady);

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
			InitOpm.getInstance().initOpm("UIOpmSCADAgen");
			
		} else {
			logger.warn(className, function, "ready folder[{}] received[{}] isCreated", folder, received);
		}

		
		logger.end(className, function);
	}
}
