package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

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
		
		ComplexPanel complexPanel = this.getUIWidget(uiPanel).getMainPanel(uiNameCard);
		
		logger.log(Level.FINE, "getMainPanel End");

		return complexPanel;
	}
	
	private HashMap<String, UIWidgetMgrEvent> uiWidgetMgrEvents = new HashMap<String, UIWidgetMgrEvent>();
	public void clearUIWidgetEvents() { this.uiWidgetMgrEvents.clear(); }
	public void addUIWidgetEvent(String name, UIWidgetMgrEvent uiWidgetMgrEvent) { this.uiWidgetMgrEvents.put(name, uiWidgetMgrEvent); }
	public void removeUIWidgetEvent(UIWidgetMgrEvent uiWidgetMgrEvent) { this.uiWidgetMgrEvents.remove(uiWidgetMgrEvent); }

	public UIWidget_i getUIWidget(String widget) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.SEVERE, "getPanel uiPanel["+widget+"]");
		
		UIWidget_i uiWidget = null;
		
		for ( String name : uiWidgetMgrEvents.keySet() ) {
			
			UIWidgetMgrEvent uiWidgetMgrEvent = uiWidgetMgrEvents.get(name);
			
			if ( null != uiWidgetMgrEvent ) {
			
				logger.log(Level.SEVERE, "getUIWidget uiWidgetMgrEvent");
			
				uiWidget = uiWidgetMgrEvent.getUIWidget(widget);
				
				if ( null != uiWidget ) break;
			
			} else {
			
				logger.log(Level.SEVERE, "getUIWidget uiWidgetMgrEvent IS NULL");
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
