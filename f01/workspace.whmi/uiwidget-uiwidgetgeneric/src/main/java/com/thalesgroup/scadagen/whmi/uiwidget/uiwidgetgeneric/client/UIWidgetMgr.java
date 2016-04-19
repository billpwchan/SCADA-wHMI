package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelAlarmBanner;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelAlarmBannerList;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelEmpty;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelSoundServerController;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelStatusBar;

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
	
	public UIWidget_i getUIWidget(String widget) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.SEVERE, "getPanel uiPanel["+widget+"]");
		
		UIWidget_i uiWIdget = null;
		if ( 0 == widget.compareTo("UIPanelSoundServerController") ) {
			uiWIdget = new UIPanelSoundServerController();
		} else if ( 0 == widget.compareTo("UIPanelAccessBar") ) {
			uiWIdget = new UIPanelAccessBar();
		} else if ( 0 == widget.compareTo("UIPanelAlarmBanner") ) {
			uiWIdget = new UIPanelAlarmBanner();
		} else if ( 0 == widget.compareTo("UIPanelStatusBar") ) {
			uiWIdget = new UIPanelStatusBar();
		} else if ( 0 == widget.compareTo("UIPanelAlarmBannerList") ) {
			uiWIdget = new UIPanelAlarmBannerList();
		} else if ( 0 == widget.compareTo("UIPanelViewLayout") ) {
			uiWIdget = new UIPanelViewLayout();
		} else if ( 0 == widget.compareTo("UIPanelEmpty") ) {
			uiWIdget = new UIPanelEmpty();
		}
		
		logger.log(Level.FINE, "getPanel End");

		return uiWIdget;
	}
}
