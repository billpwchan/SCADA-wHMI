package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelNavigation implements UIWidget_i {
	
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
	
	public ComplexPanel getMenu(UINameCard uiNameCard, String menuLevel, String menuType) {
		
		logger.log(Level.FINE, "getMenu Begin");
		
		UIPanelMenus uiPanelMenus = getMenus(uiNameCard);
		
		logger.log(Level.SEVERE, "getMenu uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"]");
		
		ComplexPanel complexPanel = uiPanelMenus.getMenu(menuLevel, menuType);
		
		logger.log(Level.FINE, "getMenu End");
		
		return complexPanel;
	}
	
	@Override
	public ComplexPanel getMainPanel() {
		ComplexPanel menu = getMenu(this.uiNameCard, parameters.get("menuLevel"), parameters.get("menuType"));
		return menu;
	}
	
	private HashMap<String, String> parameters = new HashMap<String, String>();
	@Override
	public void setParameter(String key, String value) {
		parameters.put(key, value);
	}
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
	}
	@Override
	public void init(String xmlFile) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}
}
