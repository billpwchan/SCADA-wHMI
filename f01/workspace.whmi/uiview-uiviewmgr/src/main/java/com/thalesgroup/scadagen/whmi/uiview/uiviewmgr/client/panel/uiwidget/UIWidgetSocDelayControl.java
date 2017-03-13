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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetSocDelayControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private String targetDataGrid_A			= "";
	private String targetDataGridColumn_A 	= "";
	private String targetDataGridColumn_A2 	= "";
	
	private String targetDataGrid_B			= "";
	private String targetDataGridColumn_B 	= "";
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected_A = null;
	
	private String datagridSelected2 = null;
	private Equipment_i equipmentSelected_B = null;
	
	private final String strDelayValue = "delayvalue";
	private final String strWriteDelayToDB = "write_delay_to_db";
	
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
						
						// build scsenvid
						
						String scsenvid = equipmentSelected_A.getStringValue(targetDataGridColumn_A);
						logger.info(className, function, "scsenvid[{}]", scsenvid);
						
						// build dbalias
						
						String alias = equipmentSelected_A.getStringValue(targetDataGridColumn_A2);
						logger.info(className, function, "alias[{}]", alias);
						
						// build step
						
						Number nStep = equipmentSelected_B.getNumberValue(targetDataGridColumn_B);
						logger.info(className, function, "nStep[{}]", nStep);

						String step = "";
						if ( null != nStep ) {
							step = Integer.toString(nStep.intValue());
						}
						
						logger.info(className, function, "step[{}]", step);
						
						String dbalias = "<alias>" + alias+".brctable("+step+",succdelay)";
						
						// build delay value
						String strDelay = uiWidgetGeneric.getWidgetValue(strDelayValue);
						
						int delay = -1;
						if ( null != strDelay ) { 
							try {
								delay = Integer.parseInt(strDelay);
							} catch ( NumberFormatException ex ) {
								logger.info(className, function, "strDelay[{}] IS INVALID", strDelay);
							}
						}
						
						logger.info(className, function, "delay[{}]", delay);
							
						HashMap<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString2.toString(), scsenvid);
						parameter.put(ActionAttribute.OperationString3.toString(), dbalias);
						parameter.put(ActionAttribute.OperationString4.toString(), Integer.toString(delay));
						
						HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
						override.put(strWriteDelayToDB, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
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
				if ( os1.equals(DataGridEvent.RowSelected.toString() ) ) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					
					logger.info(className, function, "Store Selected Row");
					
					if ( null != targetDataGrid_A ) {
						
						logger.info(className, function, "targetDataGrid[{}]", targetDataGrid_A);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected	= (String) obj1;
								
								logger.info(className, function, "datagridSelected[{}]", datagridSelected);

								if ( datagridSelected.equals(targetDataGrid_A) ) {
									if ( null != obj2 ) {
										if ( obj2 instanceof Equipment_i ) {
											equipmentSelected_A = (Equipment_i) obj2;
										} else {
											equipmentSelected_A = null;
											
											logger.warn(className, function, "obj2 IS NOT TYPE OF Equipment_i");
										}
									} else {
										logger.warn(className, function, "obj2 IS NULL");
									}
								}
							} else {
								logger.warn(className, function, "obj1 IS NOT TYPE OF String");
							}
						} else {
							logger.warn(className, function, "obj1 IS NULL");
						}
					} else {
						logger.warn(className, function, "targetDataGrid IS NULL");
					}
					
					if ( null != targetDataGrid_B ) {
						
						logger.info(className, function, "targetDataGrid2[{}]", targetDataGrid_B);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected2	= (String) obj1;
								
								logger.info(className, function, "datagridSelected2[{}]", datagridSelected2);

								if ( datagridSelected2.equals(targetDataGrid_B) ) {
									if ( null != obj2 ) {
										if ( obj2 instanceof Equipment_i ) {
											equipmentSelected_B = (Equipment_i) obj2;
										} else {
											equipmentSelected_B = null;
											
											logger.warn(className, function, "obj2 IS NOT TYPE OF Equipment_i");
										}
									} else {
										logger.warn(className, function, "obj2 IS NULL");
									}
								}
							} else {
								logger.warn(className, function, "obj1 IS NOT TYPE OF String");
							}
						} else {
							logger.warn(className, function, "obj1 IS NULL");
						}
					} else {
						logger.warn(className, function, "targetDataGrid2 IS NULL");
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
			
			targetDataGrid_A		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_A.toString(), strHeader);
			targetDataGridColumn_A	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A.toString(), strHeader);
			targetDataGridColumn_A2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A2.toString(), strHeader);
			
			targetDataGrid_B		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_B.toString(), strHeader);
			targetDataGridColumn_B	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_B.toString(), strHeader);
		}
		
		logger.info(className, function, "targetDataGridColumn[{}]", targetDataGridColumn_A);
		logger.info(className, function, "targetDataGrid[{}]", targetDataGrid_A);
		
		logger.info(className, function, "targetDataGridColumn2[{}]", targetDataGridColumn_A2);
		logger.info(className, function, "targetDataGrid2[{}]", targetDataGrid_B);
		
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
