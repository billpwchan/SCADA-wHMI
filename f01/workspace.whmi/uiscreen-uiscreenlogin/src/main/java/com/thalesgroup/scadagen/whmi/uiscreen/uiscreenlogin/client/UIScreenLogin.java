package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.security.AuthenticationError;
import com.thalesgroup.scadagen.whmi.opm.authentication.client.OpmAuthentication;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenLogin_i.Attribute;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIScreenLogin extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String UIPathUIPanelScreen	= ":UIGws:UIPanelScreen";

	private String strUIPanelLoginInfo			= "UIPanelLoginInfo";
	private String strUIPanelLoginButton		= "UIPanelLoginButton";
	
	private UIWidget_i uiPanelGenericInfo		= null;
	private UIWidget_i uiPanelGenericButton		= null;

	private String EMPTY						= "";
	
    private UILayoutGeneric uiLayoutGeneric		= null;
    
    private void onButtonEvent(Widget widget) {
    	final String function = "onButtonEvent";
    	
		TextBox textbox = (TextBox)uiPanelGenericInfo.getWidget(Attribute.name.toString());
		if ( null != textbox ) {
			String operator = textbox.getText();
			logger.info(className, function, "operator[{}]", operator);
			if ( null != operator ) {
				OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
				String profile = opmAuthentication.getProfile(operator);
				if ( null == profile ) {
					profile = EMPTY;
				}

				ListBox listbox = (ListBox)uiPanelGenericInfo.getWidget(Attribute.profile.toString());
				if ( null != listbox ) {
					listbox.clear();
					listbox.addItem(profile);
					listbox.setSelectedIndex(0);
				} else {
					logger.warn(className, function, "element[{}] IS NULL", profile);
				}
			}
		}
    }
    
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiPanelGenericInfo		= uiLayoutGeneric.getUIWidget(strUIPanelLoginInfo);
		uiPanelGenericButton	= uiLayoutGeneric.getUIWidget(strUIPanelLoginButton);
		
		if ( null != uiPanelGenericInfo ) {
			
			uiPanelGenericInfo.setUIWidgetEvent(new UIWidgetEventOnValueChangeHandler() {
				
				@Override
				public void setUIWidgetEventOnValueChangeHandler(ValueChangeEvent<String> event) {
					onButtonEvent(null);
				}
			});
			
		} else {
			logger.warn(className, function, "uiPanelGenericInfo IS NULL");
		}
	
		if ( null != uiPanelGenericButton ) {
			uiPanelGenericButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
				
				@Override
				public void onClickHandler(ClickEvent event) {
					String function = "onClickHandler";
					
					Widget widget = (Widget) event.getSource();
					String element = uiPanelGenericButton.getWidgetElement(widget);
					if ( null != element ) {
						if ( Attribute.login.equalsName(element)
								|| Attribute.changepassword.equalsName(element)
								) {
							// Operation: Login
							TextBox txtOperator = (TextBox)uiPanelGenericInfo.getWidget(Attribute.name.toString());
							ListBox lstProfile = (ListBox)uiPanelGenericInfo.getWidget(Attribute.profile.toString());
							PasswordTextBox txtpwdPassword = (PasswordTextBox)uiPanelGenericInfo.getWidget(Attribute.password.toString());
							
							if ( null == txtOperator ) {
								logger.warn(className, function, "setUIPanelGenericEvent onClickHandler widget element[{}] IS NULL", Attribute.name.toString());
							} else if ( null == lstProfile ) {
								logger.warn(className, function, "setUIPanelGenericEvent onClickHandler widget element[{}] IS NULL", Attribute.profile.toString());
							} else if ( null == txtpwdPassword) {
								logger.warn(className, function, "setUIPanelGenericEvent onClickHandler widget element[{}] IS NULL", Attribute.password.toString());
							} else {
								String operator		= txtOperator.getText();
								String profile		= lstProfile.getValue(lstProfile.getSelectedIndex());
								String password		= txtpwdPassword.getText();
								
								logger.info(className, function, "operator[{}] profile[{}] password[{}]", new Object[]{operator, profile, password});
								
								OpmMgr.getInstance("UIOpmSCADAgen").login(operator, password);
							}
						} 						
					} else {
						logger.warn(className, function, "button IS NULL");
					}
				}
			});
			
		} else {
			logger.warn(className, function, "uiPanelGenericButton IS NULL");
		}
		
		handleErrorCode();
		
		logger.end(className, function);
	}

	private int handleErrorCode() {
		final String function = "handleErrorMessage";
		logger.begin(className, function);
		
		int code = 0;
		
		String authErrCode = Window.Location.getParameter("autherr");
		if ((authErrCode != null) && (!authErrCode.isEmpty())) {
			
			String title	= "&uiscreenlogin_error_messagebox_title_autherr" + "_" + authErrCode;
			String message	= "&uiscreenlogin_error_messagebox_content_autherr" + "_" + authErrCode;
			
			title = Translation.getDBMessage(title);
			message = Translation.getDBMessage(message);
			
			DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
			UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
			uiDialgogMsg.setUINameCard(this.uiNameCard);
			uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, title, message, null, null);
			uiDialgogMsg.popUp();
			
		}
		
		logger.begin(className, function);
		
		return code;
	}
	
	private void verify(String element, String operator, String profile, String password) {
		final String function = "verify";
		
		logger.begin(className, function);
		
		OpmAuthentication opmAuthentication = OpmAuthentication.getInstance();
		
		if ( Attribute.login.equalsName(element) ) {
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
				uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
				uiTaskLaunch.setUiPanel("UIScreenMMI");
				uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));

			} else {
				
				DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
				UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
				uiDialgogMsg.setUINameCard(this.uiNameCard);
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_ERR, "Invalid Login Name or Password", "Please check the Login name and Password is correct!", null, null);
				uiDialgogMsg.popUp();
				
			}
		} else if ( Attribute.cancel.equalsName(element) ) {
			
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
				uiTaskLaunch.setUiPath(UIPathUIPanelScreen);
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
		
		logger.end(className, function);
	}
}
