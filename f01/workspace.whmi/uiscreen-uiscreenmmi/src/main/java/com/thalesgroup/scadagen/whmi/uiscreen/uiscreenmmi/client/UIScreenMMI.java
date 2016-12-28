package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelEmpty;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelSoundServerController;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelStatusBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

/**
 * @author syau
 *
 */
public class UIScreenMMI extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenMMI.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;
	
	@Override
	public void init() {
		
		final String function = "init";
		
		logger.begin(className, function);
		
		handlerRegistrations.add(		
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					onUIEvent(uiEvent);
				}
			})
		);
		
		UIPanelNavigation.getInstance().resetInstance();
		
		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		
		uiWidgetMgr.clearUIWidgetFactorys();		
		uiWidgetMgr.addUIWidgetFactory(className, new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiDict
					, HashMap<String, Object> options) {
				final String function = "getUIWidget";
				
				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});
				
				if ( null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				
				if ( null == options ) {
					logger.warn(className, function, "options IS NULL[{}]", null == options);
				}
				
				UIWidget_i uiWidget_i = null;
				
				if ( null != uiCtrl) {
					
					if ( ! uiCtrl.startsWith("WidgetFactory") ) {
						
						if (  UIWidgetUtil.getClassSimpleName(
								UIPanelSoundServerController.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelSoundServerController();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelAccessBar.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelAccessBar();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelStatusBar.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelStatusBar();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelViewLayout.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelViewLayout();

						} else if ( UIWidgetUtil.getClassSimpleName(
								UIPanelEmpty.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelEmpty();

						} else if ( UIWidgetUtil.getClassSimpleName(
								UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UILayoutEntryPoint();
							
						} else if ( UIWidgetUtil.getClassSimpleName(
								UILayoutSummary.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UILayoutSummary();

						}
						
						if ( null != uiWidget_i ) {
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.setDictionaryFolder(uiDict);
							uiWidget_i.setViewXMLFile(uiView);
							uiWidget_i.setOptsXMLFile(uiOpts);
							uiWidget_i.init();
						} else {
							logger.warn(className, function, "uiCtrl[{}] uiWidget_i IS NULL", uiCtrl);
						}
						
					} else {
						String params [] = uiCtrl.split("_");
						
						if ( params[1].equals("UINavigationMenu") ) {
							
							uiWidget_i = UIPanelNavigation.getInstance();
							
							logger.info(className, function, "getUIWidget uiCtrl[{}] widgetType[{}] menuLevel[{}] menuType[{}]", new Object[]{uiCtrl, params[1], params[2], params[3]});
								
							uiWidget_i.setParameter("menuLevel", params[2]);
							uiWidget_i.setParameter("menuType", params[3]);
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.getMainPanel();
						}
					}
				} else {
					logger.warn(className, function, "getUIWidget widget IS NULL");
				}
				
				logger.info(className, function, "getUIWidget uiWidget[{}]", uiWidget_i);

				return uiWidget_i;
			}
		});


		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		//Start the Navigation Menu
		logger.info(className, function, "Start the Navigation Menu Begin");
		
		UIPanelNavigation.getInstance().getMenus(this.uiNameCard).readyToGetMenu("", "", 0, "");
		
		logger.end(className, function);
	}
	
	void onUIEvent(UIEvent uiEvent) {
		
//		final String function = "onUIEvent";
//
//		logger.begin(className, function);
//
//		if (null != uiEvent) {
//			UITask_i taskProvide = uiEvent.getTaskProvide();
//			if (null != taskProvide) {
//				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
//						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {
//
//					if ( taskProvide instanceof UITaskLaunch ) {
//
//						UITaskLaunch taskLaunch = (UITaskLaunch) taskProvide;
//
//						logger.info(className, function, "taskLaunch.getUiPanel()[{}]", taskLaunch.getUiPanel());
//
//					}
//				}
//			}
//
//		}
//		
//		logger.end(className, function);

	}
}
