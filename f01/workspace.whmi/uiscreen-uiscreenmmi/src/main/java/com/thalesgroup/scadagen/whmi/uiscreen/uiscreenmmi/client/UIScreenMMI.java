package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
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
import com.thalesgroup.scadagen.whmi.uipanel.uipanelalarmbanner.client.UIPanelAlarmBanner;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelMenus;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelstatusbar.client.UIPanelStatusBar;
import com.thalesgroup.scadagen.whmi.uipanel.uipaneltoolbar.client.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;

public class UIScreenMMI implements UIScreen_i {

	private static Logger logger = Logger.getLogger(UIScreenMMI.class.getName());

	public static final String CUSTOMER_LOGIN_NAME = "Logo_Corp-300dpi.bg_grey.15per.jpg";
	public static final String THALES_LOGIN_NAME = "logo_thales.jpg";
	
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
	
	public static final String IMAGE_PATH	= "imgs";
	
	private UIPanelMenus uiPanelmenu;
	private UINameCard uiNameCard;

	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {

		logger.log(Level.FINE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});

		this.uiPanelmenu = new UIPanelMenus(this.uiNameCard);

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);

		// North Bar
		logger.log(Level.FINE, "getMainPanel North Bar Begin");

		DockLayoutPanel statusBar = new UIPanelStatusBar().getMainPanel(this.uiNameCard);

		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.addStyleName("project-gwt-panel-north");

		dockLayoutPanel.addNorth(statusBar, 42);

		DockLayoutPanel alarmBanner = new UIPanelAlarmBanner().getMainPanel(this.uiNameCard);
		alarmBanner.setWidth("100%");
		alarmBanner.setHeight("100%");
		dockLayoutPanel.add(alarmBanner);

		logger.log(Level.FINE, "getMainPanel North Bar End");
		// End of North

		// South Bar
		logger.log(Level.FINE, "getMainPanel South Bar Begin");
		// LV2, SYS
		HorizontalPanel menuBarLv1 = uiPanelmenu.getHorizontalMenu(1);
		menuBarLv1.setWidth(menubarWidth);
		menuBarLv1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		menuBarLv1.addStyleName("project-gwt-panel-south");

		Panel accessBar = new UIPanelAccessBar().getMainPanel(this.uiNameCard);

		SplitLayoutPanel splitLayoutPanelSouth = new SplitLayoutPanel(0);
		splitLayoutPanelSouth.addStyleName("project-gwt-panel-south");
		splitLayoutPanelSouth.addLineStart(menuBarLv1, BUTTON_WIDTH * 20);
		splitLayoutPanelSouth.addLineEnd(accessBar, ( UIScreenMMI.IMG_BTN_WIDTH * 9 ) + 10);

		logger.log(Level.FINE, "getMainPanel South Bar End");
		// End of South

		// East Bar
		logger.log(Level.FINE, "getMainPanel East Bar Begin");

		VerticalPanel verticalPanelEast = new VerticalPanel();
		verticalPanelEast.setWidth(menubarWidth);
		verticalPanelEast.setHeight("100%");
		verticalPanelEast.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanelEast.addStyleName("project-gwt-panel-east");

		// LV1, STN
		FlowPanel menuBarLv0 = uiPanelmenu.getFlowMenu(0);
		verticalPanelEast.add(menuBarLv0);

		logger.log(Level.FINE, "getMainPanel East Bar End");
		// End of East Bar

		// West Bar
		logger.log(Level.FINE, "getMainPanel West Bar Begin");
		// LV3, SubSys/Fun
		VerticalPanel menuBarLv2 = uiPanelmenu.getVerticalMenu(2);
		menuBarLv2.setWidth(menubarWidth);
		menuBarLv2.addStyleName("paddedHorizontalPanel");
		menuBarLv2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		// LV4, SubSys/Fun
		VerticalPanel menuBarLv3 = uiPanelmenu.getVerticalMenu(3);
		menuBarLv3.setWidth(menubarWidth);
		menuBarLv3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel verticalPanelWest = new VerticalPanel();
		verticalPanelWest.setWidth(menubarWidth);
		verticalPanelWest.setHeight("100%");
		verticalPanelWest.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanelWest.addStyleName("project-gwt-panel-west");

		verticalPanelWest.add(menuBarLv2);
		verticalPanelWest.add(menuBarLv3);

		logger.log(Level.FINE, "getMainPanel East Bar End");
		// End of West

		// Main Area
		logger.log(Level.FINE, "getMainPanel Main Area Begin");

		DockLayoutPanel manAreaPanel = new UIPanelViewLayout().getMainPanel(this.uiNameCard);

		logger.log(Level.FINE, "getMainPanel Main Area End");
		// End of Main Area

		logger.log(Level.FINE, "getMainPanel Adding North to base...");
		basePanel.addNorth(dockLayoutPanel, 		NORTH_HIGHT);

		logger.log(Level.FINE, "getMainPanel Adding West to base...");
		basePanel.addWest(verticalPanelWest, 		WEST_WIDTH);

		logger.log(Level.FINE, "getMainPanel Adding South to base...");
		basePanel.addSouth(splitLayoutPanelSouth, 	SOUTH_HIGHT);

		logger.log(Level.FINE, "getMainPanel Adding East to base...");
		basePanel.addEast(verticalPanelEast, 		EAST_WIDTH);

		logger.log(Level.FINE, "getMainPanel Adding Main to base...");
		basePanel.add(manAreaPanel);

		// Update Root level Menu
		logger.log(Level.FINE, "getMainPanel setMenu...");

		uiPanelmenu.readyToGetMenu("", "", 0, "");

		logger.log(Level.FINE, "getMainPanel End");

		return basePanel;
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
