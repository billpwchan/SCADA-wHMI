package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelViewFactoryMgr {
	private static Logger logger = Logger.getLogger(UIPanelViewFactoryMgr.class.getName());
	
	public static String UIPanelViewPanel		= "UIPanelViewPanel";
	public static String UIPanelViewSchematic	= "UIPanelViewSchematic";
	public static String UIPanelViewEmpty		= "UIPanelViewEmpty";

	public DockLayoutPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		DockLayoutPanel dockLayoutPanel = this.getPanel(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}
	
	public UIPanelViewProvide getPanel(String uiPanel) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIPanelViewProvide uiPanelViewProvide = null;
		
		if ( 0 == uiPanel.compareTo("UIPanelViewPanel") ) {
			uiPanelViewProvide = new UIPanelViewPanel();
		} else if ( 0 == uiPanel.compareTo("UIPanelViewSchematic") ) {
			uiPanelViewProvide = new UIPanelViewSchematic();
		} else if ( 0 == uiPanel.compareTo("UIPanelViewPanel") ) {
			uiPanelViewProvide = new UIPanelViewEmpty();
		}
		
		/*
		switch ( uiPanel ) {
		case "UIPanelViewPanel":
		{
			uiPanelViewProvide = new UIPanelViewPanel();
		}
			break;
		case "UIPanelViewSchematic":
		{
			uiPanelViewProvide = new UIPanelViewSchematic();
		}
			break;
		case "UIPanelViewEmpty":
			
		default:{
			uiPanelViewProvide = new UIPanelViewEmpty();
		}
			break;			
		}
		*/
		
		logger.log(Level.FINE, "getPanel End");

		return uiPanelViewProvide;
		
	}
}
