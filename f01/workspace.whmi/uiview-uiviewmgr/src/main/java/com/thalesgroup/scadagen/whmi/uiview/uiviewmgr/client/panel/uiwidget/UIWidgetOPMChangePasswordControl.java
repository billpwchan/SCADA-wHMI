package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;

public class UIWidgetOPMChangePasswordControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetOPMChangePasswordControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private SimpleEventBus eventBus 	= null;
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private String opmApi = null;
	private String stropmapi = "opmapi";
	private String strHeader = "header";

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String function = "onClick";
			
			logger.begin(className, function);
			
			if ( null != event ) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(className, function, "element[{}]", element);
					if ( null != element ) {
						
						
						String actionsetkey = element;
						uiEventActionProcessor_i.executeActionSet(actionsetkey, new ExecuteAction_i() {
							
							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								// TODO Auto-generated method stub
								String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
								
								logger.info(className, function, "os1[{}]", os1);
								
								if ( null != os1 ) {
									if ( os1.equals("SendChangePasswordControl") ) {
										
										String uiopmapivalue	= opmApi;
										String oldpassvalue		= uiWidgetGeneric.getWidgetValue("oldpassvalue");
										String newpassvalue		= uiWidgetGeneric.getWidgetValue("newpassvalue");
										
										UIOpm_i uiOpm_i = OpmMgr.getInstance(uiopmapivalue);
										
										uiOpm_i.changePassword(uiopmapivalue, oldpassvalue, newpassvalue, new UIWrapperRpcEvent_i() {
			
											@Override
											public void CallbackEvent(String result, String detail) {
												
												if ( result.equals("valid") ) {
													
													uiEventActionProcessor_i.executeActionSet("set_result_value_valid");

												} else {
													
													uiEventActionProcessor_i.executeActionSet("set_result_value_invalid");

												}
												
											}
			
											@Override
											public void CallbackEvent(String api, String rpcapi, String rpcReturnFunction,
													String resultType, String message1, String message2, String message3) {
												// TODO Auto-generated method stub
												
											}
											
										});
									}
								}
								return true;
							}
						});

					}
				}
			}
			
			logger.begin(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);

			logger.end(className, function);
		}
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			opmApi = dictionariesCache.getStringValue(optsXMLFile, stropmapi, strHeader);
		}

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIWidgetGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
					}
				}
			})
		);

		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(className, function);
	}
	
	/*
	private void verify ( String buttonText, String operator, String password, String newpassword ) {
		final String function = "verify";
		
		logger.begin(className, function);
		
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
				uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OK, title, message, null, null);
				uiDialgogMsg.popUp();
				
				
			} else if ( 0 == buttonText.compareToIgnoreCase(strCancel) ) {
				
				// Back to main screen
				UITaskLaunch taskLaunchYes = new UITaskLaunch();
				taskLaunchYes.setTaskUiScreen(this.uiNameCard.getUiScreen());
				taskLaunchYes.setUiPath(UIPathUIPanelScreen);
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

		logger.end(className, function);
	}
	*/

}
