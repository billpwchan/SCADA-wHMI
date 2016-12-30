package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetSocControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private String targetDataGridColumn = "";
	private String targetDataGrid		= "";
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected = null;
	
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
						uiEventActionProcessor_i.executeActionSet(actionsetkey, new ExecuteAction_i() {
							
							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
								
								logger.info(className, function, "os1[{}]", os1);
								
								if ( null != os1 ) {
									
									if ( os1.equals("SendSocControlStart") ) {
										
										if ( null != equipmentSelected ) {
											
											logger.info(className, function, "targetDataGridColumn[{}]", targetDataGridColumn);
											
											String alias = equipmentSelected.getStringValue(targetDataGridColumn);
											
											logger.info(className, function, "alias[{}]", alias);
											
										} else {
											logger.warn(className, function, "equipmentSelected IS NULL");
										}
										
									} else if ( os1.equals("SendSocControlStop") ) {
										
										logger.info(className, function, "targetDataGridColumn[{}]", targetDataGridColumn);
										
										String alias = equipmentSelected.getStringValue(targetDataGridColumn);
										
										logger.info(className, function, "alias[{}]", alias);
										
									} else if ( os1.equals("SendSocControlSkipFailed") ) {
										
										
									}
									
									
//									if ( os1.equals("SendDpcTagControl") ) {
//								
//										for ( HashMap<String, String> hashMap : selectedSet ) {
//											String selectedAlias = hashMap.get(columnAlias);
//											String selectedServiceOwner = hashMap.get(columnServiceOwner);
//											
//											logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
//											
//											String scsEnvId = selectedServiceOwner;
//											String alias = selectedAlias;
//											
//											logger.info(className, function, "alias BF [{}]", alias);
//											
//											if ( ! selectedAlias.startsWith("<alias>") ) alias = "<alias>" + selectedAlias;
//											
//											logger.info(className, function, "alias AF [{}]", alias);
//											
//											WidgetStatus curStatusSet = uiWidgetGeneric.getWidgetStatus(strSet);
//											
//											TaggingStatus taggingStatus = TaggingStatus.NO_TAGGING;
//											if ( WidgetStatus.Down == curStatusSet ) {
//												taggingStatus = TaggingStatus.TAGGING_1;
//												if ( null != os2 && os2.length() > 0 ) {
//													if ( os2.equalsIgnoreCase(
//															Integer.toString(TaggingStatus.ALL_TAGGING.getValue()) ) ) {
//														taggingStatus = TaggingStatus.ALL_TAGGING;
//													} else if ( os2.equalsIgnoreCase(
//															Integer.toString(TaggingStatus.TAGGING_1.getValue()) ) ) {
//														taggingStatus = TaggingStatus.TAGGING_1;
//													} else if ( os2.equalsIgnoreCase(
//															Integer.toString(TaggingStatus.TAGGING_2.getValue()) ) ) {
//														taggingStatus = TaggingStatus.TAGGING_2;
//													}
//												}
//											}
//				
//											String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + taggingStatus.toString() + "_" + alias;
//												
//											String taggingLabel1 = strEMPTY;
//											String taggingLabel2 = strEMPTY;
//											
////											if ( null != os2 && os2.length() > 0 ) taggingLabel1 = os2;
////											if ( null != os3 && os3.length() > 0 ) taggingLabel2 = os3;
//											
//											taggingLabel1 = uiWidgetGeneric.getWidgetValue(strTextValue);
//											uiWidgetGeneric.setWidgetValue(strTextValue, strEMPTY);
//											
//											logger.info(className, function, "key[{}]", key);
//											
//											dpcMgr.sendChangeEqpTag(key, scsEnvId, alias, taggingStatus, taggingLabel1, taggingLabel2);
//				
//										}
//									}
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
				if ( os1.equals(DataGridEvent.RowSelected.toString() ) ) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					
					logger.info(className, function, "Store Selected Row");
					
					if ( null != targetDataGrid ) {
						
						logger.info(className, function, "targetDataGrid[{}]", targetDataGrid);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected	= (String) obj1;
								
								logger.info(className, function, "datagridSelected[{}]", datagridSelected);

								if ( datagridSelected.equals(targetDataGrid) ) {
									if ( null != obj2 ) {
										if ( obj2 instanceof Equipment_i ) {
											equipmentSelected = (Equipment_i) obj2;
										} else {
											equipmentSelected = null;
											
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
					
//					String selectedStatus1 = null;
//					for ( HashMap<String, String> hashMap : selectedSet ) {
//						selectedStatus1 = hashMap.get(columnStatus);
//					}
				}
				
//				// Filter Action
//				if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
//					
//					logger.info(className, function, "FilterAdded");
//					
//					uiEventActionProcessor_i.executeActionSet(os1);
//					
//				} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
//					
//					logger.info(className, function, "FilterRemoved");
//					
//					uiEventActionProcessor_i.executeActionSet(os1);
//				
//				} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
//					// Activate Selection
//					
//					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
//					
//					logger.info(className, function, "Store Selected Row");
//					
//					selectedSet	= (Set<HashMap<String, String>>) obj1;
//					
//					String selectedStatus1 = null;
//					for ( HashMap<String, String> hashMap : selectedSet ) {
//						selectedStatus1 = hashMap.get(columnStatus);
//					}
//					
//					if ( null != selectedStatus1 ) {
//						if ( valueSet.equals(selectedStatus1) ) {
//							String actionsetkey = os1+"_valueUnset";
//							uiEventActionProcessor_i.executeActionSet(actionsetkey);
//						}
//						if ( valueUnSet.equals(selectedStatus1) ) {
//							String actionsetkey = os1+"_valueSet";
//							uiEventActionProcessor_i.executeActionSet(actionsetkey);
//						}
//					}
//
//				} else {
//					// General Case
//					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
//					
//					logger.info(className, function, "oe ["+oe+"]");
//					logger.info(className, function, "os1["+os1+"]");
//					
//					if ( null != oe ) {
//						if ( oe.equals(element) ) {
//							uiEventActionProcessor_i.executeActionSet(os1, new ExecuteAction_i() {
//								
//								@Override
//								public boolean executeHandler(UIEventAction uiEventAction) {
//									return true;
//									
//								}
//							});
//						}
//					}
//				}
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
			targetDataGridColumn	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn.toString(), strHeader);
			targetDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid.toString(), strHeader);
//			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnAlias.toString(), strHeader);
//			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnStatus.toString(), strHeader);
//			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnServiceOwner.toString(), strHeader);
//			valueSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueSet.toString(), strHeader);
//			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueUnSet.toString(), strHeader);
		}
		
		logger.info(className, function, "targetDataGridColumn[{}]", targetDataGridColumn);
		logger.info(className, function, "targetDataGrid[{}]", targetDataGrid);
		
//		logger.info(className, function, "columnAlias[{}]", columnAlias);
//		logger.info(className, function, "columnStatus[{}]", columnStatus);
//		logger.info(className, function, "columnServiceOwner[{}]", columnServiceOwner);
//		logger.info(className, function, "valueSet[{}]", valueSet);
//		logger.info(className, function, "valueUnSet[{}]", valueUnSet);
		
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