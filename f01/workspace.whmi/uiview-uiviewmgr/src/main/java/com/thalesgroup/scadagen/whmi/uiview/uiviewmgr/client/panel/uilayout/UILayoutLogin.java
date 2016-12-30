package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.ExecuteAction_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;

public class UILayoutLogin extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private String strUIWidgetLoginInfo			= "UIWidgetLoginInfo";
	private String strUIWidgetLoginButton		= "UIWidgetLoginButton";
	
	private UIWidget_i uiWidgetGenericInfo		= null;
	private UIWidget_i uiWidgetGenericButton	= null;
	
	private String opmApi				= null;
	private String stropmapi			= "opmapi";
	private String strHeader			= "header";
	
	private String strname				= "name";
	private String strprofile			= "profile";
	private String strpassword			= "password";
	private String strlogin				= "login";
	private String strchangepassword	= "changepassword";
	private String strcancel			= "cancel";
	
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
			logger.end(className, function);
			
			Widget widget = (Widget) event.getSource();
			String element = uiWidgetGenericButton.getWidgetElement(widget);
			if ( null != element ) {
				
				
				String actionsetkey = element;
				uiEventActionProcessor_i.executeActionSet(actionsetkey, new ExecuteAction_i() {
					
					@Override
					public boolean executeHandler(UIEventAction uiEventAction) {
						String function = "executeHandler";
						
						String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
						
						logger.info(className, function, "os1[{}]", os1);
						
						if ( null != os1 ) {
							if ( os1.equals("OpmLogin") ) {
								
								String operator = uiWidgetGenericInfo.getWidgetValue(strname);
								String password = uiWidgetGenericInfo.getWidgetValue(strpassword);
								logger.info(className, function, "operator[{}] password[{}]", new Object[]{operator, password});
								
								String uiopmapivalue	= opmApi;
								OpmMgr.getInstance(uiopmapivalue).login(operator, password);

							}
						}
						return true;
					}
				});
				
				

			} else {
				logger.warn(className, function, "button IS NULL");
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
			opmApi = dictionariesCache.getStringValue(optsXMLFile, stropmapi, strHeader);
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

		if ( null != uiWidgetGenericButton ) {
			uiWidgetGenericButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
				
				@Override
				public void onClickHandler(ClickEvent event) {
					if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
				}
			});
			
		} else {
			logger.warn(className, function, "uiPanelGenericButton IS NULL");
		}
		
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
		
		handleErrorCode();
		
		logger.end(className, function);
	}

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
