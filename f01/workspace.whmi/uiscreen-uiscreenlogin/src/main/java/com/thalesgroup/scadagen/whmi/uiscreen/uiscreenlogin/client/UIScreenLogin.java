package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreen.client.UIScreen_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.generic.uipanel.UIPanelGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidget_i;

public class UIScreenLogin implements UIScreen_i {
	
	private static Logger logger = Logger.getLogger(UIScreenLogin.class.getName());
	
	private String strUIPanelLogin				= "UIPanelLogin.xml";
	
	private String strUIPanelLoginInfo			= "UIPanelLoginInfo.xml";
	private String strUIPanelLoginButton		= "UIPanelLoginButton.xml";
	
	private UIWidget_i uiPanelGenericInfo	= null;
	private UIWidget_i uiPanelGenericButton	= null;

	private String EMPTY						= "";
	
    private UIPanelGeneric uiPanelGeneric		= null;
	
	private UINameCard uiNameCard;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard){
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		uiPanelGeneric = new UIPanelGeneric();
		uiPanelGeneric.init(strUIPanelLogin);
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel(this.uiNameCard);
		
		uiPanelGenericInfo		= uiPanelGeneric.getUIWidget(strUIPanelLoginInfo);
		uiPanelGenericButton	= uiPanelGeneric.getUIWidget(strUIPanelLoginButton);
		
		if ( null != uiPanelGenericInfo ) {
			uiPanelGenericInfo.setUIWidgetEvent(new UIWidgetEvent() {

				@Override
				public void onClickHandler(ClickEvent event) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onKeyPressHandler(KeyPressEvent event) {
					TextBox textbox = (TextBox)uiPanelGenericInfo.getWidget(UIScreenLogin_i.Attribute.name.toString());
					if ( null != textbox ) {
						String operator = textbox.getText();
						logger.log(Level.FINE, "getMainPanel operator["+operator+"]");
						if ( null != operator ) {
							OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
							String profile = opmAuthentication.getProfile(operator);
							if ( null == profile ) {
								profile = EMPTY;
							}

							ListBox listbox = (ListBox)uiPanelGenericInfo.getWidget(UIScreenLogin_i.Attribute.profile.toString());
							if ( null != listbox ) {
								listbox.clear();
								listbox.addItem(profile);
								listbox.setSelectedIndex(0);
							} else {
								logger.log(Level.SEVERE, "getMainPanel element["+profile+"] IS NULL");
							}
						}
					}
				}

				@Override
				public void onValueChange(String name, String value) {
					// TODO Auto-generated method stub
					
				}
			});
		} else {
			logger.log(Level.SEVERE, "getMainPanel uiPanelGeneric.get(strUIPanelLoginInfo) IS NULL");
		}
	
		if ( null != uiPanelGenericButton ) {
			uiPanelGenericButton.setUIWidgetEvent(new UIWidgetEvent() {
				
				@Override
				public void onClickHandler(ClickEvent event) {
					Widget widget = (Widget) event.getSource();
					String element = uiPanelGenericButton.getWidgetElement(widget);
					if ( null != element ) {
						if ( UIScreenLogin_i.Attribute.login.equalsName(element)
								|| UIScreenLogin_i.Attribute.changepassword.equalsName(element)
								) {
							// Operation: Login
							TextBox txtOperator = (TextBox)uiPanelGenericInfo.getWidget(UIScreenLogin_i.Attribute.name.toString());
							ListBox lstProfile = (ListBox)uiPanelGenericInfo.getWidget(UIScreenLogin_i.Attribute.profile.toString());
							PasswordTextBox txtpwdPassword = (PasswordTextBox)uiPanelGenericInfo.getWidget(UIScreenLogin_i.Attribute.password.toString());
							
							if ( null == txtOperator ) {
								logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+UIScreenLogin_i.Attribute.name.toString()+"] IS NULL");
							} else if ( null == lstProfile ) {
								logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+UIScreenLogin_i.Attribute.profile.toString()+"] IS NULL");
							} else if ( null == txtpwdPassword) {
								logger.log(Level.SEVERE, "setUIPanelGenericEvent onClickHandler widget element["+UIScreenLogin_i.Attribute.password.toString()+"] IS NULL");
							} else {
								String operator		= txtOperator.getText();
								String profile		= lstProfile.getValue(lstProfile.getSelectedIndex());
								String password		= txtpwdPassword.getText();
								
								verify(element, operator, profile, password);
							}
						} 						
					} else {
						logger.log(Level.SEVERE, "getMainPanel button IS NULL");
					}
				}
				
				@Override
				public void onKeyPressHandler(KeyPressEvent event) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onValueChange(String name, String value) {
					// TODO Auto-generated method stub
					
				}
			});
			
		} else {
			logger.log(Level.SEVERE, "getMainPanel uiPanelGeneric.get(strUIPanelLoginButton) IS NULL");
		}

		logger.log(Level.SEVERE, "getMainPanel End");
		
		return complexPanel;
	}
	
	private void verify(String element, String operator, String profile, String password) {
		logger.log(Level.FINE, "verify Begin");
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		
		if ( UIScreenLogin_i.Attribute.login.equalsName(element) ) {
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
		} else if ( UIScreenLogin_i.Attribute.cancel.equalsName(element) ) {
			
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
