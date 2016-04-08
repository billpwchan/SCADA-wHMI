package com.thalesgroup.scadagen.whmi.uipanel.uipanelmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelempty.client.UIPanelEmpty;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidget_i;

public class UIPanelMgr {
	
	private static Logger logger = Logger.getLogger(UIPanelMgr.class.getName());
	
	private UIPanelMgr() {};
	private static UIPanelMgr instance = null;
	public static UIPanelMgr getInstance() {
		if ( null == instance ) 
			instance = new UIPanelMgr();
		return instance;
	}

	public ComplexPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		ComplexPanel complexPanel = this.getPanel(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return complexPanel;
	}
	
	public UIWidget_i getUIWidget(String uiPanel) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIWidget_i uiWIdget = null;
		
		if ( 0 == uiPanel.compareTo("UIPanelAlarmBannerList") ) {
			uiWIdget = new UIPanelAlarmBannerList();
		} else if ( 0 == uiPanel.compareTo("UIPanelEmpty") ) {
			uiWIdget = new UIPanelEmpty();
		}
		
		logger.log(Level.FINE, "getPanel End");

		return uiWIdget;
		
	}
	
	public UIPanel_i getPanel(String uiPanel) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIPanel_i uiPanel_i = null;
		
		if ( 0 == uiPanel.compareTo("UIPanelAlarmBannerList") ) {
			uiPanel_i = new UIPanelAlarmBannerList();
		} else if ( 0 == uiPanel.compareTo("UIPanelEmpty") ) {
			uiPanel_i = new UIPanelEmpty();
		}
		
		logger.log(Level.FINE, "getPanel End");

		return uiPanel_i;
		
	}
}
