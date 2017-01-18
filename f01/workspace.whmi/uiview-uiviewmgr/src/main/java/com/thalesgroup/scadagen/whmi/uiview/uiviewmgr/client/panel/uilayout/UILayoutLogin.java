package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UILayoutLogin extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private String strUIWidgetLoginInfo			= "UIWidgetLoginInfo";
	private String strUIWidgetLoginButton		= "UIWidgetLoginButton";
	
	private UIWidget_i uiWidgetGenericInfo		= null;
	private UIWidget_i uiWidgetGenericButton	= null;
	
	private String opmApi					= null;
	private final String strOpmApi			= "OpmApi";
	private final String strHeader			= "header";
	
	private final String strname			= "name";
	private final String strpassword		= "password";
	
	// External
	private SimpleEventBus eventBus = null;

	private UILayoutGeneric uiLayoutGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			String function = "onUIEvent";
			logger.begin(className, function);
			logger.end(className, function);
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			String function = "onClick";
			logger.begin(className, function);
			
			logger.info(className, function, "event[{}]", event);
			
			Widget widget = (Widget) event.getSource();
			
			logger.info(className, function, "widget[{}]", widget);
			
			String element = uiWidgetGenericButton.getWidgetElement(widget);
			
			logger.info(className, function, "element[{}]", element);
			
			if ( null != element ) {
				
				String actionsetkey = element;
				
				HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();

				HashMap<String, Object> parameters = new HashMap<String, Object>();
				
				String operator = uiWidgetGenericInfo.getWidgetValue(strname);
				String password = uiWidgetGenericInfo.getWidgetValue(strpassword);
					
				logger.info(className, function, "update Counter Values");
					
				logger.info(className, function, "opmApi[{}]", opmApi);
				logger.info(className, function, "operator[{}]", operator);
				logger.info(className, function, "password[{}]", password);
					
				if ( null != opmApi && null != operator && null != password ) {
					parameters.put(ActionAttribute.OperationString2.toString(), opmApi);
					parameters.put(ActionAttribute.OperationString3.toString(), operator);
					parameters.put(ActionAttribute.OperationString4.toString(), password);
				} else {
					logger.warn(className, function, "opmApi[{}] OR operator[{}] OR password[{}] IS INVALID", new Object[]{opmApi, operator, password});
				}
				
				override.put("OpmLogin", parameters);
					
				uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
				
			} else {
				logger.warn(className, function, "element IS NULL");
			}
			
			logger.end(className, function);
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
			opmApi = dictionariesCache.getStringValue(optsXMLFile, strOpmApi, strHeader);
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
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGenericButton.setCtrlHandler(uiWidgetCtrl_i);

//		if ( null != uiWidgetGenericButton ) {
//			uiWidgetGenericButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
//				
//				@Override
//				public void onClickHandler(ClickEvent event) {
//					if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
//				}
//			});
//			
//		} else {
//			logger.warn(className, function, "uiPanelGenericButton IS NULL");
//		}
//		
//		handlerRegistrations.add(
//			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
//				@Override
//				public void onEvenBusUIChanged(UIEvent uiEvent) {
//					if ( uiEvent.getSource() != this ) {
//						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
//					}
//				}
//			})
//		);
//
//		handlerRegistrations.add(
//			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
//				@Override
//				public void onAction(UIEventAction uiEventAction) {
//					if ( uiEventAction.getSource() != this ) {
//						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
//					}
//				}
//			})
//		);

		uiEventActionProcessor_i.executeActionSetInit();
		
		// Show the error id from the URL for the login failed
		// Which come from the Spring framework
		handleErrorCode();
		
		logger.end(className, function);
	}

	// Display the login invalid message 
	private int handleErrorCode() {
		final String function = "handleErrorMessage";
		logger.begin(className, function);
		
		int code = 0;
		
		String authErrCode = Window.Location.getParameter("autherr");
		if ((authErrCode != null) && (!authErrCode.isEmpty())) {
			
			uiEventActionProcessor_i.executeActionSet("set_result_value_invalid");
			
		}
		
		logger.begin(className, function);
		
		return code;
	}
	
}
