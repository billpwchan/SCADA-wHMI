package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.EquipmentBuilderImpl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.EquipmentBuilder_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridDatabase;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatterSOC;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatterSOCDetails;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatter_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.database.UIDataGridDatabaseMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetDataGrid extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDataGrid.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
//	private String contextMenuOptsXMLFile = "UIEventActionProcessor_CallImage.opts.xml";

	// External
	private SimpleEventBus eventBus		= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	private UIEventActionProcessor_i uiEventActionProcessorContextMenu_i = null;
	
	private String strDataGrid = null;
	private DataGrid<Equipment_i> dataGrid = null;
	
//	private ScsOlsListPanel scsOlsListPanel					= null;
//	private ScsAlarmDataGridPresenterClient gridPresenter	= null;
//	private ScsGenericDataGridView gridView					= null;
//	private ScsOlsListPanelMenu contextMenu					= null;
	
//	public void removeFilter() {
//		final String function = "removeFilter";
//		
//		logger.begin(className, function);
//		if ( null != gridPresenter ) {
//			Set<String> entitles = gridPresenter.getFilterColumns();
//			for ( String entitle : entitles ) {
//				logger.warn(className, function, "entitle[{}]", entitle);
//				gridPresenter.removeContainerFilter(entitle);
//			}
//		} else {
//			logger.warn(className, function, "gridPresenter IS NULL");
//		}
//		logger.end(className, function);
//	}
	
//	public void applyFilter(String column, String value) {
//		final String function = "applyFilter";
//		
//		logger.begin(className, function);
//		logger.info(className, function, "column[{}] value[{}]", column, value);
//
//		ColumnFilterData cfd = new ColumnFilterData(column, value);
//		
//		FilterDescription fd = null;
//		boolean isEnumType = true;
//		if ( isEnumType ) {
//			Set<String> s = new HashSet<String>();
//			s.add(value);
//			fd = new StringEnumFilterDescription(s);			
//		} else {
//			fd = new StringFilterDescription(StringFilterTypes.EQUALS, cfd.getFilterValue());
//		}
//		
//		gridPresenter.setContainerFilter(cfd.getColumnName(), fd);
//		logger.info(className, function, "column[{}] value[{}]", column, value);
//		logger.end(className, function);
//	}

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			if ( null != uiEventAction ) {
				
				String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
				String os2 = (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
				String os3 = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
				String os4 = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
				
				logger.info(className, function, "os1[{}]", os1);
				logger.info(className, function, "os2[{}]", os2);
				logger.info(className, function, "os3[{}]", os3);
				logger.info(className, function, "os4[{}]", os4);
				
				if ( null != os1 ) {
//					if ( os1.equals(FilterViewEvent.AddFilter.toString()) ) {
//						if ( null != os2 && null != os3 && null != os4) {
//							String listConfigId = scsOlsListPanel.getStringParameter(ScsOlsListPanel_i.ParameterName.ListConfigId.toString());
//							logger.info(className, function, "listConfigId[{}]", listConfigId);
//							if ( null != listConfigId ) {
//								if ( os2.equals(listConfigId) ) {
//									applyFilter(os3, os4);
//								} else {
//									logger.warn(className, function, "od1[{}] AND listConfigId[{}] IS NOT EQUALS", os1, listConfigId);
//								}
//							} else {
//								logger.warn(className, function, "listConfigId IS NULL", listConfigId);
//							}
//		
//						} else if ( null == os2 ) {
//							logger.warn(className, function, "od1 IS NULL");
//						} else if ( null == os3 ) {
//							logger.warn(className, function, "od2 IS NULL");
//						}
//					} else if ( os1.equals(FilterViewEvent.RemoveFilter.toString()) ) {
//						removeFilter();
//					} else if ( os1.equals(ViewerViewEvent.AckVisible.toString()) ) {
//						if ( null != gridView ) { 
//							gridView.ackVisible();
//						} else {
//							logger.warn(className, function, "gridView IS NULL");
//						}
//					} else if ( os1.equals(ViewerViewEvent.AckVisibleSelected.toString()) ) {
//						if ( null != gridView ) { 
//							gridView.ackVisibleSelected();
//						} else {
//							logger.warn(className, function, "gridView IS NULL");
//						}
//					} else if ( os1.equals(PrintViewEvent.Print.toString()) ) {
//						Window.alert("Print Event");
//					}
				} else {
					logger.warn(className, function, "op IS NULL");
				}
			} else {
				logger.warn(className, function, "uiEventAction IS NULL");
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
			strDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGrid.toString(), strHeader);
		}
		
		logger.info(className, function, "strDataGrid[{}]", strDataGrid);
		
		uiWidgetGeneric = new UIWidgetGeneric();
		
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		rootPanel = uiWidgetGeneric.getMainPanel();

		rootPanel.add(createDataGrid());
		
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
		
//		String strScsOlsListPanel = UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName());
//		scsOlsListPanel = (ScsOlsListPanel)uiLayoutGeneric.getPredefineWidget(strScsOlsListPanel);
//		
//		if ( null != scsOlsListPanel ) {
//			
//			gridView = scsOlsListPanel.getView();
//			gridPresenter = scsOlsListPanel.getPresenter();
//			contextMenu = scsOlsListPanel.getContextMenu();
//			
//			if ( null != gridPresenter ) {
//				gridPresenter.setSelectionEvent(new SelectionEvent() {
//
//					@Override
//					public void onSelection(Set<HashMap<String, String>> entities) {
//						final String function = "onSelection fireFilterEvent";
//						
//						logger.begin(className, function);
//						
//						String actionsetkey = "RowSelected";
//						HashMap<String, Object> parameter = new HashMap<String, Object>();
//						parameter.put(ViewAttribute.OperationObject1.toString(), entities);
//						
//						HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
//						override.put("RowSelected", parameter);
//						
//						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
//
//						logger.end(className, function);
//					}
//				});
//				
//				gridPresenter.setFilterEvent(new FilterEvent() {
//					
//					@Override
//					public void onFilterChange(ArrayList<String> columns) {
//						final String function = "onFilterChange fireFilterEvent";
//
//						logger.begin(className, function);
//						
//						// Dump
//						logger.debug(className, function, "columns.size[{}]", columns.size());
//						for ( String column : columns ) {
//							logger.debug(className, function, "column[{}]", column);
//						}
//						
//						
//						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
//						if ( null != columns && columns.size() > 0 ) {
//							viewerViewEvent = ViewerViewEvent.FilterAdded;
//						}
//						String actionsetkey = viewerViewEvent.toString();
//						uiEventActionProcessor_i.executeActionSet(actionsetkey);
//
//						logger.end(className, function);
//					}
//				});
//				
//				gridPresenter.setCounterEvent(new CounterEvent() {
//					
//					@Override
//					public void onCounterChange(Map<String, Integer> countersValue) {
//						final String function = "onCounterChange fire onCounterChange";
//						
//						logger.begin(className, function);
//						
//						if ( null != countersValue ) {
//							for ( Entry<String, Integer> keyValue : countersValue.entrySet() ) {
//								
//								String key = keyValue.getKey();
//								Integer value = keyValue.getValue();
//								String strValue = value.toString();
//								
//								String actionsetkey = "CounterValueChanged";
//								HashMap<String, Object> parameter = new HashMap<String, Object>();
//								parameter.put(ViewAttribute.OperationString2.toString(), key);
//								parameter.put(ViewAttribute.OperationString3.toString(), strValue);
//								
//								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
//								override.put("CounterValueChanged", parameter);
//								
//								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
//								
//							}
//						}
//
//						logger.end(className, function);
//					}
//				});
//
//			} else {
//				logger.warn(className, function, "gridPresenter IS NULL");
//			}
			
			
//			if ( null != contextMenu ) {
				
				logger.info(className, function, "Init uiEventActionProcessorContextMenu");

				uiEventActionProcessorContextMenu_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");
				
				uiEventActionProcessorContextMenu_i.setUINameCard(uiNameCard);
				uiEventActionProcessorContextMenu_i.setPrefix(className);
				uiEventActionProcessorContextMenu_i.setElement(element);
				uiEventActionProcessorContextMenu_i.setDictionariesCacheName("UIWidgetGeneric");
//				uiEventActionProcessorContextMenu.setEventBus(eventBus);
				uiEventActionProcessorContextMenu_i.setOptsXMLFile(optsXMLFile);
//				uiEventActionProcessorContextMenu.setUIWidgetGeneric(uiWidgetGeneric);
				uiEventActionProcessorContextMenu_i.setActionSetTagName(UIActionEventType.actionset.toString());
				uiEventActionProcessorContextMenu_i.setActionTagName(UIActionEventType.action.toString());
				uiEventActionProcessorContextMenu_i.init();

//	            contextMenu.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
//	    			
//	    			@Override
//	    			public void onSelection(Set<HashMap<String, String>> entity) {
//	    				logger.warn(className, function, "entity[{}]", entity);
//	    				
//	    				if ( null != entity ) {
//	    					HashMap<String, String> hashMap = entity.iterator().next();
//	    					if ( null != hashMap ) {
//	    						String sourceID = hashMap.get("sourceID");
//	    						logger.info(className, function, "sourceID[{}]", sourceID);
//	    						if ( null != sourceID ) {
//	    							if ( null != uiEventActionProcessorContextMenu_i ) {
//	    								uiEventActionProcessorContextMenu_i.executeActionSet(sourceID);
//	    							} else {
//	    								logger.warn(className, function, "uiEventActionProcessorContextMenu IS NULL");	
//	    							}
//	    						} else {
//	    							logger.warn(className, function, "sourceID IS NULL");	
//	    						}
//	    					} else {
//	    						logger.warn(className, function, "hashMap IS NULL");	
//	    					}
//	    				} else {
//	    					logger.warn(className, function, "entity IS NULL");	
//	    				}
//	    			}
//	    		});
//			} else {
//				logger.warn(className, function, "contextMenu IS NULL");
//			}
//			
//		} else {
//			logger.warn(className, function, "scsOlsListPanel IS NULL");
//		}
		
		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
//		if ( null != scsOlsListPanel ) {
//			scsOlsListPanel.terminate();
//		}
	}

	
	private Widget createDataGrid () {
		
		final String function = "init";
		
		logger.begin(className, function);
		
	    UIDataGridDatabaseMgr databaseMgr = UIDataGridDatabaseMgr.getInstance();
	    UIDataGridFomatter_i dataGridFomatter = databaseMgr.getDataGrid(strDataGrid);
		
	    /*
	     * Set a key provider that provides a unique key for each contact. If key is
	     * used to identify contacts when fields (such as the name and address)
	     * change.
	     */
	    dataGrid = new DataGrid<Equipment_i>();
	    dataGrid.addStyleName("project-"+strDataGrid+"-table");
	    
	    /*
	     * Do not refresh the headers every time the data is updated. The footer
	     * depends on the current data, so we do not disable auto refresh on the
	     * footer.
	     */
//	    dataGrid.setAutoHeaderRefreshDisabled(true);
	    
	    // Set the message to display when the table is empty.
	    dataGrid.setEmptyTableWidget(new Label(dataGridFomatter.getEmptyLabel()));

	    // Attach a column sort handler to the ListDataProvider to sort the list.
//	    ListHandler<ContactInfo> sortHandler =
//	        new ListHandler<ContactInfo>(ContactDatabase.get().getDataProvider().getList());
//	    dataGrid.addColumnSortHandler(sortHandler);
	    
	    // Create a Pager to control the table.
//	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//	    pager.setDisplay(dataGrid);
	    
	    // Add a selection model so we can select cells.
//	    final SelectionModel<Equipment_i> selectionModel = new SingleSelectionModel<Equipment_i>();
//	    dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<Equipment_i>createCheckboxManager());

		final SingleSelectionModel<Equipment_i> selectionModel = new SingleSelectionModel<Equipment_i>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						Equipment_i selected = selectionModel.getSelectedObject();
						if (selected != null) {
							
							final String function = "onSelection fireFilterEvent";
							
							logger.begin(className, function);
							
							String actionsetkey = "RowSelected";
							HashMap<String, Object> parameter = new HashMap<String, Object>();
							parameter.put(ViewAttribute.OperationObject1.toString(), strDataGrid);
							parameter.put(ViewAttribute.OperationObject2.toString(), selected);
							
							HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
							override.put("RowSelected", parameter);
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

							logger.end(className, function);
							
//							{
//								String [] keys = selected.getFields();
//								String msg = "You selected: ";
//								for ( String key : keys ) {
//									msg += key + ":["+selected.getValue(key)+"]";
//									msg += " ";
//								}
//								Window.alert(msg);
//							}
						}
					}
				});

//	    initDataGridColumn(selectionModel, header, dataGrid);
	    
	    dataGrid = dataGridFomatter.addDataGridColumn(dataGrid);
	    
	    // Add the CellList to the adapter in the database.
	    UIDataGridDatabase uiDataGridDatabase = new UIDataGridDatabase();
//	    UIDataGridDatabase uiDataGridDatabase = UIDataGridDatabase.getInstance(strDataGrid);
	    uiDataGridDatabase.addDataDisplay(dataGrid);
	    
	    // Testing
	    if ( strDataGrid.equals("UIDataGridFomatterSOC") ) {
	    	
	    	EquipmentBuilder_i builder = new EquipmentBuilderImpl();
	    	Equipment_i equipment_i = builder
	    			.setValue(UIDataGridFomatterSOC.Header.SOCCard.toString(), "GrcPoint_TMP")
	    			.setValue(UIDataGridFomatterSOC.Header.Alias.toString(), "ScadaSoft:ScsCtlGrc"+":"+"GrcPoint_TMP")
	    			.build();
	    	uiDataGridDatabase.addEquipment(equipment_i);
//	    	UIDataGridDatabase.getInstance(strDataGrid).addEquipment(equipment_i);
	    }
	    
	    // Testing
	    if ( strDataGrid.equals("UIDataGridFomatterSOCDetails") ) {
	    	
//	    	EquipmentBuilder_i builder1 = new EquipmentBuilderImpl();
	    	Equipment_i equipment_i = null;
	    	
	    	EquipmentBuilder_i builder1 = new EquipmentBuilderImpl();
	    	equipment_i = builder1
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Step.toString(), 1)
	    			.setValue(UIDataGridFomatterSOCDetails.Header.EquipmentLabel.toString(), "OPEN HM513")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Action.toString(), "OPEN")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Status.toString(), "")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Reserved.toString(), "Reserved")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.PTW.toString(), "Not Set")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.LR.toString(), "dac")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Disable.toString(), new Boolean(true))
	    			.build();
	    	uiDataGridDatabase.addEquipment(equipment_i);
//	    	UIDataGridDatabase.getInstance(strDataGrid).addEquipment(equipment_i);
	    	
	    	EquipmentBuilder_i builder2 = new EquipmentBuilderImpl();
	    	equipment_i = builder2
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Step.toString(), 2)
	    			.setValue(UIDataGridFomatterSOCDetails.Header.EquipmentLabel.toString(), "CLOSE HOM514")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Action.toString(), "CLOSE")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Status.toString(), "")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Reserved.toString(), "Reserved")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.PTW.toString(), "Not Set")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.LR.toString(), "dac")
	    			.setValue(UIDataGridFomatterSOCDetails.Header.Disable.toString(), new Boolean(false))
	    			.build();
	    	uiDataGridDatabase.addEquipment(equipment_i);
//	    	UIDataGridDatabase.getInstance(strDataGrid).addEquipment(equipment_i);
	    }
	    
	    logger.end(className, function);
	    
	    return dataGrid;
	}
	
}
