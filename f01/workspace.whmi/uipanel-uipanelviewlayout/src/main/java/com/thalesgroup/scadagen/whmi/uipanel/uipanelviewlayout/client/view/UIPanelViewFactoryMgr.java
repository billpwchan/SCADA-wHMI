package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewFactoryMgr {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public final static String UIPanelViewPanel		= "UIPanelViewPanel";
	public final static String UIPanelViewSchematic	= "UIPanelViewSchematic";
	public final static String UIPanelViewEmpty		= "UIPanelViewEmpty";
	
	public UIWidget_i getMainPanel(String uiCtrl, UINameCard uiNameCard) {
		final String function = "getPanel";
		
		logger.begin(function);
		
		logger.debug(function, "getPanel uiCtrl[{}]", uiCtrl);
		
		UIWidget_i uiWidget_i = null;
		
		if ( 0 == uiCtrl.compareTo(UIPanelViewPanel) ) {
			
			uiWidget_i = new UIPanelViewPanel();
		}
		else if ( 0 == uiCtrl.compareTo(UIPanelViewSchematic) ) {
			
			uiWidget_i = new UIPanelViewSchematic();
		}
		else if ( 0 == uiCtrl.compareTo(UIPanelViewEmpty) ) {
			
			uiWidget_i = new UIPanelViewEmpty();
		}
		
		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else {
			logger.warn(function, "uiCtrl[{}] uiWidget_i IS NULL", uiCtrl);
		}

		logger.end(function);

		return uiWidget_i;
		
	}
}
