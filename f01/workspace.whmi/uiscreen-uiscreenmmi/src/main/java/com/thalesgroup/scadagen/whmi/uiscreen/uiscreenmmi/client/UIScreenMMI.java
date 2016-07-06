package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorConnectionBox;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspector;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
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

public class UIScreenMMI implements UIScreen_i {

	private static Logger logger = Logger.getLogger(UIScreenMMI.class.getName());
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";

	private UINameCard uiNameCard = null;
	
	private String strUIScreenMMI = "UIScreenMMI.xml";
	
	public UINameCard getUINameCard() { return this.uiNameCard; }

	private UILayoutGeneric uiPanelGeneric = null;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});
		
		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		
		uiWidgetMgr.clearUIWidgetFactorys();
		uiWidgetMgr.addUIWidgetFactory("UIScreenMMI", new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String widget) {
				logger.log(Level.FINE, "getUIWidget Begin");
				logger.log(Level.FINE, "getUIWidget widget["+widget+"]");
				
				UIWidget_i uiWidget = null;
				
				if ( null != widget) {
					
					if ( widget.startsWith("WidgetFactory") ) {
						
						String params [] = widget.split("_");
						
						if ( params[1].equals("UINavigationMenu") ) {
							
							uiWidget = UIPanelNavigation.getInstance();
							
							logger.log(Level.SEVERE, "getMainPanel widget["+widget+"] widgetType["+params[1]+"] menuLevel["+params[2]+"] menuType["+params[3]+"]");
								
							uiWidget.setParameter("menuLevel", params[2]);
							uiWidget.setParameter("menuType", params[3]);
							uiWidget.setUINameCard(getUINameCard());
							uiWidget.getMainPanel();
						}
						
					} else if ( widget.equals("UIPanelSoundServerController") ) {
						uiWidget = new UIPanelSoundServerController();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelAccessBar") ) {
						uiWidget = new UIPanelAccessBar();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelAlarmBanner") ) {
						uiWidget = new UIPanelAlarmBanner();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelStatusBar") ) {
						uiWidget = new UIPanelStatusBar();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelAlarmBannerList") ) {
						uiWidget = new UIPanelAlarmBannerList();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelViewLayout") ) {
						uiWidget = new UIPanelViewLayout();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					} else if ( widget.equals("UIPanelEmpty") ) {
						uiWidget = new UIPanelEmpty();
						uiWidget.setUINameCard(getUINameCard());
						uiWidget.init(widget);
					}
				} else {
					logger.log(Level.SEVERE, "getUIWidget widget IS NULL");
				}
				

				logger.log(Level.FINE, "getUIWidget uiWIdget["+uiWidget+"]");
				logger.log(Level.FINE, "getUIWidget End");
				
				return uiWidget;
			}
		});

		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.setUINameCard(this.uiNameCard);
		uiPanelGeneric.init(strUIScreenMMI);
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel();
		
		//Start the Navigation Menu
		logger.log(Level.FINE, "getMainPanel Start the Navigation Menu Begin");
		
		UIPanelNavigation.getInstance().getMenus(this.uiNameCard).readyToGetMenu("", "", 0, "");

		return complexPanel;
	}
	
	void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");
		
//		if ( null != uiNameCard ) {
//			logger.log(Level.SEVERE, "onUIEvent uiNameCard.getUiScreen()["+uiNameCard.getUiScreen()+"]");
//			logger.log(Level.SEVERE, "onUIEvent uiNameCard.getUiPath()["+uiNameCard.getUiPath()+"]");
//		} else {
//			logger.log(Level.SEVERE, "onUIEvent uiNameCard IS NULL");
//		}
//		
//		if ( null != uiEvent ) {
//			logger.log(Level.SEVERE, "onUIEvent uiEvent.getTaskProvide().getTaskUiScreen()["+uiEvent.getTaskProvide().getTaskUiScreen()+"]");
//			logger.log(Level.SEVERE, "onUIEvent uiEvent.getTaskProvide().getUiPath()["+uiEvent.getTaskProvide().getUiPath()+"]");
//		} else {
//			logger.log(Level.SEVERE, "onUIEvent uiEvent IS NULL");
//		}

		if (null != uiEvent) {
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					if (UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide)) {

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
							
							String hvid			= taskLaunch.getOption()[0];
							
							String period		= "250";
							if ( taskLaunch.getOption().length >= 2 && null != taskLaunch.getOption()[1] ) {
								period		= taskLaunch.getOption()[1];
							}
							
							String scsEnvId		= null;
							String dbaddress	= null;

							String dbaddresses	= null;
							String hvides[] = hvid.split("_");
							if ( null != hvides ) {
								if ( hvides.length >= 1 && null != hvides[0] ) {
									scsEnvId = hvides[0];
								}
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
									
//							Window.alert("dbaddress0["+dbaddress0+"] dbaddress1["+dbaddress1+"] dbaddress2["+dbaddress2+"] dbaddress3["+dbaddress3+"]");
							
							logger.log(Level.SEVERE, "onUIEvent hvid["+hvid+"]");
							
							logger.log(Level.SEVERE, "onUIEvent dbaddress["+dbaddress+"]");
							
							UIInspectorTab_i uiPanelInspector = new UIPanelInspector();
							uiPanelInspector.setParent(dbaddress);
							uiPanelInspector.setAddresses(scsEnvId, new String[]{dbaddress}, period);
							uiPanelInspector.getMainPanel(this.uiNameCard);
							((DialogBox)uiPanelInspector).show();
							uiPanelInspector.connect();
							
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
