package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.ValidityStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIWidgetDpcScanSuspendControl extends UIWidgetRealize {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private DpcMgr dpcMgr				= null;
	
	private String columnAlias			= "";
	private String columnStatus			= "";
	private String columnServiceOwner	= "";
	
	private String valueSet				= "";
	private String valueUnSet			= "";
	
	private final String strSet					= "set";

	private Set<Map<String, String>> selectedSet = null;

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(function);
		
		dpcMgr = DpcMgr.getInstance(className);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcScanSuspendControl_i.ParameterName.ColumnAlias.toString(), strHeader);
			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcScanSuspendControl_i.ParameterName.ColumnStatus.toString(), strHeader);
			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcScanSuspendControl_i.ParameterName.ColumnServiceOwner.toString(), strHeader);
			valueSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcScanSuspendControl_i.ParameterName.ValueSet.toString(), strHeader);
			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcScanSuspendControl_i.ParameterName.ValueUnSet.toString(), strHeader);
		}
		
		logger.info(function, "columnAlias[{}]", columnAlias);
		logger.info(function, "columnStatus[{}]", columnStatus);
		logger.info(function, "columnServiceOwner[{}]", columnServiceOwner);
		logger.info(function, "valueSet[{}]", valueSet);
		logger.info(function, "valueUnSet[{}]", valueUnSet);
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				
				logger.begin(function);
				
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(function, "element[{}]", element);
						if ( null != element ) {
							String actionsetkey = element;
							
							Map<String, Map<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									// TODO Auto-generated method stub
									String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.info(function, "os1[{}]", os1);
									
									if ( null != os1 ) {
										if ( os1.equals("SendDpcScanControl") ) {
											
											if ( null != selectedSet ) {
												for ( Map<String, String> hashMap : selectedSet ) {
													String selectedAlias = hashMap.get(columnAlias);
													String selectedServiceOwner = hashMap.get(columnServiceOwner);
													
													logger.info(function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
													
													String scsEnvId = selectedServiceOwner;
													String alias = selectedAlias;
													
													logger.info(function, "alias BF [{}]", alias);
													
													if ( ! selectedAlias.startsWith("<alias>") ) alias = "<alias>" + selectedAlias;
													
													logger.info(function, "alias AF [{}]", alias);
													
													WidgetStatus curStatusSet = uiGeneric.getWidgetStatus(strSet);
													
													ValidityStatus validityStatus = DCP_i.ValidityStatus.VALID;
													if ( WidgetStatus.Down == curStatusSet ) {
														validityStatus = ValidityStatus.OPERATOR_INHIBIT;
													}
													
													String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + validityStatus.toString() + "_" + alias;
													dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, validityStatus);
						
												}
											} else {
												logger.warn(function, "selectedSet IS NULL");
											}
											

										}
									}
									return true;
								}
							});
						}
					}
				}
				logger.end(function);
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				
				logger.begin(function);
				
				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				
				logger.info(function, "os1["+os1+"]");
				
				if ( null != os1 ) {
					// Filter Action
					if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
						
						logger.info(function, "FilterAdded");
						
						uiEventActionProcessor_i.executeActionSet(os1);
						
					} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
						
						logger.info(function, "FilterRemoved");
						
						uiEventActionProcessor_i.executeActionSet(os1);
					
					} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
						// Activate Selection
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						
						logger.info(function, "Store Selected Row");
						
						selectedSet	= (Set<Map<String, String>>) obj1;
						
						String selectedStatus1 = null;
						for ( Map<String, String> hashMap : selectedSet ) {
							selectedStatus1 = hashMap.get(columnStatus);
						}
						
						if ( null != selectedStatus1 ) {
							if ( valueSet.equals(selectedStatus1) ) {
								String actionsetkey = os1+"_valueUnset";
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
							}
							if ( valueUnSet.equals(selectedStatus1) ) {
								String actionsetkey = os1+"_valueSet";
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
							}
						}

					} else {
						// General Case
						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.info(function, "oe ["+oe+"]");
						logger.info(function, "os1["+os1+"]");
						
						if ( null != oe ) {
							if ( oe.equals(element) ) {
								uiEventActionProcessor_i.executeActionSet(os1);
							}
						}
					}
				}
				logger.end(function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.begin(function);
				logger.end(function);
			}
			
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.begin(function);
				logger.end(function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.begin(function);
				logger.end(function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(function);
				envDown(null);
				logger.end(function);
			}
		};

		logger.end(function);
	}

}
