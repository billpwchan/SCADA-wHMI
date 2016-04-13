package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelNavigation {
	
	private static Logger logger = Logger.getLogger(UIPanelNavigation.class.getName());
	
	private static UIPanelNavigation uiPanelNavigation = null;
	private UIPanelNavigation() {}
	public static UIPanelNavigation getInstance() { 
		if ( null == uiPanelNavigation ) uiPanelNavigation = new UIPanelNavigation();
		return uiPanelNavigation; 
	}
	
	private HashMap<Integer, UIPanelMenus> hashMap = new HashMap<Integer, UIPanelMenus>();
	public UIPanelMenus getMenus(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMenu Begin");
		logger.log(Level.SEVERE, "getMenu uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"] uiNameCard.getUiPath()["+uiNameCard.getUiPath()+"]");
		
		Integer screen = Integer.valueOf(uiNameCard.getUiScreen());
		
		logger.log(Level.FINE, "getMenu screen["+screen+"]");
		
		UIPanelMenus uiPanelMenus = this.hashMap.get(screen);
		if ( null == uiPanelMenus ) {
			this.hashMap.put(screen, new UIPanelMenus(uiNameCard));
		}
		uiPanelMenus = this.hashMap.get(screen);
		
		logger.log(Level.FINE, "getMenu uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"] uiPanelMenus["+uiPanelMenus+"] hash code");
			
		if ( null == uiPanelMenus ) {
			logger.log(Level.SEVERE, "getMenu uiPanelMenus["+uiPanelMenus+"] IS NULL");
		}

		return uiPanelMenus;
	}
	public ComplexPanel getMenu(UINameCard uiNameCard, int level, String menuType) {
		
		logger.log(Level.FINE, "getMenu Begin");
		
		UIPanelMenus uiPanelMenus = getMenus(uiNameCard);
		
		logger.log(Level.SEVERE, "getMenu uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"]");
		
		ComplexPanel complexPanel = uiPanelMenus.getMenu(level, menuType);
		
		logger.log(Level.FINE, "getMenu End");
		
		return complexPanel;
	}
}
