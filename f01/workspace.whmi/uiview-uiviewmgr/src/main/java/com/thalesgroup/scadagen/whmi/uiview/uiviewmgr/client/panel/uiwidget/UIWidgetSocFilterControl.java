package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl_i.AutoManuEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocFilterControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetSocFilterControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocFilterControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private String targetDataGrid			= "";
	private String targetDataGridColumn1	= "";
	private String targetDataGridColumn2	= "";
	private String targetDataGridColumn3	= "";
	private String targetDataGridColumn4	= "";
	private String targetDataGridColumn5	= "";
	private String targetDataGridColumn6	= "";
	private String targetDataGridColumn7	= "";
	private String targetDataGridColumn8	= "";
	
	private String strColumnFilter1Value = "";
	private String strColumnFilter2Value = "";
	private String strColumnFilter3Value = "";
	private String strColumnFilter4Value = "";
	private String strColumnFilter5Value = "";
	private String strColumnFilter6Value = "";
	private String strColumnFilter7Value = "";
	private String strColumnFilter8Value = "";
	
	private String strColumnFilter1 = "";
	private String strColumnFilter2 = "";
	private String strColumnFilter3 = "";
	private String strColumnFilter4 = "";
	private String strColumnFilter5 = "";
	private String strColumnFilter6 = "";
	private String strColumnFilter7 = "";
	private String strColumnFilter8 = "";
	
	private String strColumnFilter1Status = "";
	private String strColumnFilter2Status = "";
	private String strColumnFilter3Status = "";
	private String strColumnFilter4Status = "";
	private String strColumnFilter5Status = "";
	private String strColumnFilter6Status = "";
	private String strColumnFilter7Status = "";
	private String strColumnFilter8Status = "";
	

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			
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
						
						HashMap<String, Object> parameter = new HashMap<String, Object>();
						HashMap<String, String> filterMap = new HashMap<String, String>();
						String actionsetkey = element;
						String action = element;

						if (element.equals(targetDataGridColumn1)) {
							strColumnFilter1 = uiWidgetGeneric.getWidgetValue(strColumnFilter1Value);
							filterMap.put(element, strColumnFilter1);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);
							
							if (strColumnFilter1Status != null && !strColumnFilter1Status.isEmpty()) {
								if (strColumnFilter1.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
								
							}
						} else if (element.equals(targetDataGridColumn2)) {
							strColumnFilter2 = uiWidgetGeneric.getWidgetValue(strColumnFilter2Value);
							filterMap.put(element, strColumnFilter2);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);	
							
							if (strColumnFilter2Status != null && !strColumnFilter2Status.isEmpty()) {
								if (strColumnFilter2.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn3)) {
							strColumnFilter3 = uiWidgetGeneric.getWidgetValue(strColumnFilter3Value);
							filterMap.put(element, strColumnFilter3);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);		
						
							if (strColumnFilter3Status != null && !strColumnFilter3Status.isEmpty()) {
								if (strColumnFilter3.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn4)) {
							strColumnFilter4 = uiWidgetGeneric.getWidgetValue(strColumnFilter4Value);
							filterMap.put(element, strColumnFilter4);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);	
							
							if (strColumnFilter4Status != null && !strColumnFilter4Status.isEmpty()) {
								if (strColumnFilter4.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn5)) {
							strColumnFilter5 = uiWidgetGeneric.getWidgetValue(strColumnFilter5Value);
							filterMap.put(element, strColumnFilter5);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);	
							
							if (strColumnFilter5Status != null && !strColumnFilter5Status.isEmpty()) {
								if (strColumnFilter5.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn6)) {
							strColumnFilter6 = uiWidgetGeneric.getWidgetValue(strColumnFilter6Value);
							filterMap.put(element, strColumnFilter6);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);	
							
							if (strColumnFilter6Status != null && !strColumnFilter6Status.isEmpty()) {
								if (strColumnFilter6.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn7)) {
							strColumnFilter7 = uiWidgetGeneric.getWidgetValue(strColumnFilter7Value);
							filterMap.put(element, strColumnFilter7);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);		
							
							if (strColumnFilter7Status != null && !strColumnFilter7Status.isEmpty()) {
								if (strColumnFilter7.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						} else if (element.equals(targetDataGridColumn8)) {
							strColumnFilter8 = uiWidgetGeneric.getWidgetValue(strColumnFilter8Value);
							filterMap.put(element, strColumnFilter8);
							parameter.put(ViewAttribute.OperationObject1.toString(), filterMap);	
						
							if (strColumnFilter8Status != null && !strColumnFilter8Status.isEmpty()) {
								if (strColumnFilter8.isEmpty()) {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Up);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonUpLabel");
								} else {
									uiWidgetGeneric.setWidgetStatus(element, WidgetStatus.Down);
									uiWidgetGeneric.setWidgetValue(element, "&"+className+"_"+element+"_"+"ButtonDownLabel");
								}
							}
						}
						
						if (!parameter.isEmpty()) {
							HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
							override.put(action, parameter);
						
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						}
					}
				}
			}
			logger.end(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "os1["+os1+"]");
			
			if ( null != os1 ) {
				if ( os1.equals(AutoManuEvent.RadioBoxSelected.toString() ) ) {
					
					logger.info(className, function, "Store Selected RadioBox");
					
					String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
					
					if ( null != os2 ) {
						
						logger.info(className, function, "os2[{}]", os2);
						
					} else {
						logger.warn(className, function, "os2 IS NULL");
					}
					
				} else {
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
			}
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

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			targetDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid.toString(), strHeader);
			targetDataGridColumn1	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn1.toString(), strHeader);
			targetDataGridColumn2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn2.toString(), strHeader);
			targetDataGridColumn3	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn3.toString(), strHeader);
			targetDataGridColumn4	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn4.toString(), strHeader);
			targetDataGridColumn5	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn5.toString(), strHeader);
			targetDataGridColumn6	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn6.toString(), strHeader);
			targetDataGridColumn7	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn7.toString(), strHeader);
			targetDataGridColumn8	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn8.toString(), strHeader);
			
			String dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn1Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter1Value = dataGridColumnValue;
			} else {
				strColumnFilter1Value = targetDataGridColumn1 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn2Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter2Value = dataGridColumnValue;
			} else {
				strColumnFilter2Value = targetDataGridColumn2 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn3Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter3Value = dataGridColumnValue;
			} else {
				strColumnFilter3Value = targetDataGridColumn3 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn4Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter4Value = dataGridColumnValue;
			} else {
				strColumnFilter4Value = targetDataGridColumn4 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn5Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter5Value = dataGridColumnValue;
			} else {
				strColumnFilter5Value = targetDataGridColumn5 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn6Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter6Value = dataGridColumnValue;
			} else {
				strColumnFilter6Value = targetDataGridColumn6 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn7Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter7Value = dataGridColumnValue;
			} else {
				strColumnFilter7Value = targetDataGridColumn7 + "Value";
			}
			dataGridColumnValue	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn8Value.toString(), strHeader);
			if (dataGridColumnValue != null && !dataGridColumnValue.isEmpty()) {
				strColumnFilter8Value = dataGridColumnValue;
			} else {
				strColumnFilter8Value = targetDataGridColumn8 + "Value";
			}
			
			String dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn1Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter1Status = dataGridColumnStatus;
			} else {
				strColumnFilter1Status = targetDataGridColumn1 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn2Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter2Status = dataGridColumnStatus;
			} else {
				strColumnFilter2Status = targetDataGridColumn2 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn3Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter3Status = dataGridColumnStatus;
			} else {
				strColumnFilter3Status = targetDataGridColumn3 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn4Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter4Status = dataGridColumnStatus;
			} else {
				strColumnFilter4Status = targetDataGridColumn4 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn5Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter5Status = dataGridColumnStatus;
			} else {
				strColumnFilter5Status = targetDataGridColumn5 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn6Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter6Status = dataGridColumnStatus;
			} else {
				strColumnFilter6Status = targetDataGridColumn6 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn7Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter7Status = dataGridColumnStatus;
			} else {
				strColumnFilter7Status = targetDataGridColumn7 + "Status";
			}
			dataGridColumnStatus	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn8Status.toString(), strHeader);
			if (dataGridColumnStatus != null && !dataGridColumnStatus.isEmpty()) {
				strColumnFilter8Status = dataGridColumnStatus;
			} else {
				strColumnFilter8Status = targetDataGridColumn8 + "Status";
			}
		}
		
		logger.info(className, function, "targetDataGrid[{}]", targetDataGrid);
		logger.info(className, function, "targetDataGridColumn1[{}]", targetDataGridColumn1);
		logger.info(className, function, "targetDataGridColumn2[{}]", targetDataGridColumn2);
		logger.info(className, function, "targetDataGridColumn3[{}]", targetDataGridColumn3);
		logger.info(className, function, "targetDataGridColumn4[{}]", targetDataGridColumn4);
		logger.info(className, function, "targetDataGridColumn5[{}]", targetDataGridColumn5);
		logger.info(className, function, "targetDataGridColumn6[{}]", targetDataGridColumn6);
		logger.info(className, function, "targetDataGridColumn7[{}]", targetDataGridColumn7);
		logger.info(className, function, "targetDataGridColumn8[{}]", targetDataGridColumn8);
		
		logger.info(className, function, "strColumnFilter1Value[{}]", strColumnFilter1Value);
		logger.info(className, function, "strColumnFilter2Value[{}]", strColumnFilter2Value);
		logger.info(className, function, "strColumnFilter3Value[{}]", strColumnFilter3Value);
		logger.info(className, function, "strColumnFilter4Value[{}]", strColumnFilter4Value);
		logger.info(className, function, "strColumnFilter5Value[{}]", strColumnFilter5Value);
		logger.info(className, function, "strColumnFilter6Value[{}]", strColumnFilter6Value);
		logger.info(className, function, "strColumnFilter7Value[{}]", strColumnFilter7Value);
		logger.info(className, function, "strColumnFilter8Value[{}]", strColumnFilter8Value);
		
		logger.info(className, function, "strColumnFilter1Status[{}]", strColumnFilter1Status);
		logger.info(className, function, "strColumnFilter2Status[{}]", strColumnFilter2Status);
		logger.info(className, function, "strColumnFilter3Status[{}]", strColumnFilter3Status);
		logger.info(className, function, "strColumnFilter4Status[{}]", strColumnFilter4Status);
		logger.info(className, function, "strColumnFilter5Status[{}]", strColumnFilter5Status);
		logger.info(className, function, "strColumnFilter6Status[{}]", strColumnFilter6Status);
		logger.info(className, function, "strColumnFilter7Status[{}]", strColumnFilter7Status);
		logger.info(className, function, "strColumnFilter8Status[{}]", strColumnFilter8Status);

		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
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
