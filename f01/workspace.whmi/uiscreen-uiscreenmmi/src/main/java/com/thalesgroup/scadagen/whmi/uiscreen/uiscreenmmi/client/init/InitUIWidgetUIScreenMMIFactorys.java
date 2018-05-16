package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelEmpty;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelSoundServerController;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelStatusBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class InitUIWidgetUIScreenMMIFactorys {
	
	private final static String name = InitUIWidgetUIScreenMMIFactorys.class.getName();
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(InitUIWidgetUIScreenMMIFactorys.class.getName());
	
	public static void init() {
		String function = "init";
		logger.begin(function);
		
		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		uiWidgetMgr.clearUIWidgetFactorys();
		uiWidgetMgr.addUIWidgetFactory(name, new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElement
					, String uiDict
					, Map<String, Object> options) {
				final String function = "getUIWidget";
				
				logger.debug(function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});
				
				if ( null != uiNameCard) {
					logger.debug(function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(function, "uiNameCard IS NULL");
				}
				
				if ( null == options ) {
					logger.warn(function, "options IS NULL[{}]", null == options);
				}
				
				UIWidget_i uiWidget_i = null;
				
				if ( null != uiCtrl) {

					if ( 
							UIPanelSoundServerController.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UIPanelSoundServerController();

					}
					else if ( 
							UIPanelAccessBar.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UIPanelAccessBar();

					}
					else if ( 
							UIPanelStatusBar.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UIPanelStatusBar();

					}
					else if ( 
							UIPanelViewLayout.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UIPanelViewLayout();

					}
					else if ( 
							UIPanelEmpty.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UIPanelEmpty();

					}
					else if ( 
							UILayoutEntryPoint.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UILayoutEntryPoint();
						
					}
					else if ( 
							UILayoutSummary.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = new UILayoutSummary();

					}
					else if ( 
							UIPanelNavigation.class.getSimpleName().equals(uiCtrl) ) {
						
						uiWidget_i = UIPanelNavigation.getInstance();
						
					}
					
					if ( null != uiWidget_i ) {
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setElement(uiElement);
						uiWidget_i.setDictionaryFolder(uiDict);
						uiWidget_i.setViewXMLFile(uiView);
						uiWidget_i.setOptsXMLFile(uiOpts);
						uiWidget_i.init();
					} else {
						logger.warn(function, "uiCtrl[{}] uiWidget_i IS NULL", uiCtrl);
					}

				} else {
					logger.warn(function, "getUIWidget widget IS NULL");
				}
				
				logger.debug(function, "getUIWidget uiWidget[{}]", uiWidget_i);
	
				return uiWidget_i;
			}
		});
		
		logger.end(function);
	}
}
