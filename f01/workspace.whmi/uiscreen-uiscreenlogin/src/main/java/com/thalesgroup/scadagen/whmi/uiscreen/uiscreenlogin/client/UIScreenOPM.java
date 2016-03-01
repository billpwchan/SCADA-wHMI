package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIScreenOPM implements UIScreen_i {

	private static Logger logger = Logger.getLogger(UIScreenOPM.class.getName());

	public static final String UNIT_PX		= "px";
	public static final int LAYOUT_BORDER	= 1;	
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";
	
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	private String EMPTY					= "";
	
	private ListBox listBoxProfile			= new ListBox();
	private TextBox passwordTextBox1		= new TextBox();
	private TextBox passwordTextBox2		= new TextBox();
	
	private String strSave					= "Save";
    private String strCancel				= "Exit";
    private String [] strBtns				= new String[] { strSave, strCancel};
    private Button [] buttons				= new Button[strBtns.length];
	
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


		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		
		VerticalPanel verticalPanel = new VerticalPanel();
	    verticalPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
	    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		

		HorizontalPanel horizontalPanelBar;
		
		FlexTable flexTable = new FlexTable();
		
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("320px");
		horizontalPanelBar.setHeight("5px");
		verticalPanel.add(horizontalPanelBar);
		flexTable.setWidget(0, 1, horizontalPanelBar);
		
		
		flexTable.setWidget(1, 0, new InlineLabel("Select Operator:"));
		listBoxProfile.setWidth("100%");
		listBoxProfile.setHeight("100%");
		listBoxProfile.setVisibleItemCount(1);
		for (String s : OpmAuthentication.getInstance().getOperators() ) {
			listBoxProfile.addItem(s);
		}
		flexTable.setWidget(1, 1, listBoxProfile);
		
		flexTable.setWidget(2, 0, new InlineLabel("Password"));
		passwordTextBox1.setMaxLength(16);
		passwordTextBox1.setWidth("100%");
		flexTable.setWidget(2, 1, passwordTextBox1);
		
		flexTable.setWidget(3, 0, new InlineLabel("New Password (min 8)"));
		passwordTextBox2.setMaxLength(16);
		passwordTextBox2.setWidth("100%");
		flexTable.setWidget(3, 1, passwordTextBox2);

		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("128px");
		horizontalPanelBar.setHeight("10px");
		verticalPanel.add(horizontalPanelBar);
		flexTable.setWidget(4, 0, horizontalPanelBar);
		
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("320px");
		horizontalPanelBar.setHeight("10px");
		verticalPanel.add(horizontalPanelBar);
		flexTable.setWidget(4, 1, horizontalPanelBar);
		
		verticalPanel.add(flexTable);
		
		HorizontalPanel bottomButtonBar = new HorizontalPanel();
		bottomButtonBar.setWidth("100%");
	    bottomButtonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    bottomButtonBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

	    for ( int i = 0 ; i < strBtns.length ; ++i ) {
	    	buttons[i] = new Button(strBtns[i]);
			buttons[i].setWidth("128px");
			buttons[i].addStyleName("project-gwt-button");
			
			buttons[i].addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button button = (Button)event.getSource();
					String buttonText = button.getText();
					String profile = listBoxProfile.getValue(listBoxProfile.getSelectedIndex());
					String password1 = passwordTextBox1.getText();
					String password2 = passwordTextBox2.getText();
					verify(buttonText, profile, password1, password2);
				}
			});
			
			bottomButtonBar.add(buttons[i]);
			
	    }
		
		verticalPanel.add(bottomButtonBar);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHeight("100%");
		horizontalPanel.setWidth("100%");
	    horizontalPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
	    horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    horizontalPanel.add(verticalPanel);

		logger.log(Level.FINE, "getMainPanel Adding Main to base...");
		dockLayoutPanel.add(horizontalPanel);

		// Update Root level Menu
		logger.log(Level.FINE, "getMainPanel setMenu...");


		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}
	
	enum VerifyReason {
		UNKNOW
		, PASSWORD_NULL
		, PASSWORD_EMPTY
		, PASSWORD_INVALID
		, NEWPASSWORD_NULL
		, NEWPASSWORD_EMPTY
		, NEWPASSWORD_CONTAIN_SPACE
		, NEWPASSWORD_TOO_SHORT
		, NEWPASSWORD_TOO_LONG
		, PASSWORD_SAME_AS_NEWPASSWORD
		, SUCCESS
	};
	
	private void verify ( String buttonText, String operator, String password, String newpassword ) {
		if ( null != buttonText ) {
			if ( 0 == buttonText.compareToIgnoreCase(strSave) ) {
				
				// Change Password
				VerifyReason result = VerifyReason.UNKNOW;
				
				OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();

				if ( null == password ) {
					result = VerifyReason.PASSWORD_NULL;
				} else if ( 0 == password.compareTo(EMPTY) ) {
					result = VerifyReason.PASSWORD_EMPTY;
				} else if ( ! opmAuthentication.isValidPassword(operator, password) ) {
					result = VerifyReason.PASSWORD_INVALID;	
				} else if ( null == newpassword ) {
					result = VerifyReason.NEWPASSWORD_NULL;
				} else if ( 0 == newpassword.compareTo(EMPTY) ) {
					result = VerifyReason.NEWPASSWORD_EMPTY;
				} else if ( -1 != newpassword.indexOf(' ') ) {
					result = VerifyReason.NEWPASSWORD_CONTAIN_SPACE;
				} else if ( newpassword.length() < 8 ) {
					result = VerifyReason.NEWPASSWORD_TOO_SHORT;
				} else if ( newpassword.length() > 16 ) {
					result = VerifyReason.NEWPASSWORD_TOO_LONG;
				} else if ( 0 == password.compareTo(newpassword) ) {
					result = VerifyReason.PASSWORD_SAME_AS_NEWPASSWORD;
				} else {
					result = VerifyReason.SUCCESS;
				}
				
				String title = "Undefined title";
				String message = "Undefined message";
				
				switch ( result ) {
				case PASSWORD_NULL:
				case PASSWORD_EMPTY:
				case PASSWORD_INVALID:
				{
					title = "Invalid password!";
					message = "Invalid Password, operation of change password aborted, no change in the password!";
				}
					break;
					
				case NEWPASSWORD_NULL:
				case NEWPASSWORD_EMPTY:
				{
					title = "Invalid new password!";
					message = "Invalid New Password, operation of change password aborted, no change in the password!";
				}
					break;
					
				case NEWPASSWORD_CONTAIN_SPACE:
				{
					title = "Invalid new password!";
					message = "New Password contain space char, operation of change password aborted, no change in the password!";
				}
					break;
					
				case PASSWORD_SAME_AS_NEWPASSWORD:
				{
					title = "Password same as New password!";
					message = "Password same as New Password, operation of change password aborted, no change in the password!";
				}
					break;
					
				case NEWPASSWORD_TOO_SHORT:
				{
					title = "Too short of the new password!";
					message = "New Password length is less then 8, operation of change password aborted, no change in the password!";
				}
					break;
					
				case NEWPASSWORD_TOO_LONG:
				{
					title = "Too long of the new password!";
					message = "New Password length is more then 16, operation of change password aborted, no change in the password!";
				}
					break;
				
				case SUCCESS:
				{
					title = "New password applied!";
					message = "Change Password of "+operator+" successfull!";

					opmAuthentication.setPassword(operator, newpassword);
				}
					break;
					
				case UNKNOW:
				default:
				{
					title = "Unknow Error!";
					message = "Unknow Error, operation of change password aborted, no change in the password!";
				}
					break;
				}
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OK, title, message, null, null);
				uiDialgogMsg.popUp();
				
				
			} else if ( 0 == buttonText.compareToIgnoreCase(strCancel) ) {
				
				// Back to main screen
				UITaskLaunch taskLaunchYes = new UITaskLaunch();
				taskLaunchYes.setTaskUiScreen(this.uiNameCard.getUiScreen());
				taskLaunchYes.setUiPath(":UIGws:UIPanelScreen");
				taskLaunchYes.setUiPanel("UIScreenLogin");

				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OKCANCEL, "Logout",
						"Are you sure logout?", taskLaunchYes, null);
				uiDialgogMsg.popUp();
				
			}			
		}

	}

	private void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		if (null != uiEvent) {
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

//					if (UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide)) {
//
//						UITaskLaunch taskLaunch = (UITaskLaunch) taskProvide;
//
//						logger.log(Level.FINE, "onUIEvent taskLaunch.getUiPanel()[" + taskLaunch.getUiPanel() + "]");
//
//						if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIDialogMsg")) {
//
//							UITaskLaunch taskLaunchYes = new UITaskLaunch();
//							taskLaunchYes.setTaskUiScreen(this.uiNameCard.getUiScreen());
//							taskLaunchYes.setUiPath(":UIGws:UIPanelScreen");
//							taskLaunchYes.setUiPanel("UIScreenLogin");
//
//							UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
//							uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OKCANCEL, "Logout",
//									"Are you sure to login the current HMI?", taskLaunchYes, null);
//							uiDialgogMsg.popUp();
//
//						} else if (0 == taskLaunch.getUiPanel().compareToIgnoreCase("UIPanelInspector")) {
//
//							UIPanelInspector uiPanelInspector = new UIPanelInspector();
//							uiPanelInspector.show();
//						}
//					}
				}
			}

		}
		logger.log(Level.FINE, "onUIEvent End");

	}

}
