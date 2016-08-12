package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorConnectionBox;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBox;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
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

public class UIScreenMMI extends UIWidget_i {

	private Logger logger = Logger.getLogger(UIScreenMMI.class.getName());
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";
	
	private String strUIScreenMMI = "UIScreenMMI.xml";

	private UILayoutGeneric uiPanelGeneric = null;
	
	@Override
	public void init() {
		
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
			public UIWidget_i getUIWidget(String widget) {
				logger.log(Level.FINE, "getUIWidget Begin");
				logger.log(Level.FINE, "getUIWidget widget["+widget+"]");
				
				UIWidget_i uiWidget_i = null;
				
				if ( null != widget) {
					
					if ( widget.startsWith("WidgetFactory") ) {
						
						String params [] = widget.split("_");
						
						if ( params[1].equals("UINavigationMenu") ) {
							
							uiWidget_i = UIPanelNavigation.getInstance();
							
							logger.log(Level.SEVERE, "getMainPanel widget["+widget+"] widgetType["+params[1]+"] menuLevel["+params[2]+"] menuType["+params[3]+"]");
								
							uiWidget_i.setParameter("menuLevel", params[2]);
							uiWidget_i.setParameter("menuType", params[3]);
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.getMainPanel();
						}
						
					} else if ( widget.equals("UIPanelSoundServerController") ) {
						uiWidget_i = new UIPanelSoundServerController();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelAccessBar") ) {
						uiWidget_i = new UIPanelAccessBar();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelAlarmBanner") ) {
						uiWidget_i = new UIPanelAlarmBanner();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelStatusBar") ) {
						uiWidget_i = new UIPanelStatusBar();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelAlarmBannerList") ) {
						uiWidget_i = new UIPanelAlarmBannerList();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelViewLayout") ) {
						uiWidget_i = new UIPanelViewLayout();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					} else if ( widget.equals("UIPanelEmpty") ) {
						uiWidget_i = new UIPanelEmpty();
						uiWidget_i.setUINameCard(uiNameCard);
						uiWidget_i.setXMLFile(widget);
						uiWidget_i.init();
					}
				} else {
					logger.log(Level.SEVERE, "getUIWidget widget IS NULL");
				}
				

				logger.log(Level.FINE, "getUIWidget uiWIdget["+uiWidget_i+"]");
				logger.log(Level.FINE, "getUIWidget End");
				
				return uiWidget_i;
			}
		});

		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.setUINameCard(this.uiNameCard);
		uiPanelGeneric.setXMLFile(strUIScreenMMI);
		uiPanelGeneric.init();
		
		rootPanel = uiPanelGeneric.getMainPanel();
		
		//Start the Navigation Menu
		logger.log(Level.FINE, "getMainPanel Start the Navigation Menu Begin");
		
		UIPanelNavigation.getInstance().getMenus(this.uiNameCard).readyToGetMenu("", "", 0, "");
	}
	
	void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		if (null != uiEvent) {
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if ( taskProvide instanceof UITaskLaunch ) {

						UITaskLaunch taskLaunch = (UITaskLaunch) taskProvide;

						logger.log(Level.FINE, "onUIEvent taskLaunch.getUiPanel()[" + taskLaunch.getUiPanel() + "]");

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
									logger.log(Level.SEVERE, "onUIEvent hvid IS NOT A STRING");
								}
							}
							
							int mouseX			= -1;
							if ( taskLaunch.getOption().length > 1 && null != taskLaunch.getOption()[1] ) {
								Object obj = taskLaunch.getOption()[1];
								if ( obj instanceof Integer ) {
									mouseX		= (Integer)obj;
								} else {
									logger.log(Level.SEVERE, "onUIEvent mouseX IS NOT A Integer");
								}
							}
							
							int mouseY			= -1;
							if ( taskLaunch.getOption().length > 2 && null != taskLaunch.getOption()[2] ) {
								Object obj = taskLaunch.getOption()[2];
								if ( obj instanceof Integer ) {
									mouseY		= (Integer)obj;
								} else {
									logger.log(Level.SEVERE, "onUIEvent mouseY IS NOT A Integer");
								}
							}
							
							String period	= "250";
							if ( taskLaunch.getOption().length > 3 && null != taskLaunch.getOption()[3] ) {
								Object obj = taskLaunch.getOption()[3];
								if ( obj instanceof String ) {
									period		= (String)obj;
								} else {
									logger.log(Level.SEVERE, "onUIEvent period IS NOT A STRING");
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
					
							logger.log(Level.SEVERE, "onUIEvent hvid["+hvid+"]");
							
							logger.log(Level.SEVERE, "onUIEvent dbaddress["+dbaddress+"]");
							
							UIPanelInspectorDialogBox uiInspectorDialogbox = new UIPanelInspectorDialogBox();
							
							uiInspectorDialogbox.setUINameCard(this.uiNameCard);
							uiInspectorDialogbox.init(null);
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
		logger.log(Level.FINE, "onUIEvent End");

	}
}
