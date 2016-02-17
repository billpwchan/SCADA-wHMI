package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

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

	private static Logger logger = null;

	//private UINameCard uiNameCard = null;
	
	//private SplitLayoutPanel root = null;
	//private VerticalPanel logArea = null;
	
	static int counter = 0;
		
	public SplitLayoutPanel getMainPanel() {
		
		EVENT_BUS = GWT.create(SimpleEventBus.class);
		
		logger = Logger.getLogger("");
	
		logger.log(Level.FINE, "UIGws Begin");
		
		/**
		 * Configuration.
		 */
		UIPanelSetting.set("NUM_OF_SCREEN", Integer.toString(1));
		
		RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		UINameCard uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);
		uiNameCard.appendUIPanel(this);

		SplitLayoutPanel root = new SplitLayoutPanel();
		
		String strDebug = Window.Location.getParameter("debug");

		if ( null != strDebug && 0==strDebug.compareToIgnoreCase("true") && LogConfiguration.loggingIsEnabled()) {
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

		logger.log(Level.FINE, "UIGws End");
				
		return root;
	}


	
}
