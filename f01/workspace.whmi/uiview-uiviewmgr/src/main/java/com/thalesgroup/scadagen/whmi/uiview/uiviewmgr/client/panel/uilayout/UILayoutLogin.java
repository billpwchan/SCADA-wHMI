package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutLogin_i.PropertiesName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UILayoutLogin extends UIWidget_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private final String strUIWidgetGeneric		= "UIWidgetGeneric";
	
	private final String strUIWidgetLoginInfo	= "UIWidgetLoginInfo";
	private final String strUIWidgetLoginButton	= "UIWidgetLoginButton";
	
	private UIWidget_i uiWidgetGenericInfo		= null;
	private UIWidget_i uiWidgetGenericButton	= null;
	
	private String opmApi						= null;
	private String upperCaseName				= null;
	private String lowerCaseName				= null;

	private final String strHeader				= "header";
	
	private final String STR_NAME				= "name";
	private final String STR_PASSWORD			= "password";
	
	private final String STR_LOGIN				= "login";
	
	private final String STR_CHGPWD 			= "changepassword";
	
	private final String STR_URLFAULT_CODEKEY	= "autherr";
	
	private final String STR_RESULT_INVALID_PREFIX	= "set_result_value_invalid_";
	
	private void changepassword() {
		String f = "changepassword";
		logger.begin(f);
		
		// Write Cookies
		final String STR_CHANGEPASSWORD = "changepassword";
		final String changePassword = "1";
		logger.debug(f, "STR_CHANGEPASSWORD[{}] changePassword[{}]", STR_CHANGEPASSWORD, changePassword);
		UICookies.setCookies(STR_CHANGEPASSWORD, changePassword);

		// Login
		login();
		
		logger.end(f);
	}
	
	private void login() {
		String f = "login";
		logger.begin(f);
		
		final String actionsetkey = STR_LOGIN;
		
		final Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();

		final Map<String, Object> parameters = new HashMap<String, Object>();
		
		String operator = uiWidgetGenericInfo.getWidgetValue(STR_NAME);
		String password = uiWidgetGenericInfo.getWidgetValue(STR_PASSWORD);
		
		if ( null != upperCaseName && Boolean.TRUE.toString().equals(upperCaseName) ) {
			operator = operator.toUpperCase();
		}
		
		if ( null != lowerCaseName && Boolean.TRUE.toString().equals(lowerCaseName) ) {
			operator = operator.toLowerCase();
		}
			
		logger.debug(f, "opmApi[{}] operator[{}]", opmApi, operator);
			
		if ( null != opmApi && null != operator && null != password ) {
			parameters.put(ActionAttribute.OperationString2.toString(), opmApi);
			parameters.put(ActionAttribute.OperationString3.toString(), operator);
			parameters.put(ActionAttribute.OperationString4.toString(), password);
		} else {
			logger.warn(f, "opmApi[{}] OR operator[{}] OR password IS INVALID", new Object[]{opmApi, operator});
		}
		
		override.put("OpmLogin", parameters);
			
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(f);
	}
	
	// External
	private SimpleEventBus eventBus = null;

	private UILayoutGeneric uiLayoutGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			String f = "onUIEvent";
			logger.beginEnd(f);
		}
		
		@Override
		public void onClick(ClickEvent event) {
			String f = "onClick";
			logger.begin(f);
			
			logger.debug(f, "event[{}]", event);
			
			final Widget widget = (Widget) event.getSource();
			
			logger.debug(f, "widget[{}]", widget);
			
			final String element = uiWidgetGenericButton.getWidgetElement(widget);
			
			logger.debug(f, "element[{}]", element);
			
			if ( null != element ) {
				
				if ( element.equals(STR_LOGIN) ) {
					login();
				} else if ( element.equals(STR_CHGPWD) ) {
					changepassword();	
				}
				
			} else {
				logger.warn(f, "element IS NULL");
			}
			
			logger.end(f);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String f = "onActionReceived";
			logger.beginEnd(f);
		}
	};
	
	@Override
	public void init() {
		final String f = "init";
		
		logger.begin(f);
		
		final String strEventBusName = getStringParameter(UIRealize_i.ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.debug(f, "strEventBusName[{}]", strEventBusName);
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			opmApi			= dictionariesCache.getStringValue(optsXMLFile, PropertiesName.OpmApi.toString(), strHeader);
			upperCaseName	= dictionariesCache.getStringValue(optsXMLFile, PropertiesName.UpperCaseName.toString(), strHeader);
			lowerCaseName	= dictionariesCache.getStringValue(optsXMLFile, PropertiesName.LowerCaseName.toString(), strHeader);
		}
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiWidgetGenericInfo		= uiLayoutGeneric.getUIWidget(strUIWidgetLoginInfo);
		uiWidgetGenericButton	= uiLayoutGeneric.getUIWidget(strUIWidgetLoginButton);

		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessor_i.setUIGeneric((UIGeneric) uiWidgetGenericInfo);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGenericButton.setCtrlHandler(uiWidgetCtrl_i);

		uiEventActionProcessor_i.executeActionSetInit();
		
		// Show the error id from the URL for the login failed
		// Which come from the Spring framework
		handleErrorCode();
		
		// Handle the Entry Key in the Password Box
		final Widget password = uiWidgetGenericInfo.getWidget(STR_PASSWORD);
		if ( null != password ) {
			if ( password instanceof TextBox ) {
				TextBox txtpassword = (TextBox) password;
				txtpassword.addKeyUpHandler(new KeyUpHandler() {
					@Override
					public void onKeyUp(KeyUpEvent event) {
						if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ) {
							login();
						}
					}
				});
			}
		}
		
//		TextBox name = (TextBox) uiWidgetGenericInfo.getWidget(STR_NAME);
//		if (null!=name) name.setFocus(true);
		
		logger.end(f);
	}

	// Display the login invalid message 
	private void handleErrorCode() {
		final String f = "handleErrorMessage";
		logger.begin(f);
		
		// Spring Message
		final String authErrCode = Window.Location.getParameter(STR_URLFAULT_CODEKEY);
		if ((authErrCode != null) && (!authErrCode.isEmpty())) {
			final String actionset = STR_RESULT_INVALID_PREFIX+authErrCode;
			logger.debug(f, "actionset[{}]", actionset);
			uiEventActionProcessor_i.executeActionSet(actionset);
		}

		logger.end(f);
	}
	
}
