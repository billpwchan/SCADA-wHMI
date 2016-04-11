package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspector;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelMenus;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIScreenMMI implements UIScreen_i {

	private static Logger logger = Logger.getLogger(UIScreenMMI.class.getName());

	private static final String menubarWidth = "160px";

	public static final int BUTTON_WIDTH	= 128;
	public static final int BUTTON_HIGHT	= 30;
	public static final int FUNTION_WIDTH	= 128;
	public static final int IMG_BTN_WIDTH	= 45;

	public static final int EAST_WIDTH		= 160;
	public static final int SOUTH_HIGHT		= 50;
	public static final int WEST_WIDTH		= 160;
	public static final int NORTH_HIGHT		= 200;

	public static final String UNIT_PX		= "px";
	
	private UINameCard uiNameCard;
	
	private String strUIScreenMMI = "UIScreenMMI.xml";

	private UILayoutGeneric uiPanelGeneric = null;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.init(strUIScreenMMI);
		
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel(uiNameCard);
		
		//Start the Navigation Menu
		logger.log(Level.SEVERE, "getMainPanel Start the Navigation Menu Begin");
		UIPanelNavigation uiPanelNavigation = UIPanelNavigation.getInstance();
		if ( null != uiPanelNavigation ) {
			UIPanelMenus uiPanelmenu = uiPanelNavigation.getMenus(this.uiNameCard);
			if ( null != uiPanelmenu ) {
				uiPanelmenu.readyToGetMenu("", "", 0, "");						
			} else {
				logger.log(Level.SEVERE, "getMainPanel uiPanelmenu IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "getMainPanel uiPanelNavigation IS NULL");
		}
		logger.log(Level.SEVERE, "getMainPanel Start the Navigation Menu End");

		return complexPanel;
	}
	
	void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

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
							taskLaunchYes.setUiPath(":UIGws:UIPanelScreen");
							taskLaunchYes.setUiPanel("UIScreenLogin");

							DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
							UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
							uiDialgogMsg.setUINameCard(this.uiNameCard);
							uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OKCANCEL, "Logout",
									"Are you sure to logout the current HMI?", taskLaunchYes, null);
							uiDialgogMsg.popUp();

						} else if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIPanelInspector")) {

							UIPanelInspector uiPanelInspector = new UIPanelInspector();
							uiPanelInspector.show();
						}
					}
				}
			}

		}
		logger.log(Level.FINE, "onUIEvent End");

	}

}
