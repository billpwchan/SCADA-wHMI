package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorConnectionBox;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBox;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAlarmBanner;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAlarmBannerList;
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
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private String strUIScreenMMI = "UIScreenMMI.xml";

	private UILayoutGeneric uiPanelGeneric = null;
	
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
		
		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		
//		uiWidgetMgr.clearUIWidgetFactorys();
		uiWidgetMgr.addUIWidgetFactory("UIScreenMMI", new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String widget, String view, UINameCard uiNameCard, HashMap<String, Object> options) {
				final String function = "getUIWidget";
				
				logger.info(className, function, "widget[{}] view[{}]", widget, view);
				
				if ( null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				
				if ( null == options ) {
					logger.warn(className, function, "options IS NULL[{}]", null == options);
				}
				
				UIWidget_i uiWidget_i = null;
				
				if ( null != widget) {
					
					if ( widget.startsWith("WidgetFactory") ) {
						
						String params [] = widget.split("_");
						
						if ( params[1].equals("UINavigationMenu") ) {
							
							uiWidget_i = UIPanelNavigation.getInstance();
							
							logger.info(className, function, "getUIWidget widget[{}] widgetType[{}] menuLevel[{}] menuType[{}]", new Object[]{widget, params[1], params[2], params[3]});
								
							uiWidget_i.setParameter("menuLevel", params[2]);
							uiWidget_i.setParameter("menuType", params[3]);
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.getMainPanel();
						}
						
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelSoundServerController.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelSoundServerController();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelAccessBar.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelAccessBar();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelAlarmBanner.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelAlarmBanner();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelStatusBar.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelStatusBar();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelAlarmBannerList.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelAlarmBannerList();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if (  UIWidgetUtil.getClassSimpleName(
							UIPanelViewLayout.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelViewLayout();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( UIWidgetUtil.getClassSimpleName(
							UIPanelEmpty.class.getName()).equals(widget) ) {
						uiWidget_i = new UIPanelEmpty();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					}
				} else {
					logger.error(className, function, "getUIWidget widget IS NULL");
				}
				
				logger.info(className, function, "getUIWidget uiWIdget[{}]", uiWidget_i);

				return uiWidget_i;
			}
		});

		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.setUINameCard(this.uiNameCard);
		uiPanelGeneric.setXMLFile(strUIScreenMMI);
		uiPanelGeneric.init();
		
		rootPanel = uiPanelGeneric.getMainPanel();
		
		//Start the Navigation Menu
		logger.info(className, function, "Start the Navigation Menu Begin");
		
		UIPanelNavigation.getInstance().getMenus(this.uiNameCard).readyToGetMenu("", "", 0, "");
		
		logger.end(className, function);
	}
	
	void onUIEvent(UIEvent uiEvent) {
		
		final String function = "onUIEvent";

		logger.begin(className, function);

		if (null != uiEvent) {
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if ( taskProvide instanceof UITaskLaunch ) {

						UITaskLaunch taskLaunch = (UITaskLaunch) taskProvide;

						logger.info(className, function, "taskLaunch.getUiPanel()[{}]", taskLaunch.getUiPanel());

						if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIDialogMsg")) {

							UITaskLaunch taskLaunchYes = new UITaskLaunch();
							taskLaunchYes.setTaskUiScreen(this.uiNameCard.getUiScreen());
							taskLaunchYes.setUiPath(UIPathUIPanelScreen);
							taskLaunchYes.setUiPanel("UIScreenLogin");

							DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
							UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
							uiDialgogMsg.setUINameCard(this.uiNameCard);
							uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OKCANCEL, "Logout",
									"Are you sure to logout the current HMI?", taskLaunchYes, null);
							uiDialgogMsg.popUp();

						} else if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIPanelInspector")) {
							
							
							String hvid = null;
							if ( taskLaunch.getOption().length > 0 && null != taskLaunch.getOption()[0] ) {
								Object obj = taskLaunch.getOption()[0];
								if ( obj instanceof String ) {
									hvid		= (String)obj;
								} else {
									logger.warn(className, function, "hvid IS NOT A STRING");
								}
							}
							
							int mouseX			= -1;
							if ( taskLaunch.getOption().length > 1 && null != taskLaunch.getOption()[1] ) {
								Object obj = taskLaunch.getOption()[1];
								if ( obj instanceof Integer ) {
									mouseX		= (Integer)obj;
								} else {
									logger.warn(className, function, "mouseX IS NOT A Integer");
								}
							}
							
							int mouseY			= -1;
							if ( taskLaunch.getOption().length > 2 && null != taskLaunch.getOption()[2] ) {
								Object obj = taskLaunch.getOption()[2];
								if ( obj instanceof Integer ) {
									mouseY		= (Integer)obj;
								} else {
									logger.warn(className, function, "mouseY IS NOT A Integer");
								}
							}
							
							String period	= "250";
							
							DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIInspectorPanel");
							if ( null != dictionariesCache ) {
								period = dictionariesCache.getStringValue("inspectorpanel.properties", "inspectorpanel.periodMillis");
							}
							
							if ( taskLaunch.getOption().length > 3 && null != taskLaunch.getOption()[3] ) {
								Object obj = taskLaunch.getOption()[3];
								if ( obj instanceof String ) {
									period		= (String)obj;
								} else {
									logger.error(className, function, "period IS NOT A STRING");
								}
							}

							String scsEnvId		= hvid;
							String dbaddress	= hvid;

							String dbaddresses	= null;
							String hvides[] = hvid.split("_");
							if ( null != hvides ) {
//								if ( hvides.length >= 1 && null != hvides[0] ) {
//									scsEnvId = hvides[0];
//								}
								dbaddresses = "";
								for ( int i = 1 ; i < hvides.length ; i++ ) {
									if ( dbaddresses.length() > 0 ) {
										dbaddresses += "_";
									}
									dbaddresses += hvides[i];
								}
								if ( dbaddresses.length() > 0 ) {
									
									int s = 0, n = 0;
									
									s = n;
									n = s+3;
									String dbaddress0	= dbaddresses.substring(s, n);
									
									s = n;
									n = s+3;
									String dbaddress1	= dbaddresses.substring(s, n);
									
									s = n;
									n = s+3;
									String dbaddress2	= dbaddresses.substring(s);
									
									dbaddress	= ":" + dbaddress0 + ":" + dbaddress1 + ":" + dbaddress2;
								}
								
							}
					
							logger.info(className, function, "hvid[{}]", hvid);
							
							logger.info(className, function, "dbaddress[{}]", dbaddress);
							
							UIPanelInspectorDialogBox uiInspectorDialogbox = new UIPanelInspectorDialogBox();
							
							uiInspectorDialogbox.setUINameCard(this.uiNameCard);
							uiInspectorDialogbox.init();
							uiInspectorDialogbox.getMainPanel();
							
							uiInspectorDialogbox.setMousePosition(mouseX, mouseY);
							uiInspectorDialogbox.show();
							
							uiInspectorDialogbox.setParent(scsEnvId, dbaddress);
							
							uiInspectorDialogbox.setPeriod(period);
											
							uiInspectorDialogbox.connect();
							
						} else if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIInspectorConnectionBox")) { 
							
							UIInspectorConnectionBox uiInspectorConnectionBox = new UIInspectorConnectionBox();
							uiInspectorConnectionBox.getMainPanel(this.uiNameCard);
							uiInspectorConnectionBox.show();
						}
					}
				}
			}

		}
		
		logger.end(className, function);

	}
}
