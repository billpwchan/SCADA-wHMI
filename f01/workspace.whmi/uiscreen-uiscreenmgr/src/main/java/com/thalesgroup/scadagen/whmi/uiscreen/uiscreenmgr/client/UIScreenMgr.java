package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreendss.client.UIScreenDSS;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client.UIScreenEmpty;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenLogin;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenOPM;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIScreenMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIScreenMgr() {}
	private static UIScreenMgr instance = null;
	public static UIScreenMgr getInstance() {
		if ( null == instance ) {
			instance = new UIScreenMgr();
		}
		return instance;
	}
	
	public Panel getMainPanel(String uiPanel, UINameCard uiNameCard){
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, " uiNameCard["+uiNameCard.getUiPath()+"]");
		
		UIWidget_i uiWidget_i = this.getPanel(uiPanel, uiNameCard);
		Panel panel = uiWidget_i.getMainPanel();
		
		logger.end(className, function);

		return panel;
	}

	public UIWidget_i getPanel(String uiPanel, UINameCard uiNameCard){
		final String function = "getPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, " uiPanel[{}]", "uiPanel");
		
		String xmlFile = null;
		UIWidget_i uiWidget_i = null;
		
		if ( 0 == uiPanel.compareTo("UIScreenLogin") ) {
			uiWidget_i = new UIScreenLogin();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setXMLFile(xmlFile);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo("UIScreenMMI") ) {
			uiWidget_i = new UIScreenMMI();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setXMLFile(xmlFile);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo("UIScreenDSS") ) {
			uiWidget_i = new UIScreenDSS();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setXMLFile(xmlFile);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo("UIScreenOPM") ) {
			uiWidget_i = new UIScreenOPM();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setXMLFile(xmlFile);
			uiWidget_i.init();
		} else {
			uiWidget_i = new UIScreenEmpty();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setXMLFile(xmlFile);
			uiWidget_i.init();
		}
		
		logger.end(className, function);

		return uiWidget_i;
	}
	
}
