package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.IGenericDataGridView;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridDatabase;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFormatter_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.database.UIDataGridDatabaseMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ButtonOperation_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.CreateText_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.SCADAgenPager;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIWidgetDataGrid extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDataGrid.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus		= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	private UIEventActionProcessor_i uiEventActionProcessorContextMenu_i = null;
	
	private String strDataGrid = null;
	private String strDataGridColumnsType = null;
	private String strDataGridColumnsHeaderString = null;
	private String strDataGridColumnsHeaderTranslation = null;
	private String strDataGridColumnsLabel = null;
	private String strDataGridColumnsWidth = null;
	private String strDataGridColumnsTranslation = null;
	private String strDataGridColumnsSort = null;
	private String strDataGridOptsXMLFile = null;
	private String strDataGridPageSize = null;
	private String strDataGridEmptyLabel = null;
	private String strDataGridCssFlagColumn = null; 

	private String targetDataGrid		= "";
	private String targetDataGridColumn1 = "";
	private String targetDataGridColumn2 = "";
	private String targetDataGridColumn3 = "";
	
	private String [] strDataGridColumnsHeaderStrings = null;
	private int [] intDataGridColumnsHeaderTranslations = null;
	private String [] strDataGridColumnsLabels = null;
	private String [] strDataGridColumnsTypes = null;
	private String [] strDataGridCssFlagColumns = null;
	private int [] intDataGridColumnsWidths = null;
	private int [] intDataGridColumnsTranslations = null;
	private int [] intDataGridColumnsSort = null;
	
	private Map<String, Boolean> columnLabelTranslationMap = new HashMap<String, Boolean>();
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected = null;
	
	private final String split = ",";
	
	private DataGrid<Equipment_i> dataGrid = null;
	
	private UIDataGridDatabase uiDataGridDatabase = null;
	
	protected static final DataGrid.Resources GRID_RESOURCES = GWT.create(IGenericDataGridView.Resources.class);

	private String strDataGridPagerName = null;
	private String strDataGridFastForwardRows = null;

	protected int pageSize = 20;
	protected int fastForwardRows = 60;
	private SCADAgenPager pager = null;
	
	private String scsEnvIdsStr = null;
	
	//private String uuid = UUIDWrapper.getUUID();
	
    UIDataGridFormatter_i dataGridFormatter = null;
	
	private SingleSelectionModel<Equipment_i> singleSelectionModel = null;

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
//				String os2 = (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
//				String os3 = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
//				String os4 = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
				
				logger.debug(className, function, "os1[{}]", os1);
//				logger.debug(className, function, "os2[{}]", os2);
//				logger.debug(className, function, "os3[{}]", os3);
//				logger.debug(className, function, "os4[{}]", os4);
				
				String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
				
				logger.debug(className, function, "oe[{}] element[{}]", oe, element);
				
				if ( null != os1 ) {
					if ( os1.equals(DataGridEvent.RowSelected.toString() ) ) {
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
						
						logger.debug(className, function, "Store Selected Row");
						
						if ( null != targetDataGrid ) {
							
							logger.debug(className, function, "targetDataGrid[{}]", targetDataGrid);
							
							if ( null != obj1 ) {
								if ( obj1 instanceof String ) {
									datagridSelected	= (String) obj1;
									
									logger.debug(className, function, "datagridSelected[{}]", datagridSelected);

									if ( datagridSelected.equals(targetDataGrid) ) {
										if ( null != obj2 ) {
											if ( obj2 instanceof Equipment_i ) {
												equipmentSelected = (Equipment_i) obj2;
												
												String scsEnvId = equipmentSelected.getStringValue(targetDataGridColumn1);
												String dbaddress = equipmentSelected.getStringValue(targetDataGridColumn2);																							
												
												if (uiDataGridDatabase != null) {
													uiDataGridDatabase.loadData(scsEnvId, dbaddress);
												}
									
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
					} else if ( os1.equals(DataGridEvent.ReloadFromDataSource.toString() ) ) {
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
						Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
						
						logger.debug(className, function, "Reload from DataSource");
						
						if ( null != strDataGrid ) {
							
							logger.debug(className, function, "strDataGrid[{}]", strDataGrid);
							
							if ( null != obj1 ) {
								if ( obj1 instanceof String ) {
									datagridSelected	= (String) obj1;
									
									logger.debug(className, function, "datagridSelected[{}]", datagridSelected);

									if ( datagridSelected.equals(strDataGrid) ) {
										if ( obj2 instanceof String && obj3 instanceof String ) {
											String scsEnvId = (String) obj2;
											String dbaddress = (String) obj3;																					
											
											if (uiDataGridDatabase != null) {
												uiDataGridDatabase.loadData(scsEnvId, dbaddress);
											}
								
										} else {
											equipmentSelected = null;

											logger.warn(className, function, "obj2 IS NOT TYPE OF Equipment_i");
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
					}  else if ( os1.equals(DataGridEvent.ResetColumnData.toString() ) ) {
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
						
						logger.debug(className, function, "Reset Column Data");
						
						if ( null != strDataGrid ) {
							
							logger.debug(className, function, "strDataGrid[{}]", strDataGrid);
							
							if ( null != obj1 && null != obj2 ) {
								if ( obj1 instanceof String && obj2 instanceof String ) {
	
									String dataGridSelected = (String) obj1;
									
									if (dataGridSelected.equals(strDataGrid)) {
										String columnLabel = (String) obj2;
										String columnType = dataGridFormatter.getColumnType(columnLabel);
										
										uiDataGridDatabase.resetColumnData(columnLabel, columnType);
									}
								} else {
									logger.warn(className, function, "obj1 or obj2 IS NOT TYPE OF String");
								}
							} else {
								logger.warn(className, function, "obj1 or obj2 IS NULL");
							}
						} else {
							logger.warn(className, function, "strDataGrid IS NULL");
						}
					
					}  else if ( os1.equals(DataGridEvent.ReloadColumnData.toString() ) ) {
						
						logger.debug(className, function, "Reload Column Data");
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
						Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
												
						if ( null != strDataGrid ) {
							
							logger.debug(className, function, "strDataGrid[{}]", strDataGrid);
							
							if ( null != obj1 && null != obj2) {
								if ( obj1 instanceof String && obj2 instanceof String) {
	
									String dataGridSelected = (String) obj1;
									
									String columnLabel = (String) obj2;
									
									String [] columnLabels = UIWidgetUtil.getStringArray(columnLabel, split); 
									String [] columnTypes = new String[columnLabels.length];
									boolean []  enableTranslations = new boolean[columnLabels.length];
											
									logger.debug(className, function, "columnLabels from obj2:[{}][{}]", columnLabels[0], columnLabels[1]);
									logger.debug(className, function, "columnLabels length is:[{}]", columnLabels.length);
									logger.debug(className, function, "dataGridSelected is:[{}]", dataGridSelected);
									logger.debug(className, function, "strDataGrid is:[{}]", strDataGrid);
									logger.debug(className, function, "Are they equal?[{}]", dataGridSelected.equals(strDataGrid));
									
									if (dataGridSelected.equals(strDataGrid)) {
										logger.debug(className, function, "For loop will be started");
										for (int i=0; i<columnLabels.length; i++){
											logger.debug(className, function, "For loop started with i = [{}], and length =[{}]", i, columnLabels.length);
											columnTypes[i] = dataGridFormatter.getColumnType(columnLabels[i]);
											enableTranslations[i] = false;
											if (columnTypes[i].equals("String")) {
												Boolean enable = columnLabelTranslationMap.get(columnLabels[i]);
												if (enable != null) {
													enableTranslations[i] = enable;
												}
											}
											logger.debug(className, function, "columnLabels just before sending:[{}][{}]", i, columnLabels[i]);
											logger.debug(className, function, "columnTypes jsut before sending:[{}][{}]", i, columnTypes[i]);
											logger.debug(className, function, "enableTranslations just before sending:[{}][{}]", i, enableTranslations[i]);
											
										}
										uiDataGridDatabase.reloadColumnData(columnLabels, columnTypes, enableTranslations);
									}
								} else {
									logger.warn(className, function, "obj1 or obj2 IS NOT TYPE OF String");
								}
							} else {
								logger.warn(className, function, "obj1 or obj2 IS NULL");
							}
						} else {
							logger.warn(className, function, "strDataGrid IS NULL");
						}
					} else if ( os1.equals(DataGridEvent.ColumnFilterChange.toString() ) ) {
						String dataGridSelected = (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						
						if (dataGridSelected.equals(strDataGrid) && obj1 != null && obj1 instanceof HashMap<?,?>) {
							logger.debug(className, function, "process ColumnFilterChange event");
							
							HashMap<String, String> filterMap = (HashMap<String,String>)obj1;
							uiDataGridDatabase.changeColumnFilter(filterMap);
							
							// Recover datagrid sort after refresh
							ColumnSortEvent.fire(dataGrid, dataGrid.getColumnSortList());
						}
						
					} else if ( os1.equals(DataGridEvent.DisableCheckBox.toString() )){
						
						logger.debug(className, function, "Disable Check Box os1[{}]", os1);
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
						Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
												
						logger.debug(className, function, "obj1[{}]", obj1.toString());
						logger.debug(className, function, "obj2[{}]", obj2.toString());
						
						if ( null != strDataGrid ) {
							
							logger.debug(className, function, "strDataGrid[{}]", strDataGrid);
							
							if ( null != obj1 && null != obj2) {
	
								String dataGridSelected = (String) obj1;
								boolean disableFlag = (boolean) obj2;
									
								logger.debug(className, function, "dataGridSelected is:[{}]", dataGridSelected);
								logger.debug(className, function, "disableFlag:[{}]", disableFlag);
								logger.debug(className, function, "strDataGrid is:[{}]", strDataGrid);
								try{
									dataGridFormatter.getCheckBoxes().isCheckBoxDisabled_ = disableFlag;
									uiDataGridDatabase.refreshDisplays();
								} catch (Exception e){
									logger.warn(e.toString());
								}								
							} else {
								logger.warn(className, function, "obj1 or obj2 IS NULL[{}]", strDataGrid);
							}
						} else {
							logger.warn(className, function, "strDataGrid IS NULL[{}]", strDataGrid);
						}			
						
					} else if ( oe.equals(element) ) {
					
						if ( null != pager ) {
							
							if ( pager instanceof SCADAgenPager ) {
								
								SCADAgenPager simplePager = (SCADAgenPager)pager;
								
								if ( os1.equals(ViewerViewEvent.FirstPageSelected.toString()) ) {
									
									simplePager.firstPage();
									
								}
								else
								if ( os1.equals(ViewerViewEvent.PreviousPageSelected.toString()) ) {
									
									simplePager.previousPage();
									
								}
								else
								if ( os1.equals(ViewerViewEvent.NextPageSelected.toString()) ) {
									
									simplePager.nextPage();
									
								}
								else
								if ( os1.equals(ViewerViewEvent.LastPageSelected.toString()) ) {
									
									simplePager.lastPage();

								}
								else
								if ( os1.equals(ViewerViewEvent.FastForwardPageSelected.toString()) ) {
									
									simplePager.fastForwardPage();

								}
							} else {
								logger.warn(className, function, "pager instanceof SimplePager IS NOT");
							}
						}
					
					} else {
						// General Case
						logger.debug(className, function, "oe ["+oe+"]");
						logger.debug(className, function, "os1["+os1+"]");
						
						if ( null != oe ) {
							if ( oe.equals(element) ) {
								uiEventActionProcessor_i.executeActionSet(os1);
							}
						}
					}
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
		logger.debug(className, function, "strEventBusName[{}]", strEventBusName);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGrid.toString(), strHeader);
			
			strDataGridColumnsType			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsType.toString(), strHeader);
			strDataGridColumnsHeaderString	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsHeaderString.toString(), strHeader);
			strDataGridColumnsHeaderTranslation	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsHeaderTranslation.toString(), strHeader);
			strDataGridColumnsLabel			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsLabel.toString(), strHeader);
			strDataGridColumnsWidth			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsWidth.toString(), strHeader);
			strDataGridColumnsTranslation	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsTranslation.toString(), strHeader);
			strDataGridColumnsSort			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsSort.toString(), strHeader);
			strDataGridOptsXMLFile 			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridOptsXMLFile.toString(), strHeader);
			strDataGridPagerName		 	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridPagerName.toString(), strHeader);
			strDataGridPageSize			 	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridPageSize.toString(), strHeader);
			strDataGridFastForwardRows		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridFastForwardRows.toString(), strHeader);
			strDataGridEmptyLabel			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridEmptyLabel.toString(), strHeader);
			strDataGridCssFlagColumn		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridCssFlagColumn.toString(), strHeader);

			targetDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_A.toString(), strHeader);
			targetDataGridColumn1	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A.toString(), strHeader);
			targetDataGridColumn2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A2.toString(), strHeader);
			targetDataGridColumn3	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A3.toString(), strHeader);
		}
		
		logger.debug(className, function, "strDataGrid[{}]", strDataGrid);
		
		scsEnvIdsStr = getStringParameter(WidgetParameterName.ScsEnvIds.toString());
		logger.debug(className, function, "scsEnvIdsStr [{}]", scsEnvIdsStr);
		logger.debug(className, function, "strDataGridColumnsType [{}]", strDataGridColumnsType);
		logger.debug(className, function, "strDataGridColumnHeaderString [{}]", strDataGridColumnsHeaderString);
		logger.debug(className, function, "strDataGridColumnsHeaderTranslation [{}]", strDataGridColumnsHeaderTranslation);
		logger.debug(className, function, "strDataGridColumnsLabel [{}]", strDataGridColumnsLabel);
		logger.debug(className, function, "strDataGridColumnsWidth [{}]", strDataGridColumnsWidth);
		logger.debug(className, function, "strDataGridColumnsTranslation [{}]", strDataGridColumnsTranslation);
		logger.debug(className, function, "strDataGridColumnsSort [{}]", strDataGridColumnsSort);
		logger.debug(className, function, "strDataGridOptsXMLFile [{}]", strDataGridOptsXMLFile);
		logger.debug(className, function, "strDataGridPagerName [{}]", strDataGridPagerName);
		logger.debug(className, function, "strDataGridPageSize [{}]", strDataGridPageSize);
		logger.debug(className, function, "strDataGridFastForwardRows [{}]", strDataGridFastForwardRows);
		logger.debug(className, function, "strDataGridEmptyLabel [{}]", strDataGridEmptyLabel);
		logger.debug(className, function, "strDataGridCssFlagColumn[{}]", strDataGridCssFlagColumn);
		
		logger.debug(className, function, "targetDataGrid [{}]", targetDataGrid);
		logger.debug(className, function, "targetDataGridColumn1 [{}]", targetDataGridColumn1);
		logger.debug(className, function, "targetDataGridColumn2 [{}]", targetDataGridColumn2);
		logger.debug(className, function, "targetDataGridColumn3 [{}]", targetDataGridColumn3);

		uiWidgetGeneric = new UIWidgetGeneric();
		
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		rootPanel = uiWidgetGeneric.getMainPanel();

		rootPanel.add(createDataGrid());
		
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
		
				
		logger.debug(className, function, "Init uiEventActionProcessorContextMenu");

		uiEventActionProcessorContextMenu_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");
		
		uiEventActionProcessorContextMenu_i.setUINameCard(uiNameCard);
		uiEventActionProcessorContextMenu_i.setPrefix(className);
		uiEventActionProcessorContextMenu_i.setElement(element);
		uiEventActionProcessorContextMenu_i.setDictionariesCacheName("UIWidgetGeneric");
//		uiEventActionProcessorContextMenu.setEventBus(eventBus);
		uiEventActionProcessorContextMenu_i.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessorContextMenu.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessorContextMenu_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessorContextMenu_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessorContextMenu_i.init();

		
		uiEventActionProcessor_i.executeActionSetInit();
		
		// Add pager only after uiEventActionProcessor_i is created
		if (strDataGridPagerName != null && !strDataGridPagerName.isEmpty()) {
			addPager();
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {

		if (uiDataGridDatabase != null) {
			uiDataGridDatabase.disconnect();
		}
	}
	
	
	private Widget createDataGrid () {
		
		final String function = "init";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "strDataGridColumnsType[{}]", strDataGridColumnsType);
		logger.debug(className, function, "strDataGridColumnsHeaderString[{}]", strDataGridColumnsHeaderString);
		logger.debug(className, function, "strDataGridColumnsHeaderTranslation[{}]", strDataGridColumnsHeaderTranslation);
		logger.debug(className, function, "strDataGridColumnsLabel[{}]", strDataGridColumnsLabel);
		logger.debug(className, function, "strDataGridColumnsWidth[{}]", strDataGridColumnsWidth);
		logger.debug(className, function, "strDataGridColumnsTranslation[{}]", strDataGridColumnsTranslation);
		logger.debug(className, function, "strDataGridColumnsSort[{}]", strDataGridColumnsSort);
		logger.debug(className, function, "strDataGridOptsXMLFile [{}]", strDataGridOptsXMLFile);
		logger.debug(className, function, "strDataGridPagerName [{}]", strDataGridPagerName);
		logger.debug(className, function, "strDataGridPageSize [{}]", strDataGridPageSize);	
		logger.debug(className, function, "strDataGridFastForwardRows [{}]", strDataGridFastForwardRows);
		logger.debug(className, function, "strDataGridEmptyLabel [{}]", strDataGridEmptyLabel);
		logger.debug(className, function, "strDataGridCssFlagColumn[{}]", strDataGridCssFlagColumn);
		
		strDataGridColumnsTypes = UIWidgetUtil.getStringArray(strDataGridColumnsType, split);
		strDataGridColumnsHeaderStrings = UIWidgetUtil.getStringArray(strDataGridColumnsHeaderString, split); 
		intDataGridColumnsHeaderTranslations = UIWidgetUtil.getIntArray(strDataGridColumnsHeaderTranslation, split);
		strDataGridColumnsLabels = UIWidgetUtil.getStringArray(strDataGridColumnsLabel, split);
		intDataGridColumnsWidths = UIWidgetUtil.getIntArray(strDataGridColumnsWidth, split);
		intDataGridColumnsTranslations = UIWidgetUtil.getIntArray(strDataGridColumnsTranslation, split);
		intDataGridColumnsSort = UIWidgetUtil.getIntArray(strDataGridColumnsSort, split);
		
		if((strDataGridCssFlagColumn) != null && (strDataGridCssFlagColumn.length()>0 )){
			strDataGridCssFlagColumns = UIWidgetUtil.getStringArray(strDataGridCssFlagColumn, split);
		}
		
		for ( int i = 0 ; i < intDataGridColumnsHeaderTranslations.length ; ++i ) {
			if ( intDataGridColumnsHeaderTranslations[i] == 1 ) {
				strDataGridColumnsHeaderStrings[i] = Translation.getWording(strDataGridColumnsHeaderStrings[i]);
			}

			logger.debug(className, function, " intDataGridColumnsHeaderTranslations({})[{}] strDataGridColumnHeaderStrings(i)[{}]"
					, new Object[]{i, intDataGridColumnsHeaderTranslations[i], i, strDataGridColumnsHeaderStrings[i]});
		}
		
		strDataGridEmptyLabel = Translation.getWording(strDataGridEmptyLabel);
		
		if (strDataGridPageSize != null) {
			try {
				int size = Integer.parseInt(strDataGridPageSize);
				if (size > 0) {
					pageSize = size;
				}
			} catch (NumberFormatException e) {
				logger.warn(className, function, "NumberFormatException for PageSize [{}]", strDataGridPageSize);
			}
		}
		if (strDataGridFastForwardRows != null) {
			try {
				int ffrows = Integer.parseInt(strDataGridFastForwardRows);
				if (ffrows > 0) {
					fastForwardRows = ffrows;
				}
			} catch (NumberFormatException e) {
				logger.warn(className, function, "NumberFormatException for FastForwardRows [{}]", strDataGridFastForwardRows);
			}
		}
		createColumnLabelTranslationMap(strDataGridColumnsLabels, intDataGridColumnsTranslations);
		
	    UIDataGridDatabaseMgr databaseMgr = UIDataGridDatabaseMgr.getInstance();
	    dataGridFormatter = databaseMgr.getDataGrid(strDataGrid, strDataGridColumnsTypes, strDataGridColumnsHeaderStrings, strDataGridColumnsLabels, intDataGridColumnsWidths, intDataGridColumnsSort);
	    dataGridFormatter.setEmptyLabel(strDataGridEmptyLabel);
	    /*
	     * Set a key provider that provides a unique key for each contact. If key is
	     * used to identify contacts when fields (such as the name and address)
	     * change.
	     */
	    dataGrid = new DataGrid<Equipment_i>(pageSize, GRID_RESOURCES);
	    dataGrid.addStyleName("project-"+strDataGrid+"-table");
	    
	    /*
	     * Do not refresh the headers every time the data is updated. The footer
	     * depends on the current data, so we do not disable auto refresh on the
	     * footer.
	     */
//	    dataGrid.setAutoHeaderRefreshDisabled(true);
	    
	    // Set the message to display when the table is empty.
	    dataGrid.setEmptyTableWidget(new Label(dataGridFormatter.getEmptyLabel()));
	    
	    dataGrid.setRowStyles(new RowStyles<Equipment_i>() {
	    	
	    	final String strCssPrefix = strDataGrid;

			@Override
			public String getStyleNames(Equipment_i row, int rowIndex) {
				String strCssResult = strCssPrefix;
				
				if ( (strDataGridCssFlagColumns != null) && (strDataGridCssFlagColumns.length > 0) ){
					for (int i=0; i<strDataGridCssFlagColumns.length; i++){
						int colNum = Integer.parseInt(strDataGridCssFlagColumns[i]);
						String label = strDataGridColumnsLabels[colNum];
						if (strDataGridColumnsTypes[colNum].compareToIgnoreCase("Number") == 0) {
							Number value = row.getNumberValue(label);
							strCssResult += " " + strCssPrefix + "_" + label + "_" + value.toString();
							logger.info(className, function, "strCssResult (Number) is:[{}]", strCssResult);
						} else if (strDataGridColumnsTypes[colNum].compareToIgnoreCase("Boolean") == 0) {
							Boolean value = row.getBooleanValue(label);
							strCssResult += " " + strCssPrefix + "_" + label + "_" + value.toString();
							logger.info(className, function, "strCssResult (Boolean) is:[{}]", strCssResult);
						} else {
							String value = row.getStringValue(label);
							if (value != null && !value.isEmpty()) {
								// Replace space with '_'
								value.replace(' ','_');
								strCssResult += " " + strCssPrefix + "_" + label + "_" + value;
								logger.info(className, function, "strCssResult(Other) Type:[{}] is:[{}]", strDataGridColumnsTypes[colNum] ,strCssResult);
							}
						}
					}
				}
				/*	
				for (int i=0; i<strDataGridColumnsLabels.length; i++) {
					String label = strDataGridColumnsLabels[i];
					
					if (strDataGridColumnsTypes[i].compareToIgnoreCase("Number") == 0) {
						Number value = row.getNumberValue(label);
						strCssResult += " " + strCssPrefix + "_" + label + "_" + value.toString();
						logger.info(className, function, "strCssResult (Number) is:[{}]", strCssResult);
					} else if (strDataGridColumnsTypes[i].compareToIgnoreCase("Boolean") == 0) {
						Boolean value = row.getBooleanValue(label);
						strCssResult += " " + strCssPrefix + "_" + label + "_" + value.toString();
						logger.info(className, function, "strCssResult (Boolean) is:[{}]", strCssResult);
					} else {
						String value = row.getStringValue(label);
						if (value != null && !value.isEmpty()) {
							// Replace space with '_'
							value.replace(' ','_');
							strCssResult += " " + strCssPrefix + "_" + label + "_" + value;
							logger.info(className, function, "strCssResult (Other) is:[{}]", strCssResult);
						}
					}
				}
				*/
				
                return strCssResult;
			}
	    	
	    });

	    // Attach a column sort handler to the ListDataProvider to sort the list.
//	    ListHandler<ContactInfo> sortHandler =
//	        new ListHandler<ContactInfo>(ContactDatabase.get().getDataProvider().getList());
//	    dataGrid.addColumnSortHandler(sortHandler);
	    
	    // Create a Pager to control the table.
//	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//	    pager.setDisplay(dataGrid);

		singleSelectionModel = new SingleSelectionModel<Equipment_i>();
		singleSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Equipment_i selected = singleSelectionModel.getSelectedObject();
				if (selected != null) {
					
					final String function = "onSelection fireFilterEvent";
					
					logger.begin(className, function);
					
					String actionsetkey = "RowSelected";
					Map<String, Object> parameter = new HashMap<String, Object>();
					parameter.put(ViewAttribute.OperationObject1.toString(), strDataGrid);
					parameter.put(ViewAttribute.OperationObject2.toString(), selected);
					
					Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
					override.put("RowSelected", parameter);
					
					uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

					logger.end(className, function);
				}
			}
		});
		
		dataGrid.setSelectionModel(singleSelectionModel);

//	    initDataGridColumn(selectionModel, header, dataGrid);
	    
	    dataGrid = dataGridFormatter.addDataGridColumn(dataGrid);
	    
	    // Add column field updater for Boolean column
	    int numColumns = dataGridFormatter.getNumberOfColumn();
	    for (int col=0; col<numColumns; col++) {
	    	String colType = dataGridFormatter.getColumnType(col);
	    	if (colType.equals("Boolean")) {
	    		addColumnFieldUpdateHandler(dataGridFormatter.getColumnLabel(col));
	    	}
	    }
   
	    // Add the CellList to the adapter in the database.
	    String dbKey = strDataGrid;
	    uiDataGridDatabase = UIDataGridDatabase.getInstance(dbKey);
	    uiDataGridDatabase.addDataDisplay(dataGrid);
	    uiDataGridDatabase.setScsEnv(strDataGrid, scsEnvIdsStr, strDataGridColumnsLabels, strDataGridColumnsTypes, intDataGridColumnsTranslations, strDataGridOptsXMLFile);
	    uiDataGridDatabase.connect();
	    
	    logger.end(className, function);
	    
	    return dataGrid;
	}
	
	private void createColumnLabelTranslationMap(String [] labels, int [] enableTranslations) {
		if (labels != null && enableTranslations != null) {
			for (int i=0; i<labels.length; i++) {
				if (i < enableTranslations.length) {
					String label = labels[i];
					Boolean enable = enableTranslations[i]==1? Boolean.TRUE : Boolean.FALSE;
					columnLabelTranslationMap.put(label, enable);
				}
			}
		}
	}
	
	private void addPager() {
		final String function = "addPager";
		
		logger.begin(className, function);

		if (strDataGridPagerName.equals("SCADAgenPager")) {
		    pager = new SCADAgenPager();

			if (pager != null) {
				
				pager.setCreateText(new CreateText_i() {
				
					@Override
					public String CreateText(int pageStart, int endIndex, boolean exact, int dataSize) {
					    NumberFormat formatter = NumberFormat.getFormat("#,###");

					    return formatter.format(pageStart) + "-" + formatter.format(endIndex)
					        + (exact ? " of " : " of over ") + formatter.format(dataSize);
					}
	
					@Override
					public void pageStart(int pageStart) {
						final String function = "CreateText pageStart";
						
						logger.begin(className, function);
						
						String strType = "PageStart";
						
						logger.debug(className, function, "Type[{}]", strType);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(pageStart));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(className, function);
					}
	
					@Override
					public void endIndex(int endIndex) {
						final String function = "CreateText endIndex";
						
						logger.begin(className, function);
						
						String strType = "EndIndex";
						
						logger.debug(className, function, "strType[{}]", strType);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(endIndex));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(className, function);
					}
	
					@Override
					public void exact(boolean exact, int dataSize) {
						final String function = "CreateText exact";
						
						logger.begin(className, function);
						
						String strType = "Exact";
						
						logger.debug(className, function, "exact[{}] dataSize[{}]", exact, dataSize);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(dataSize));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(className, function);
					}
				});

			
				pager.setButtonOperation(new ButtonOperation_i() {
						
					@Override
					public void buttonOperation(String operation, boolean status) {
						final String function = "buttonOperation";
						
						logger.begin(className, function);
						
						logger.debug(className, function, "operation[{}] status[{}]", operation, status);
						
						String actionsetkey = "PagerButtonChanged_"+operation;
						String actionkey = "PagerButtonChanged_"+operation;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), operation);
						parameter.put(ActionAttribute.OperationString2.toString(), Boolean.toString(status));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(className, function);
					}
				});
			
			    logger.debug(className, function, "pageSize[" + pageSize + "]");
				pager.setPageSize(pageSize);
				
				logger.debug(className, function, "fastForwardRows[" + fastForwardRows + "]");
				pager.setFastForwardRows(fastForwardRows);
				
				pager.setDisplay(dataGrid);
				
				pager.ensureDebugId(this.element + "Pager");
			}
		}
	    
	    logger.end(className, function);
	}
	
	public void addColumnFieldUpdateHandler(final String columnLabel) {
		final String function = "addColumnFieldUpdateHandler";
		
		logger.begin(className, function);

		int index = -1;
		
		for (int i=0; i<strDataGridColumnsLabels.length;i++) {
			if (strDataGridColumnsLabels[i].equals(columnLabel)) {
				index = i;
				logger.debug(className, function, "label [{}] found", columnLabel);
				break;
			}
		}

		if (index >= 0) {
			Column<Equipment_i, Boolean> column = (Column<Equipment_i, Boolean>)dataGrid.getColumn(index);
			
			column.setFieldUpdater(new FieldUpdater<Equipment_i, Boolean>() {
	
				@Override
				public void update(int paramInt, Equipment_i paramT, Boolean paramC) {
					Boolean oldValue = paramT.getBooleanValue(columnLabel);
					String paramString = "index=" + String.valueOf(paramInt) + " oldValue=" + String.valueOf(oldValue) + " newValue=" + String.valueOf(paramC);
					//Window.alert(paramString);
					logger.debug(className, function, "update [{}]", paramString);
					paramT.setBooleanValue(columnLabel, paramC);
					
					UIEventAction dataGridValueChangeEvent = new UIEventAction();
					if (dataGridValueChangeEvent != null) {
						dataGridValueChangeEvent.setParameter(ViewAttribute.OperationString1.toString(), DataGridEvent.ValueChange.toString());
						dataGridValueChangeEvent.setParameter(ViewAttribute.OperationObject1.toString(), strDataGrid);
						dataGridValueChangeEvent.setParameter(ViewAttribute.OperationObject2.toString(), columnLabel);
						dataGridValueChangeEvent.setParameter(ViewAttribute.OperationObject3.toString(), paramT);
						dataGridValueChangeEvent.setParameter(ViewAttribute.OperationObject4.toString(), paramC);
						getEventBus().fireEvent(dataGridValueChangeEvent);
						logger.debug(className, function, "fire UIEventAction dataGridValueChangeEvent");
					}
				}
			});
		} else {
			logger.debug(className, function, "label [{}] not found", columnLabel);
		}
		logger.end(className, function);

	}
	
	private EventBus getEventBus() {
		return this.eventBus;
	}
	
}
