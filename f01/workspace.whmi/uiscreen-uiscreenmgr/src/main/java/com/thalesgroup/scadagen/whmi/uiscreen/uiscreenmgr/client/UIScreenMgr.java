package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreendss.client.UIScreenDSS;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client.UIScreenEmpty;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenLogin;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenOPM;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;

public class UIScreenMgr {
	
	private static Logger logger = Logger.getLogger(UIScreenMgr.class.getName());
	
	private UIScreenMgr() {}
	private static UIScreenMgr uiScreenMgr = null;
	public static UIScreenMgr getInstance() {
		if ( null == uiScreenMgr ) {
			uiScreenMgr = new UIScreenMgr();
		}
		return uiScreenMgr;
	}
	
	public DockLayoutPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		DockLayoutPanel dockLayoutPanel = this.getPanel(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}

	public UIScreen_i getPanel(String uiPanel){
//Window.alert("UIPanelFactoryMgr.getPanel uiPanel["+uiPanel+"]");
		UIScreen_i uiPanel_i = null;
		
		if ( 0 == uiPanel.compareTo("UIScreenLogin") ) {
			uiPanel_i = new UIScreenLogin();
		} else if ( 0 == uiPanel.compareTo("UIScreenMMI") ) {
			uiPanel_i = new UIScreenMMI();
		} else if ( 0 == uiPanel.compareTo("UIScreenDSS") ) {
			uiPanel_i = new UIScreenDSS();
		} else if ( 0 == uiPanel.compareTo("UIScreenOPM") ) {
			uiPanel_i = new UIScreenOPM();
		} else {
			uiPanel_i = new UIScreenEmpty();
		}
		/*
		switch ( uiPanel ) {
		case "UIScreenLogin":
			uiPanel_i = new UIScreenLogin();
			break;
		case "UIScreenMMI":
			uiPanel_i = new UIScreenMMI();
			break;
		case "UIScreenDSS":
			uiPanel_i = new UIScreenDSS();
			break;
		case "UIScreenEmpty":
			
		default:
			uiPanel_i = new UIScreenEmpty();
			break;
		}
		*/
		return uiPanel_i;
	}
	
}
