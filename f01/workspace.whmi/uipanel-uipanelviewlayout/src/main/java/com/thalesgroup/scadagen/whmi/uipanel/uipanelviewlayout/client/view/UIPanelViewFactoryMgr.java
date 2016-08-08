package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewFactoryMgr {
	
	private Logger logger = Logger.getLogger(UIPanelViewFactoryMgr.class.getName());
	
	public static String UIPanelViewPanel		= "UIPanelViewPanel";
	public static String UIPanelViewSchematic	= "UIPanelViewSchematic";
	public static String UIPanelViewEmpty		= "UIPanelViewEmpty";

	public ComplexPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.FINE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		UIWidget_i uiWidget_i =  this.getPanel(uiPanel, uiNameCard);
		ComplexPanel rootPanel = uiWidget_i.getMainPanel();

		logger.log(Level.FINE, "getMainPanel End");

		return rootPanel;
	}
	
	public UIWidget_i getPanel(String uiPanel, UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+uiPanel+"]");
		
		UIWidget_i uiWidget_i = null;
		
		if ( 0 == uiPanel.compareTo(UIPanelViewPanel) ) {
			uiWidget_i = new UIPanelViewPanel();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo(UIPanelViewSchematic) ) {
			uiWidget_i = new UIPanelViewSchematic();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo(UIPanelViewEmpty) ) {
			uiWidget_i = new UIPanelViewEmpty();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		}

		logger.log(Level.FINE, "getPanel End");

		return uiWidget_i;
		
	}
}
