package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewAlarm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.PTWPanel;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIViewMgr {
	
	private static Logger logger = Logger.getLogger(UIViewMgr.class.getName());
	
	private UIViewMgr() {};
	private static UIViewMgr instance = null;
	public static UIViewMgr getInstance() {
		if ( null == instance ) instance = new UIViewMgr();
		return instance;
	}

	public UIWidget_i getPanel(String panel, UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getPanel Begin");
		
		logger.log(Level.FINE, "getPanel uiPanel["+panel+"]");

		UIWidget_i uiWidget_i = null;
		
		if ( 0 == panel.compareTo("UIViewAlarm") ) {
			uiWidget_i = new UIViewAlarm();
		} else if ( 0 == panel.compareTo("UIViewEvent") ) {
			uiWidget_i = new UIViewEvent();
		} else if ( 0 == panel.compareTo("PTWPanel") ) {		
			uiWidget_i = new PTWPanel();
		} else {
			//uiWidget_i = new UIViewEmpty();
		}
		
		uiWidget_i.setUINameCard(uiNameCard);
		uiWidget_i.init("");		

		logger.log(Level.FINE, "getPanel End");

		return uiWidget_i;
		
	}
}
