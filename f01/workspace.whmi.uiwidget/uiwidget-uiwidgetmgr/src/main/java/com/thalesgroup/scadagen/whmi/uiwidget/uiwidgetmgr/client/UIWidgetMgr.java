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

	public Panel getMainPanel(String widget, String view, UINameCard uiNameCard, HashMap<String, Object> options){
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		logger.info(className, function, "uiNameCard[{}]", uiNameCard.getUiPath());
		
		Panel panel = null;
		UIWidget_i uiwidget = this.getUIWidget(widget, view, uiNameCard, options);
		
		if ( null != uiwidget ) {
			uiwidget.setUINameCard(uiNameCard);
			panel = uiwidget.getMainPanel();
		} else {
			logger.warn(className, function, "uiwidget IS NULL");
		}

		if ( null == panel ) {
			logger.warn(className, function, "panel IS NULL");
		}
		
		logger.end(className, function);

		return panel;
	}
	
	private HashMap<String, UIWidgetMgrFactory> uiWidgetMgrFactorys = new HashMap<String, UIWidgetMgrFactory>();
	public void clearUIWidgetFactorys() { this.uiWidgetMgrFactorys.clear(); }
	public void addUIWidgetFactory(String xmlName, UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(xmlName, uiWidgetMgrEvent); }
	public void removeUIWidgetFactory(UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.remove(uiWidgetMgrEvent); }

	public UIWidget_i getUIWidget(String widget, String view, UINameCard uiNameCard, HashMap<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		logger.info(className, function, "uiPanel[{}]", widget);
		
		UIWidget_i uiWidget = null;
		
		for ( String xmlName : uiWidgetMgrFactorys.keySet() ) {
			
			UIWidgetMgrFactory uiWidgetMgrFactory = uiWidgetMgrFactorys.get(xmlName);
			
			if ( null != uiWidgetMgrFactory ) {
			
				logger.warn(className, function, "uiWidgetMgrFactory");
			
				uiWidget = uiWidgetMgrFactory.getUIWidget(widget, view, uiNameCard, options);
				
				if ( null != uiWidget ) break;
			
			} else {
				logger.warn(className, function, "uiWidgetMgrFactory IS NULL");
			}
		}
		
		if ( null == uiWidget ) {
			logger.warn(className, function, "uiWIdget IS NULL");
			logger.warn(className, function, "widget[{}] NOT FOUND", widget);
		}
		
		logger.end(className, function);

		return uiWidget;
	}
}
