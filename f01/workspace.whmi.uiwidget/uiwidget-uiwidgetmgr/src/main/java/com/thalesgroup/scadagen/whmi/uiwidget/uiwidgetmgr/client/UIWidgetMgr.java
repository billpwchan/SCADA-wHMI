package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIWidgetMgr {
	
	private static Logger logger = Logger.getLogger(UIWidgetMgr.class.getName());
	
	private UIWidgetMgr() {};
	private static UIWidgetMgr instance = null;
	public static UIWidgetMgr getInstance() {
		if ( null == instance ) 
			instance = new UIWidgetMgr();
		return instance;
	}

	public ComplexPanel getMainPanel(String uiPanel, UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		logger.log(Level.SEVERE, "getMainPanel uiNameCard["+uiNameCard.getUiPath()+"]");
		
		UIWidget_i uiwidget = this.getUIWidget(uiPanel);
		uiwidget.setUINameCard(uiNameCard);
		ComplexPanel complexPanel = uiwidget.getMainPanel();
		
		logger.log(Level.FINE, "getMainPanel End");

		return complexPanel;
	}
	
	private HashMap<String, UIWidgetMgrFactory> uiWidgetMgrFactorys = new HashMap<String, UIWidgetMgrFactory>();
	public void clearUIWidgetFactorys() { this.uiWidgetMgrFactorys.clear(); }
	public void addUIWidgetFactory(String xmlName, UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(xmlName, uiWidgetMgrEvent); }
	public void removeUIWidgetFactory(UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.remove(uiWidgetMgrEvent); }

	public UIWidget_i getUIWidget(String widget) {
		
		logger.log(Level.FINE, "getUIWidget Begin");
		
		logger.log(Level.SEVERE, "getUIWidget uiPanel["+widget+"]");
		
		UIWidget_i uiWidget = null;
		
		for ( String xmlName : uiWidgetMgrFactorys.keySet() ) {
			
			UIWidgetMgrFactory uiWidgetMgrFactory = uiWidgetMgrFactorys.get(xmlName);
			
			if ( null != uiWidgetMgrFactory ) {
			
				logger.log(Level.SEVERE, "getUIWidget uiWidgetMgrFactory");
			
				uiWidget = uiWidgetMgrFactory.getUIWidget(widget);
				
				if ( null != uiWidget ) break;
			
			} else {
			
				logger.log(Level.SEVERE, "getUIWidget uiWidgetMgrFactory IS NULL");
			}
		}
		
		if ( null == uiWidget ) {
			logger.log(Level.SEVERE, "getUIWidget uiWIdget IS NULL");
			logger.log(Level.SEVERE, "getUIWidget widget["+widget+"] NOT FOUND");
		}
		
		logger.log(Level.FINE, "getPanel End");

		return uiWidget;
	}
}