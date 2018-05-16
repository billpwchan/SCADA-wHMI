package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelNavigation extends UIWidget_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIPanelNavigation uiPanelNavigation = null;
	private UIPanelNavigation() {}
	public static UIPanelNavigation getInstance() {
		if ( null == uiPanelNavigation ) uiPanelNavigation = new UIPanelNavigation();
		return uiPanelNavigation; 
	}
	
	public void resetInstance() {
		final String function = "resetInstance";
		logger.begin(function);
		uiPanelNavigation = null;
		logger.end(function);
	}
	
	private HashMap<Integer, UIPanelMenus> hashMap = new HashMap<Integer, UIPanelMenus>();
	public UIPanelMenus getMenus(UINameCard uiNameCard) {
		final String function = "getMenus";
		
		logger.begin(function);
		logger.debug(function, "getMenu uiNameCard.getUiScreen()[{}] uiNameCard.getUiPath()[{}]", uiNameCard.getUiScreen(), uiNameCard.getUiPath());
		
		Integer screen = Integer.valueOf(uiNameCard.getUiScreen());
		
		logger.debug(function, "getMenu screen["+screen+"]");
		
		UIPanelMenus uiPanelMenus = this.hashMap.get(screen);
		if ( null == uiPanelMenus ) {
			this.hashMap.put(screen, new UIPanelMenus(uiNameCard));
		}
		uiPanelMenus = this.hashMap.get(screen);
		
		logger.debug(function, "getMenu uiNameCard.getUiScreen()[{}] uiPanelMenus[{}] hash code", uiNameCard.getUiScreen(), uiPanelMenus);
			
		if ( null == uiPanelMenus ) {
			logger.warn(function, "getMenu uiPanelMenus[{}] IS NULL", uiPanelMenus);
		}

		logger.end(function);
		return uiPanelMenus;
	}
	
	public Panel getMenu(UINameCard uiNameCard, String menuLevel, String menuType) {
		final String function = "getMenu";
		
		logger.begin(function);
		
		UIPanelMenus uiPanelMenus = getMenus(uiNameCard);
		
		logger.debug(function, "uiNameCard.getUiScreen()[{}]", uiNameCard.getUiScreen());
		
		Panel panel = uiPanelMenus.getMenu(menuLevel, menuType);
		
		logger.end(function);
		
		return panel;
	}
	
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = "setUINameCard";
		logger.begin(function);
		this.uiNameCard = new UINameCard(uiNameCard);
		logger.end(function);
	}
	
	@Override
	public void init() {
		final String function = "init";
		logger.beginEnd(function);
	}
	
	@Override
	public Panel getMainPanel() {
		final String function = "getMainPanel";
		logger.begin(function);
		
		String menuLevel = null;
		String menuType = null;
		
		logger.debug(function, "getMenu optsXMLFile[{}]", optsXMLFile);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			menuLevel	= dictionariesCache.getStringValue(optsXMLFile, UIPanelNavigation_i.ParameterName.MenuLevel.toString(), strHeader);
			menuType	= dictionariesCache.getStringValue(optsXMLFile, UIPanelNavigation_i.ParameterName.MenuType.toString(), strHeader);
		}
		
		logger.debug(function, "getMenu menuLevel[{}] menuType[{}]", menuLevel, menuType);
		
		logger.end(function);
		
		return getMenu(this.uiNameCard, menuLevel, menuType);
		
//		return getMenu(this.uiNameCard, (String)parameters.get("menuLevel"), (String)parameters.get("menuType"));
	}

}
