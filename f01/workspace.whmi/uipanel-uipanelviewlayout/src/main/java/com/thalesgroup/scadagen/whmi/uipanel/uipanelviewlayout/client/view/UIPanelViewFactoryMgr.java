package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewFactoryMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelViewFactoryMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static String UIPanelViewPanel		= "UIPanelViewPanel";
	public static String UIPanelViewSchematic	= "UIPanelViewSchematic";
	public static String UIPanelViewEmpty		= "UIPanelViewEmpty";
	
	public UIWidget_i getMainPanel(String uiPanel, UINameCard uiNameCard) {
		final String function = "getPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getPanel uiPanel[{}]", uiPanel);
		
		UIWidget_i uiWidget_i = null;
		
		if ( 0 == uiPanel.compareTo(UIPanelViewPanel) ) {
			
			uiWidget_i = new UIPanelViewPanel();
			
		} else if ( 0 == uiPanel.compareTo(UIPanelViewSchematic) ) {
			
			uiWidget_i = new UIPanelViewSchematic();

		} else if ( 0 == uiPanel.compareTo(UIPanelViewEmpty) ) {
			
			uiWidget_i = new UIPanelViewEmpty();
			
		}
		
		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else {
			logger.warn(className, function, "uiPanel[{}] uiWidget_i IS NULL", uiPanel);
		}

		logger.end(className, function);

		return uiWidget_i;
		
	}
}
