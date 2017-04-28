package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelNavigation extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelNavigation.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIPanelNavigation uiPanelNavigation = null;
	private UIPanelNavigation() {}
	public static UIPanelNavigation getInstance() {
		if ( null == uiPanelNavigation ) uiPanelNavigation = new UIPanelNavigation();
		return uiPanelNavigation; 
	}
	
	public void resetInstance() {
		final String function = "resetInstance";
		logger.begin(className, function);
		uiPanelNavigation = null;
		logger.end(className, function);
	}
	
	private HashMap<Integer, UIPanelMenus> hashMap = new HashMap<Integer, UIPanelMenus>();
	public UIPanelMenus getMenus(UINameCard uiNameCard) {
		final String function = "getMenus";
		
		logger.begin(className, function);
		logger.info(className, function, "getMenu uiNameCard.getUiScreen()[{}] uiNameCard.getUiPath()[{}]", uiNameCard.getUiScreen(), uiNameCard.getUiPath());
		
		Integer screen = Integer.valueOf(uiNameCard.getUiScreen());
		
		logger.info(className, function, "getMenu screen["+screen+"]");
		
		UIPanelMenus uiPanelMenus = this.hashMap.get(screen);
		if ( null == uiPanelMenus ) {
			this.hashMap.put(screen, new UIPanelMenus(uiNameCard));
		}
		uiPanelMenus = this.hashMap.get(screen);
		
		logger.info(className, function, "getMenu uiNameCard.getUiScreen()[{}] uiPanelMenus[{}] hash code", uiNameCard.getUiScreen(), uiPanelMenus);
			
		if ( null == uiPanelMenus ) {
			logger.warn(className, function, "getMenu uiPanelMenus[{}] IS NULL", uiPanelMenus);
		}

		logger.end(className, function);
		return uiPanelMenus;
	}
	
	public Panel getMenu(UINameCard uiNameCard, String menuLevel, String menuType) {
		final String function = "getMenu";
		
		logger.begin(className, function);
		
		UIPanelMenus uiPanelMenus = getMenus(uiNameCard);
		
		logger.info(className, function, "uiNameCard.getUiScreen()[{}]", uiNameCard.getUiScreen());
		
		Panel panel = uiPanelMenus.getMenu(menuLevel, menuType);
		
		logger.end(className, function);
		
		return panel;
	}
	
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = "setUINameCard";
		logger.begin(className, function);
		this.uiNameCard = new UINameCard(uiNameCard);
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		final String function = "init";
		logger.beginEnd(className, function);
	}
	
	@Override
	public Panel getMainPanel() {
		return getMenu(this.uiNameCard, (String)parameters.get("menuLevel"), (String)parameters.get("menuType"));
	}

}
