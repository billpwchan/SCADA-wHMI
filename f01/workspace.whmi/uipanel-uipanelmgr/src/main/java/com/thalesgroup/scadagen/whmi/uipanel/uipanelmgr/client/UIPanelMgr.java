package com.thalesgroup.scadagen.whmi.uipanel.uipanelmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelempty.client.UIPanelEmpty;

public class UIPanelMgr {
	
	private static Logger logger = Logger.getLogger(UIPanelMgr.class.getName());
	
	private UIPanelMgr() {};
	private static UIPanelMgr instance = null;
	public static UIPanelMgr getInstance() {
		if ( null == instance ) 
			instance = new UIPanelMgr();
		return instance;
	}

	public DockLayoutPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		DockLayoutPanel dockLayoutPanel = this.getPanel(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}
	
	public UIPanel_i getPanel(String uiPanel) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIPanel_i uiPanel_i = null;
		
		//if ( 0 == compareTo("UIPanelEmpty") ) {
			uiPanel_i = new UIPanelEmpty();
		//}
		
		/*
		switch ( uiPanel ) {
		
		case "UIPanelEmpty":
			
		default:{
			uiPanel_i = new UIPanelEmpty();
		}
			break;			
		}
		*/
		
		logger.log(Level.FINE, "getPanel End");

		return uiPanel_i;
		
	}
}
