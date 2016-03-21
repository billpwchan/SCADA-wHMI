package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIScreenLogin implements UIScreen_i {
	
	private static Logger logger = Logger.getLogger(UIScreenLogin.class.getName());
	
	public static final String UNIT_PX		= "px";
	
	public static final String IMAGE_PATH	= "imgs";

	private static final String COMPANY_LOGO 	= "logologinmtr.jpg";
	private static final String CLIENT_LOGO 	= "logo_thales.jpg";
	
	private String EMPTY							= "";
	
    final String strLogin 			= "Login";
    final String strChangePassword	= "Change Password";
    final String strCancel			= "Cancel";
	
	private TextBox txtOperator;
	private ListBox listBoxProfile;
	private PasswordTextBox passwordTextBox;
	
	private UINameCard uiNameCard;
	private DockLayoutPanel dockLayoutPanel;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard){
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
	    String [] strLogins			= new String[] {
	    		strLogin, strChangePassword, strCancel
	    };
	    
	    String [] strButtonCSS 		= new String [] {
	    		"project-gwt-button-login-login", "project-gwt-button-login-changepassword", "project-gwt-button-login-cancel"
	    };
		
		HorizontalPanel horizontalPanelBar = null;
		
		txtOperator = new TextBox();
		listBoxProfile = new ListBox();
		passwordTextBox = new PasswordTextBox();
		Button buttons[];
		
		dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.addStyleName("project-gwt-panel-login");
		
		VerticalPanel verticalPanel = new VerticalPanel();
	    verticalPanel.addStyleName("project-gwt-panel-login");
	    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		String basePath		= GWT.getModuleBaseURL();
	
		verticalPanel.add(	new Image(basePath + "/" + IMAGE_PATH + "/logo/" + COMPANY_LOGO));
		
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("128px");
		horizontalPanelBar.setHeight("10px");
		verticalPanel.add(horizontalPanelBar);
		
		verticalPanel.add(	new Image(basePath + "/" + IMAGE_PATH + "/logo/" + CLIENT_LOGO));
		
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("128px");
		horizontalPanelBar.setHeight("10px");
		verticalPanel.add(horizontalPanelBar);
		
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("128px");
		horizontalPanelBar.setHeight("10px");
		verticalPanel.add(horizontalPanelBar);
		
		FlexTable flexTable = new FlexTable();
				
		horizontalPanelBar = new HorizontalPanel();
		horizontalPanelBar.setWidth("320px");
		horizontalPanelBar.setHeight("5px");
		verticalPanel.add(horizontalPanelBar);
		flexTable.setWidget(0, 1, horizontalPanelBar);
		
		flexTable.setWidget(1, 0, new InlineLabel("Operator:"));
		txtOperator.setText(OpmAuthentication.getInstance().getDefaultOperator());
		txtOperator.setMaxLength(16);
		txtOperator.setWidth("100%");
		flexTable.setWidget(1, 1, txtOperator);
		
		txtOperator.addKeyPressHandler(new KeyPressHandler() {
		    @Override
		    public void onKeyPress(KeyPressEvent event) {
				String operator = txtOperator.getText();
				logger.log(Level.FINE, "onKeyPress operator["+operator+"]");
				verifyOperator(operator);
		    }
		});
		
		flexTable.setWidget(2, 0, new InlineLabel("Profile:"));
		listBoxProfile.setWidth("100%");
		listBoxProfile.setHeight("100%");
		listBoxProfile.setVisibleItemCount(1);
		listBoxProfile.addItem(EMPTY);
		flexTable.setWidget(2, 1, listBoxProfile);
		
		flexTable.setWidget(3, 0, new InlineLabel("Password:"));
		passwordTextBox.setText(OpmAuthentication.getInstance().getDefaultPassword());
		passwordTextBox.setMaxLength(16);
		passwordTextBox.setWidth("100%");
		flexTable.setWidget(3, 1, passwordTextBox);

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
	    
	    buttons = new Button[strLogins.length];
	    for ( int i = 0 ; i < strLogins.length ; ++i ) {
			buttons[i] = new Button(strLogins[i]);
			buttons[i].addStyleName(strButtonCSS[i]);
			
			buttons[i].addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button button = (Button)event.getSource();
					if ( 0 == button.getText().compareToIgnoreCase(strLogin) 
							|| 0 == button.getText().compareToIgnoreCase(strChangePassword) ) {
						// Operation: Login
						String buttonText	= button.getText();
						String operator		= txtOperator.getText();
						String profile		= listBoxProfile.getValue(listBoxProfile.getSelectedIndex());
						String password		= passwordTextBox.getText();
						
						verify(buttonText, operator, profile, password);
					}
				}
			});
			
			bottomButtonBar.add(buttons[i]);
			
	    }
		
		verticalPanel.add(bottomButtonBar);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHeight("100%");
		horizontalPanel.setWidth("100%");
		horizontalPanel.addStyleName("project-gwt-panel-login");
	    horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    horizontalPanel.add(verticalPanel);
		
		dockLayoutPanel.add(horizontalPanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return dockLayoutPanel;
	}
	
	private void verifyOperator(String operator) {
		if ( null != operator ) {
			OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
			String profile = opmAuthentication.getProfile(operator);
			if ( null == profile ) {
				profile = EMPTY;
			}
			this.listBoxProfile.clear();
			this.listBoxProfile.addItem(profile);
			this.listBoxProfile.setSelectedIndex(0);
		}
	}
	
	private void verify(String btnText, String operator, String profile, String password) {
		
		logger.log(Level.FINE, "verify Begin");
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		
		if ( 0 == btnText.compareToIgnoreCase(strLogin) ) {
			
			if ( 0 == profile.compareTo(EMPTY) ) {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Profile", "Please check the Login name and Profile is correct!", null, null);
				uiDialgogMsg.popUp();
				
			} else if ( opmAuthentication.isValidPassword(operator, password) ) { 
				
				opmAuthentication.setCurrentOperator(operator);
				opmAuthentication.setCurrentProfile(profile);
				
				UITaskLaunch uiTaskLaunch = new UITaskLaunch();
				uiTaskLaunch.setTaskUiScreen(0);
				uiTaskLaunch.setUiPath(":UIGws:UIPanelScreen");
				uiTaskLaunch.setUiPanel("UIScreenMMI");
				uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));

			} else {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Login Name of Password", "Please check the Login name and Password is correct!", null, null);
				uiDialgogMsg.popUp();
				
			}
		} else if ( 0 == btnText.compareToIgnoreCase(strChangePassword) ) {
			
			if ( ! opmAuthentication.hasRight("M", "PASSWORD", profile) ) {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Profile to change password", "Only administator profile can change password!", null, null);
				uiDialgogMsg.popUp();
				
			} else if ( opmAuthentication.isValidPassword(operator, password) ) {
				
				opmAuthentication.setCurrentOperator(operator);
				opmAuthentication.setCurrentProfile(profile);
				
				UITaskLaunch uiTaskLaunch = new UITaskLaunch();
				uiTaskLaunch.setTaskUiScreen(0);
				uiTaskLaunch.setUiPath(":UIGws:UIPanelScreen");
				uiTaskLaunch.setUiPanel("UIScreenOPM");
				uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));
				
			} else {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
//				UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Login Name or Password", "Please check the Login name and Password is correct!", null, null);
				uiDialgogMsg.popUp();
				
			}
		}
		
		logger.log(Level.FINE, "verify End");
	}
	
	
}
