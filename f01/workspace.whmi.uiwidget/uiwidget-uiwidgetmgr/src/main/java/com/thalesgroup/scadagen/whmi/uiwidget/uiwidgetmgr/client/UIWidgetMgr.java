package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client;

import java.util.HashMap;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIWidgetMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIWidgetMgr() {};
	private static UIWidgetMgr instance = null;
	public static UIWidgetMgr getInstance() {
		if ( null == instance ) 
			instance = new UIWidgetMgr();
		return instance;
	}

	public Panel getMainPanel(String uiPanel, UINameCard uiNameCard){
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		logger.info(className, function, "uiNameCard[{}]", uiNameCard.getUiPath());
		
		Panel panel = null;
		UIWidget_i uiwidget = this.getUIWidget(uiPanel);
		
		if ( null != uiwidget ) {
			uiwidget.setUINameCard(uiNameCard);
			panel = uiwidget.getMainPanel();
		} else {
			logger.error(className, function, "IS NULL");
		}

		if ( null == panel ) {
			logger.error(className, function, "complexPanel IS NULL");
		}
		
		logger.end(className, function);

		return panel;
	}
	
	private HashMap<String, UIWidgetMgrFactory> uiWidgetMgrFactorys = new HashMap<String, UIWidgetMgrFactory>();
	public void clearUIWidgetFactorys() { this.uiWidgetMgrFactorys.clear(); }
	public void addUIWidgetFactory(String xmlName, UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(xmlName, uiWidgetMgrEvent); }
	public void removeUIWidgetFactory(UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.remove(uiWidgetMgrEvent); }

	public UIWidget_i getUIWidget(String widget) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		logger.error(className, function, "uiPanel[{}]", widget);
		
		UIWidget_i uiWidget = null;
		
		for ( String xmlName : uiWidgetMgrFactorys.keySet() ) {
			
			UIWidgetMgrFactory uiWidgetMgrFactory = uiWidgetMgrFactorys.get(xmlName);
			
			if ( null != uiWidgetMgrFactory ) {
			
				logger.error(className, function, "uiWidgetMgrFactory");
			
				uiWidget = uiWidgetMgrFactory.getUIWidget(widget);
				
				if ( null != uiWidget ) break;
			
			} else {
			
				logger.error(className, function, "uiWidgetMgrFactory IS NULL");
			}
		}
		
		if ( null == uiWidget ) {
			logger.error(className, function, "uiWIdget IS NULL");
			logger.error(className, function, "widget[{}] NOT FOUND", widget);
		}
		
		logger.end(className, function);

		return uiWidget;
	}
}
