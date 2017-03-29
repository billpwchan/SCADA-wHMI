package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.hilc;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.hilc.HILCMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.hilc.HILCMgr_i;

public class UIWidgetVerifyHILCControl extends UIWidgetRealize {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyHILCControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";

	private HILCMgr hilcMgr				= null;
	
//	private UIGeneric uiWidgetGeneric = null;
//	private UIWidgetGeneric uiWidgetGeneric = null;
	
//	private String valueSet				= "";
//	private String valueUnSet			= "";
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		
		hilcMgr = HILCMgr.getInstance(className);
		
		hilcMgr.setHILCMgr_i(new HILCMgr_i() {

			@Override
			public void HILCPreparationResult(String clientKey, int errorCode, String errorMessage) {
				if (clientKey.equals("HILCPreparationRequest")) {
					if (errorCode == 0) {
						uiGeneric.setWidgetValue("message", "HILCPreparationRequest sent successfully");
					} else {
						uiGeneric.setWidgetValue("message", "HILCPreparationRequest failed.  " + errorMessage);
					}
				}
			}

			@Override
			public void HILCConfirmResult(String clientKey, int errorCode, String errorMessage) {
				if (clientKey.equals("HILCConfirmRequest")) {
					if (errorCode == 0) {
						uiGeneric.setWidgetValue("message", "HILCConfirmRequest sent successfully");
					} else {
						uiGeneric.setWidgetValue("message", "HILCConfirmRequest failed.  " + errorMessage);
					}
				}
			}
			
		});

//		String strUIWidgetGeneric = "UIWidgetGeneric";
//		String strHeader = "header";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
//		if ( null != dictionariesCache ) {
//			valueSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcControl_i.ParameterName.ValueSet.toString(), strHeader);
//			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcControl_i.ParameterName.ValueUnSet.toString(), strHeader);
//		}
//		
//		logger.info(className, function, "valueSet[{}]", valueSet);
//		logger.info(className, function, "valueUnSet[{}]", valueUnSet);
		
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
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							String actionsetkey = element;
							
							HashMap<String, HashMap<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.info(className, function, "os1[{}]", os1);
									
									if ( null != os1 ) {
										
										if ( os1.equals("HILCPreparationRequest") ) {
	
											String key = "HILCPreparationRequest"; 											
											String operatorName = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId(); 

											String scsEnvId = "";
											String workstationName = "";
											int cmdType = 0;
								    		int cmdValue = 0;
								    		int cmdValueDiv = 0;
								    		String eqpAlias = ""; 
								    		String eqpType = ""; 
								    		String cmdName = "";

								    		// scsEnvId
								    		String str = uiGeneric.getWidgetValue("scsEnvId");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: scsEnvId is null or empty");
								    			return false;
								    		} else {
								    			scsEnvId = str;
								    		}
								    		
								    		// workstationName
								    		str = uiGeneric.getWidgetValue("workstationName");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: workstationName is null or empty");
								    			return false;
								    		} else {
								    			workstationName = str;
								    		}
								    		
								    		// cmdType
								    		str = uiGeneric.getWidgetValue("cmdType");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdType is null or empty");
								    			return false;
								    		} else if (str.equals("2")) {
								    			cmdType = 2;	// DIO
								    		} else if (str.equals("3")) {
								    			cmdType = 3;	// AIO
								    		} else {
								    			uiGeneric.setWidgetValue("message", "Error: cmdType value not supported (2=DIO, 3=AIO");
									    		return false;
								    		}
								    		
								    		// cmdValue
								    		str = uiGeneric.getWidgetValue("cmdValue");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdValue is null or empty");
								    			return false;
								    		} else if (str.matches("\\d+")) {
							    				cmdValue = Integer.parseInt(str);
							    			} else {
							    				uiGeneric.setWidgetValue("message", "Error: cmdValue is not integer");
								    			return false;
								    		}
								    		
								    		// cmdValueDiv
								    		str = uiGeneric.getWidgetValue("cmdValueDiv");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdValueDiv is null or empty");
								    			return false;
								    		} else if (str.matches("\\d+")) {
								    			cmdValueDiv = Integer.parseInt(str);
							    			} else {
							    				uiGeneric.setWidgetValue("message", "Error: cmdValueDiv is not integer");
								    			return false;
								    		}
								    		
								    		// eqpAlias
								    		str = uiGeneric.getWidgetValue("eqpAlias");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: eqpAlias is null or empty");
								    			return false;
								    		} else {
								    			eqpAlias = str;
								    		}
								    		
								    		// eqpType
								    		str = uiGeneric.getWidgetValue("eqpType");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: eqpType is null or empty");
								    			return false;
								    		} else {
								    			eqpType = str;
								    		}
								    		
								    		// cmdName
								    		str = uiGeneric.getWidgetValue("cmdName");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdName is null or empty");
								    			return false;
								    		} else {
								    			cmdName = str;
								    		}
								    		
											hilcMgr.hilcPreparationRequest(key, scsEnvId, operatorName, workstationName, cmdType,
											    	cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);

											
										} else if ( os1.equals("HILCConfirmRequest") ) {
											
											String key = "HILCConfirmRequest"; 
											String operatorName = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId(); 

											String scsEnvId = "";
											String workstationName = "";
											int cmdType = 0;
								    		int cmdValue = 0;
								    		int cmdValueDiv = 0;
								    		String eqpAlias = ""; 
								    		String eqpType = ""; 
								    		String cmdName = "";

								    		// scsEnvId
								    		String str = uiGeneric.getWidgetValue("scsEnvId");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: scsEnvId is null or empty");
								    			return false;
								    		} else {
								    			scsEnvId = str;
								    		}
								    		
								    		// workstationName
								    		str = uiGeneric.getWidgetValue("workstationName");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: workstationName is null or empty");
								    			return false;
								    		} else {
								    			workstationName = str;
								    		}
								    		
								    		// cmdType
								    		str = uiGeneric.getWidgetValue("cmdType");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdType is null or empty");
								    			return false;
								    		} else if (str.equals("2")) {
								    			cmdType = 2;	// DIO
								    		} else if (str.equals("3")) {
								    			cmdType = 3;	// AIO
								    		} else {
								    			uiGeneric.setWidgetValue("message", "Error: cmdType value not supported (2=DIO, 3=AIO");
									    		return false;
								    		}
								    		
								    		// cmdValue
								    		str = uiGeneric.getWidgetValue("cmdValue");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdValue is null or empty");
								    			return false;
								    		} else if (str.matches("\\d+")) {
							    				cmdValue = Integer.parseInt(str);
							    			} else {
							    				uiGeneric.setWidgetValue("message", "Error: cmdValue is not integer");
								    			return false;
								    		}
								    		
								    		// cmdValueDiv
								    		str = uiGeneric.getWidgetValue("cmdValueDiv");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdValueDiv is null or empty");
								    			return false;
								    		} else if (str.matches("\\d+")) {
								    			cmdValueDiv = Integer.parseInt(str);
							    			} else {
							    				uiGeneric.setWidgetValue("message", "Error: cmdValueDiv is not integer");
								    			return false;
								    		}
								    		
								    		// eqpAlias
								    		str = uiGeneric.getWidgetValue("eqpAlias");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: eqpAlias is null or empty");
								    			return false;
								    		} else {
								    			eqpAlias = str;
								    		}
								    		
								    		// eqpType
								    		str = uiGeneric.getWidgetValue("eqpType");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: eqpType is null or empty");
								    			return false;
								    		} else {
								    			eqpType = str;
								    		}
								    		
								    		// cmdName
								    		str = uiGeneric.getWidgetValue("cmdName");
								    		if (str == null || str.isEmpty()) {
								    			uiGeneric.setWidgetValue("message", "Error: cmdName is null or empty");
								    			return false;
								    		} else {
								    			cmdName = str;
								    		}
									
											hilcMgr.hilcConfirmRequest(key, scsEnvId, operatorName, workstationName, cmdType,
										    		cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
											
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
				
				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				
				logger.info(className, function, "os1["+os1+"]");
				
				if ( null != os1 ) {
					// General Case
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe ["+oe+"]");
					logger.info(className, function, "os1["+os1+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							uiEventActionProcessor_i.executeActionSet(os1);
						}
					}
				}
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
		
		logger.end(className, function);
	}
}
