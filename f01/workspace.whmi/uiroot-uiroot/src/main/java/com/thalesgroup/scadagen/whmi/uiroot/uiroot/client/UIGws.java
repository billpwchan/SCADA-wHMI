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
import com.thalesgroup.scadagen.whmi.config.configenv.client.UIPanelSetting;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIPanelScreen;

public class UIGws {

	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null; 

	private static Logger logger = Logger.getLogger("");

	static int counter = 0;
		
	public SplitLayoutPanel getMainPanel() {
		
		EVENT_BUS = GWT.create(SimpleEventBus.class);
	
		logger.log(Level.FINE, "UIGws Begin");
		
		/**
		 * Configuration.
		 */
		// start of parameter override
		UIPanelSetting uiPanelSetting = UIPanelSetting.getInstance();
		Map<String, List<String>> paramsMap = Window.Location.getParameterMap();
		for ( String key: paramsMap.keySet() ) {
			List<String> values = paramsMap.get(key);
			if ( values.size() > 0 ) {
				String value = values.get(0);
				uiPanelSetting.set(key.toLowerCase(), value);
			}
		}
		// end of parameter override

		
		RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		UINameCard uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);
		uiNameCard.appendUIPanel(this);

		SplitLayoutPanel root = new SplitLayoutPanel();
		
		String strNumOfScreen = uiPanelSetting.get("numofscreen");
		
		if ( null == strNumOfScreen ) {
			uiPanelSetting.set("numofscreen", Integer.toString(1));
		} else {
			boolean valid = false;
			try {
				int numOfScreen = Integer.parseInt(strNumOfScreen);
				if ( numOfScreen >= 1 && numOfScreen <= 3 ) {
					valid = true;
				}
			} catch ( NumberFormatException e) {
				logger.log(Level.SEVERE, "UIGws NumberFormatException e["+e.toString()+"]");
			}
			if ( ! valid ) {
				uiPanelSetting.set("numofscreen", Integer.toString(1));
			}
		}
		
		String strDebug = uiPanelSetting.get("debug");

		if ( null != strDebug && 0==strDebug.compareToIgnoreCase("true") && LogConfiguration.loggingIsEnabled() ) {
			int hight = 200;
			VerticalPanel logArea = new VerticalPanel();
			logArea.setSize("100%", "100%");
			ScrollPanel scrollPanel = new ScrollPanel(logArea);
			scrollPanel.setSize("100%", "100%");
			root.addSouth(scrollPanel, hight);
			logger.addHandler(new HasWidgetsLogHandler(logArea));
		}

		HorizontalPanel main = new UIPanelScreen().getMainPanel(uiNameCard);
		root.add(main);
		
		
		// Debug Area
		HashMap<String, String> hashMap = uiPanelSetting.getMaps();
		for ( Map.Entry<String, String> entry : hashMap.entrySet() ) {
			logger.log(Level.SEVERE, "UIGws UIPanelSetting key["+entry.getKey()+"] value["+entry.getValue()+"]");
		}
		// End of Debug Area

		logger.log(Level.FINE, "UIGws End");
				
		return root;
	}


	
}
