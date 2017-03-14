package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;

public class UIWidgetOPMChangePasswordControl extends UIWidgetRealize implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetOPMChangePasswordControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
		
		final String function = "init";
		logger.begin(className, function);

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
				final String function = "onClick";
				
				logger.begin(className, function);
				
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiWidgetGeneric.getWidgetElement(widget);
						logger.debug(className, function, "element[{}]", element);
						if ( null != element ) {
							
							String actionsetkey = element;
							
							HashMap<String, HashMap<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									// TODO Auto-generated method stub
									String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.debug(className, function, "os1[{}]", os1);
									
									boolean bContinue = true;
									
									if ( null != os1 ) {
										
										if ( os1.equals("CheckOperatorIsEmpty") ) {
											
											String operatorvalue		= uiWidgetGeneric.getWidgetValue(stroperatorvalue);
											
											logger.debug(className, function, "stroperatorvalue[{}] operatorvalue[{}]", stroperatorvalue, operatorvalue);
											
											if ( null != operatorvalue && operatorvalue.trim().length() > 0 ) {
												// Valid
											} else {
												
												uiEventActionProcessor_i.executeActionSet("set_result_operator_is_empty");
												
												bContinue = false;
											}
										} else if ( os1.equals("CheckNewPasswordIsEmpty") ) {
											
											String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
											
											logger.debug(className, function, "strnewpassvalue[{}] newpassvalue[{}]", strnewpassvalue, newpassvalue);
											
											if ( null != newpassvalue && newpassvalue.trim().length() > 0 ) {
												// Valid
											} else {
												
												uiEventActionProcessor_i.executeActionSet("set_result_newpassword_is_empty");
												
												bContinue = false;
											}
										} else if ( os1.equals("CheckRetypePasswordIsMatch") ) {

											String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
											String newpassvalue2	= uiWidgetGeneric.getWidgetValue(strnewpassvalue2);
											
											logger.debug(className, function, "strnewpassvalue[{}] newpassvalue[{}]", strnewpassvalue, newpassvalue);
											
											logger.debug(className, function, "strnewpassvalue2[{}] newpassvalue2[{}]", strnewpassvalue2, newpassvalue2);
											
											if ( null != newpassvalue && null != newpassvalue2 && newpassvalue.equals(newpassvalue2) ) {
												// Valid
											} else {
												
												uiEventActionProcessor_i.executeActionSet("set_result_retypepassword_is_not_match");
												
												bContinue = false;
											}
										} else if ( os1.equals("SendChangePasswordControl") ) {
											
											String uiopmapivalue	= opmApi;
											
											UIOpm_i uiOpm_i = OpmMgr.getInstance(uiopmapivalue);
											
											// Operator
											String operatorvalue	= uiWidgetGeneric.getWidgetValue(stroperatorvalue);
											String oldpassvalue		= uiWidgetGeneric.getWidgetValue(stroldpassvalue);
											String newpassvalue		= uiWidgetGeneric.getWidgetValue(strnewpassvalue);
											
											logger.debug(className, function, "stroperatorvalue[{}], operatorvalue[{}]", stroperatorvalue, operatorvalue);
											
											logger.debug(className, function, "stroldpassvalue[{}], oldpassvalue[{}]", stroldpassvalue, oldpassvalue);
											
											logger.debug(className, function, "strnewpassvalue[{}], newpassvalue[{}]", strnewpassvalue, newpassvalue);
											
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
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(className, function);
			}
		
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};
		
		if ( null != fillCurrentOperator ) {
			if ( fillCurrentOperator.equalsIgnoreCase(String.valueOf(true))) {
				
				String actionsetkey = "SetCurrentOperator";
				String actionkey = "SetCurrentOperator";
				
				HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();

				HashMap<String, Object> parameters = new HashMap<String, Object>();

				String uiopmapivalue	= opmApi;
				
				UIOpm_i uiOpm_i = OpmMgr.getInstance(uiopmapivalue);
				
				String operator = uiOpm_i.getOperator();
					
				logger.debug(className, function, "Set Operator Value operator[{}]", operator);
					
				if ( null != operator ) {
					parameters.put(ActionAttribute.OperationString3.toString(), operator);
				} else {
					logger.warn(className, function, "operator IS NULL");
				}
				
				override.put(actionkey, parameters);
					
				uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
				
			} else {
				logger.debug(className, function, "fillCurrentOperator[{}] IS FALSE");
			}
		} else {
			logger.warn(className, function, "fillCurrentOperator IS NULL");
		}
		
		logger.end(className, function);
	}

}
