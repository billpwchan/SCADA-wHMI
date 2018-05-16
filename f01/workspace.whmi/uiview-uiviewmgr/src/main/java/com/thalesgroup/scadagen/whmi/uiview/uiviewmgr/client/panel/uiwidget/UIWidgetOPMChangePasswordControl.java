package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;

public class UIWidgetOPMChangePasswordControl extends UIWidgetRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private String opmApi = null;
	private String fillCurrentOperator = null;
	
	private String strOpmApi = "OpmApi";
	private String strFillCurrentOperator = "FillCurrentOperator";
	private String strHeader = "header";

	private final String stroperatorvalue	= "operatorvalue";
	private final String stroldpassvalue	= "oldpassvalue";
	private final String strnewpassvalue	= "newpassvalue";
	private final String strnewpassvalue2	= "newpassvalue2";
	
	@Override
	public void init() {
		super.init();
		
		final String f = "init";
		logger.begin(f);

		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			opmApi = dictionariesCache.getStringValue(optsXMLFile, strOpmApi, strHeader);
			fillCurrentOperator = dictionariesCache.getStringValue(optsXMLFile, strFillCurrentOperator, strHeader);
		}
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String f = "onClick";
				logger.begin(f);
				
				if ( null != event ) {
					final Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						final String element = uiGeneric.getWidgetElement(widget);
						logger.debug(f, "element[{}]", element);
						if ( null != element ) {
							
							final String actionsetkey = element;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, null, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {

									final String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.debug(f, "os1[{}]", os1);
									
									boolean bContinue = true;
									
									if ( null != os1 ) {
										
										final String operatorvalue		= uiGeneric.getWidgetValue(stroperatorvalue);
										
										logger.debug(f, "stroperatorvalue[{}] operatorvalue[{}]", stroperatorvalue, operatorvalue);
										
										if ( os1.equals("CheckOperatorIsEmpty") ) {

											if ( null != operatorvalue && operatorvalue.trim().length() > 0 ) {
												// Valid
											} else {
												
												promptUser("set_result_operator_is_empty", operatorvalue);
												
												bContinue = false;
											}
										} else if ( os1.equals("CheckNewPasswordIsEmpty") ) {
											
											final String newpassvalue		= uiGeneric.getWidgetValue(strnewpassvalue);
											
											if ( null != newpassvalue && newpassvalue.trim().length() > 0 ) {
												// Valid
											} else {
												
												promptUser("set_result_newpassword_is_empty", operatorvalue);
												
												bContinue = false;
											}
										} else if ( os1.equals("CheckRetypePasswordIsMatch") ) {

											final String newpassvalue		= uiGeneric.getWidgetValue(strnewpassvalue);
											final String newpassvalue2	= uiGeneric.getWidgetValue(strnewpassvalue2);
											
											if ( null != newpassvalue && null != newpassvalue2 && newpassvalue.equals(newpassvalue2) ) {
												// Valid
											} else {
												
												promptUser("set_result_retypepassword_is_not_match", operatorvalue);
												
												bContinue = false;
											}
										} else if ( os1.equals("SendChangePasswordControl") ) {
											
											final String uiopmapivalue	= opmApi;
											
											final OpmMgr opmMgr = OpmMgr.getInstance();
											final UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
											
											// Operator
											final String oldpassvalue		= uiGeneric.getWidgetValue(stroldpassvalue);
											final String newpassvalue		= uiGeneric.getWidgetValue(strnewpassvalue);
											
											uiOpm_i.changePassword(operatorvalue, oldpassvalue, newpassvalue, new UIWrapperRpcEvent_i() {
				
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

														promptUser("set_result_value_valid", operatorvalue);
														
													} else {
														
														promptUser("set_result_value_invalid", operatorvalue);
														
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
				
				logger.end(f);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String f = "onActionReceived";
				logger.beginEnd(f);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String f = "init";
				logger.beginEnd(f);
			}
		
			@Override
			public void envUp(String env) {
				final String f = "envUp";
				logger.beginEnd(f);
			}
			
			@Override
			public void envDown(String env) {
				final String f = "envDown";
				logger.beginEnd(f);
			}
			
			@Override
			public void terminate() {
				final String f = "terminate";
				logger.begin(f);
				envDown(null);
				logger.end(f);
			};
		};
		
		if ( null != fillCurrentOperator ) {
			if ( fillCurrentOperator.equalsIgnoreCase(String.valueOf(true))) {
				
				final String actionsetkey = "SetCurrentOperator";
				final String actionkey = "SetCurrentOperator";
				
				final Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();

				final Map<String, Object> parameters = new HashMap<String, Object>();

				final String uiopmapivalue	= opmApi;
				
				final OpmMgr opmMgr = OpmMgr.getInstance();
				final UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
				
				final String operator = uiOpm_i.getCurrentOperator();
					
				logger.debug(f, "Set Operator Value operator[{}]", operator);
					
				if ( null != operator ) {
					parameters.put(ActionAttribute.OperationString3.toString(), operator);
				} else {
					logger.warn(f, "operator IS NULL");
				}
				
				override.put(actionkey, parameters);
					
				uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
				
			} else {
				logger.debug(f, "fillCurrentOperator[{}] IS FALSE");
			}
		} else {
			logger.warn(f, "fillCurrentOperator IS NULL");
		}
		
		logger.end(f);
	}
	
	private void promptUser(final String actionSetKey, final String user) {
		final String f = "promptUser";
		logger.begin(f);
		
		final String actionKey = actionSetKey + "_event";
		logger.debug(f, "actionSetKey[{}] actionKey[{}] user[{}]", new Object[]{actionSetKey, actionKey, user});
		
		final String STR_LOGIN_USR = "%CURRENT_LOGIN_USER%";
		final String STR_CUR_USR = "%USER%";
		
		final String uiopmapivalue	= opmApi;
		
		final OpmMgr opmMgr = OpmMgr.getInstance();
		final UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		final String operator = uiOpm_i.getCurrentOperator();
		
		UIEventAction uiEventAction = uiEventActionProcessor_i.getUIEventActionMgr(actionKey);
		logger.debug(f, "uiEventAction[{}]", uiEventAction);
		String msg = (String)uiEventAction.getParameter(ActionAttribute.OperationString9.toString());
		logger.debug(f, "msg[{}]", msg);
		
		msg = msg.replace(STR_LOGIN_USR, operator);
		logger.debug(f, "msg[{}] STR_LOGIN_USR[{}] operator[{}]", new Object[]{msg, STR_LOGIN_USR, operator});
		msg = msg.replace(STR_CUR_USR, user);
		logger.debug(f, "msg[{}] STR_CUR_USR[{}] user[{}]", new Object[]{msg, STR_CUR_USR, user});
		
		final Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString9.toString(), msg);
		
		final Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
		override.put(actionKey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionSetKey, override);
		
		logger.end(f);
	}

}
