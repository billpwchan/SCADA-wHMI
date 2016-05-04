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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspector;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIScreenMMI implements UIScreen_i {

	private static Logger logger = Logger.getLogger(UIScreenMMI.class.getName());
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";

	private UINameCard uiNameCard;
	
	private String strUIScreenMMI = "UIScreenMMI.xml";

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
		
		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.init(strUIScreenMMI);
		
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel(this.uiNameCard);
		
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

							String scsEnvId = "B001";
							String dbaddress = ":SITE1:B001:F001:ACCESS:DO001";
							
							UIInspectorTab_i uiPanelInspector = new UIPanelInspector();
							uiPanelInspector.getMainPanel(this.uiNameCard);
							((DialogBox)uiPanelInspector).show();
							
							// Header Connect
							uiPanelInspector.setParent(dbaddress);
							uiPanelInspector.setAddresses(scsEnvId, new String[]{dbaddress});
							uiPanelInspector.connect();
							
						}
					}
				}
			}

		}
		logger.log(Level.FINE, "onUIEvent End");

	}

}
