package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiview.client.UIView_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewempty.client.UIViewEmpty;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewAlarm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewEvent;

public class UIViewMgr {
	
	private static Logger logger = Logger.getLogger(UIViewMgr.class.getName());
	
	private UIViewMgr() {};
	private static UIViewMgr instance = null;
	public static UIViewMgr getInstance() {
		if ( null == instance ) 
			instance = new UIViewMgr();
		return instance;
	}
	

	public DockLayoutPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		DockLayoutPanel dockLayoutPanel = this.getPanel(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}
	
	public UIView_i getPanel(String uiPanel) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIView_i uiView_i = null;
		
		if ( 0 == uiPanel.compareTo("UIViewAlarm") ) {
			uiView_i = new UIViewAlarm();
		} else if ( 0 == uiPanel.compareTo("UIViewEvent") ) {
			uiView_i = new UIViewEvent();
		} else {
			//uiView_i = new UIViewEmpty();
		}
		
		/*
		switch ( uiPanel ) {
		
		case "UIViewAlarm":
		{
			uiView_i = new UIViewAlarm();
		}
		break;
		case "UIViewEvent":	
		{
			uiView_i = new UIViewEvent();
		}
		break;
		case "UIViewEmpty":
			
		default:{
			uiView_i = new UIViewEmpty();
		}
			break;			
		}
		*/
		
		logger.log(Level.FINE, "getPanel End");

		return uiView_i;
		
	}
}
