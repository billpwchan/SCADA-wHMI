package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
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

	private final String stroldpassvalue	= "oldpassvalue";
	private final String strnewpassvalue	= "newpassvalue";
	private final String strnewpassvalue2	= "newpassvalue2";
	
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
						
						HashMap<String, HashMap<String, Object>> override = null;
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new ExecuteAction_i() {
							
							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								// TODO Auto-generated method stub
								String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
								
								logger.info(className, function, "os1[{}]", os1);
								
								boolean bContinue = true;
								
								if ( null != os1 ) {
									
									if ( os1.equals("CheckNewPasswordIsEmpty") ) {
										
										String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
										
										logger.info(className, function, "strnewpassvalue[{}]", strnewpassvalue);
										logger.info(className, function, "newpassvalue[{}]", newpassvalue);
										
										if ( null != newpassvalue && newpassvalue.trim().length() > 0 ) {
											// Valid
										} else {
											
											uiEventActionProcessor_i.executeActionSet("set_result_newpassword_is_empty");
											
											bContinue = false;
										}
									} else if ( os1.equals("CheckRetypePasswordIsMatch") ) {

										String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
										String newpassvalue2	= uiWidgetGeneric.getWidgetValue(strnewpassvalue2);
										
										logger.info(className, function, "strnewpassvalue[{}]", strnewpassvalue);
										logger.info(className, function, "newpassvalue[{}]", newpassvalue);
										
										logger.info(className, function, "strnewpassvalue2[{}]", strnewpassvalue2);
										logger.info(className, function, "newpassvalue2[{}]", newpassvalue2);
										
										if ( null != newpassvalue && null != newpassvalue2 && newpassvalue.equals(newpassvalue2) ) {
											// Valid
										} else {
											
											uiEventActionProcessor_i.executeActionSet("set_result_retypepassword_is_not_match");
											
											bContinue = false;
										}
									} else if ( os1.equals("SendChangePasswordControl") ) {
										
										String uiopmapivalue	= opmApi;
										String oldpassvalue		= uiWidgetGeneric.getWidgetValue(stroldpassvalue);
										String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
										
										logger.info(className, function, "stroldpassvalue[{}]", stroldpassvalue);
										logger.info(className, function, "oldpassvalue[{}]", oldpassvalue);
										
										logger.info(className, function, "strnewpassvalue[{}]", strnewpassvalue);
										logger.info(className, function, "newpassvalue[{}]", newpassvalue);
										
										UIOpm_i uiOpm_i = OpmMgr.getInstance(uiopmapivalue);
										
										uiOpm_i.changePassword(uiopmapivalue, oldpassvalue, newpassvalue, new UIWrapperRpcEvent_i() {
			
											@Override
											public void event(JSONObject jsobject) {
												
												String function = null;
												String resultinstanceof = null;
												
												if ( null != jsobject ) {
													JSONValue v = null;
													
													v = jsobject.get("function");
													if ( null != v ) {
														JSONString s = v.isString();
														if ( null != s ) {
															function = s.stringValue();
														}
													}
													
													v = jsobject.get("resultinstanceof");
													if ( null != v ) {
														JSONString s = v.isString();
														if ( null != s ) {
															resultinstanceof = s.stringValue();
														}
													}
													
												}
												
												if ( null != function 
														&& null != resultinstanceof
														&& function.equalsIgnoreCase("onSuccessMwt") 
														&& resultinstanceof.equalsIgnoreCase("OperatorActionReturn") ) {
													
													uiEventActionProcessor_i.executeActionSet("set_result_value_valid");
												} else {
													uiEventActionProcessor_i.executeActionSet("set_result_value_invalid");
												}
											}
											
										});
									}
								}
								return bContinue;
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

}
