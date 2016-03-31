package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIPanelGeneric;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIPanelGenericEvent;

public class UIScreenLogin implements UIScreen_i {
	
	private static Logger logger = Logger.getLogger(UIScreenLogin.class.getName());
	
	private String strUIPanelLoginLogo			= "UIPanelLoginLogo.xml";
	private UIPanelGeneric uiPanelLoginButton 	= null;
	
	private String strUIPanelLoginInfo			= "UIPanelLoginInfo.xml";
	private UIPanelGeneric uiPanelLoginInfo 	= null;
	
	private String strUIPanelLoginButton		= "UIPanelLoginButton.xml";
	private UIPanelGeneric uiPanelLoginLogo		= null;
	
	private String EMPTY						= "";
	
	private final String strName				= "name";
	private final String strProfile				= "profile";
	private final String strPassword			= "password";
	
    private final String strLogin 				= "login";
    private final String strChangePassword		= "changepassword";
    private final String strCancel				= "cancel";

	private UINameCard uiNameCard;
	private DockLayoutPanel dockLayoutPanel;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard){
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		HorizontalPanel horizontalPanelBar = null;

		dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.addStyleName("project-gwt-panel-login");
		
		VerticalPanel verticalPanel = new VerticalPanel();
	    verticalPanel.addStyleName("project-gwt-panel-login-inner");
	    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		uiPanelLoginLogo = new UIPanelGeneric();
		uiPanelLoginLogo.init(strUIPanelLoginLogo);
		verticalPanel.add(uiPanelLoginLogo.getMainPanel(this.uiNameCard));
		
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

		uiPanelLoginInfo = new UIPanelGeneric();
		uiPanelLoginInfo.init(strUIPanelLoginInfo);
		uiPanelLoginInfo.setUIPanelGenericEvent(new UIPanelGenericEvent() {
			@Override
			public void onKeyPressHandler(KeyPressEvent event) {
				TextBox textbox = (TextBox)uiPanelLoginInfo.getWidget(strName);
				if ( null != textbox ) {
					String operator = textbox.getText();
					logger.log(Level.FINE, "onKeyPress operator["+operator+"]");
					verifyOperator(operator);
				}
			}
			
			@Override
			public void onClickHandler(ClickEvent event) {
			}
		});
		verticalPanel.add(uiPanelLoginInfo.getMainPanel(this.uiNameCard));

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

		uiPanelLoginButton = new UIPanelGeneric();
		uiPanelLoginButton.init(strUIPanelLoginButton);
		uiPanelLoginButton.setUIPanelGenericEvent(new UIPanelGenericEvent() {
			
			@Override
			public void onClickHandler(ClickEvent event) {
				Widget widget = (Widget) event.getSource();
				
				String element = uiPanelLoginButton.getWidgetName(widget);
				
				if ( null != element ) {
					if ( 0 == element.compareTo(strLogin) || 0 == element.compareTo(strChangePassword) ) {
					
						// Operation: Login
						TextBox txtOperator = (TextBox)uiPanelLoginInfo.getWidget(strName);
						ListBox lstProfile = (ListBox)uiPanelLoginInfo.getWidget(strProfile);
						PasswordTextBox txtpwdPassword = (PasswordTextBox)uiPanelLoginInfo.getWidget(strPassword);
						
						if ( null == txtOperator ) {
							logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+strName+"] IS NULL");
						} else if ( null == lstProfile ) {
							logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+strProfile+"] IS NULL");
						} else if ( null == txtpwdPassword) {
							logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+strPassword+"] IS NULL");
						} else {

							String operator		= txtOperator.getText();
							String profile		= lstProfile.getValue(lstProfile.getSelectedIndex());
							String password		= txtpwdPassword.getText();
							
							verify(element, operator, profile, password);
						}
					} 						
				} else {
					logger.log(Level.SEVERE, "onClickHandler button IS NULL");
				}

			}

			@Override
			public void onKeyPressHandler(KeyPressEvent event) {
			}
		});
		verticalPanel.add(uiPanelLoginButton.getMainPanel(this.uiNameCard));
		//
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHeight("100%");
		horizontalPanel.addStyleName("project-gwt-panel-login-outer");
	    horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    horizontalPanel.add(verticalPanel);
		
		dockLayoutPanel.add(horizontalPanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return dockLayoutPanel;
	}
	
	private void verifyOperator(String operator) {
		if ( null != operator ) {
			OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
			String profile = opmAuthentication.getProfile(operator);
			if ( null == profile ) {
				profile = EMPTY;
			}

			ListBox listbox = (ListBox)uiPanelLoginInfo.getWidget(strProfile);
			if ( null != listbox ) {
				listbox.clear();
				listbox.addItem(profile);
				listbox.setSelectedIndex(0);
			} else {
				logger.log(Level.SEVERE, "verifyOperator element["+profile+"] IS NULL");
			}
		}
	}
	
	private void verify(String element, String operator, String profile, String password) {	
		logger.log(Level.FINE, "verify Begin");
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		
		
		if ( 0 == element.compareTo(strLogin) ) {
			if ( 0 == profile.compareTo(EMPTY) ) {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
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
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Login Name of Password", "Please check the Login name and Password is correct!", null, null);
				uiDialgogMsg.popUp();
				
			}
		} else if ( 0 == element.compareTo(strCancel) ) {
			
			if ( ! opmAuthentication.hasRight("M", "PASSWORD", profile) ) {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
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
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Login Name or Password", "Please check the Login name and Password is correct!", null, null);
				uiDialgogMsg.popUp();
				
			}
		}
		
		logger.log(Level.FINE, "verify End");
	}
	
	
}
