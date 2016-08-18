package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.google.gwt.user.client.ui.Panel;
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

	public Panel getMainPanel(String uiPanel, UINameCard uiNameCard){
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getMainPanel uiNameCard[{}]", uiNameCard.getUiPath());
		
		UIWidget_i uiWidget_i =  this.getPanel(uiPanel, uiNameCard);
		Panel rootPanel = uiWidget_i.getMainPanel();

		logger.end(className, function);

		return rootPanel;
	}
	
	public UIWidget_i getPanel(String uiPanel, UINameCard uiNameCard) {
		final String function = "getPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getPanel uiPanel[{}]", uiPanel);
		
		UIWidget_i uiWidget_i = null;
		
		if ( 0 == uiPanel.compareTo(UIPanelViewPanel) ) {
			uiWidget_i = new UIPanelViewPanel();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo(UIPanelViewSchematic) ) {
			uiWidget_i = new UIPanelViewSchematic();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		} else if ( 0 == uiPanel.compareTo(UIPanelViewEmpty) ) {
			uiWidget_i = new UIPanelViewEmpty();
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.init();
		}

		logger.end(className, function);

		return uiWidget_i;
		
	}
}
