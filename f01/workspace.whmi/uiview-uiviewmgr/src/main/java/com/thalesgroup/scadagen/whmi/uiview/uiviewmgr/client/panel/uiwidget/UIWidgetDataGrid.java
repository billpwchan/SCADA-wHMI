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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridDatabase;
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
	private String strDataGridColumnsType = null;
	private String strDataGridColumnsLabel = null;
	private String strDataGridColumnsWidth = null;
	private String strDataGridColumnsFilter = null;
	private String strDataGridColumnsSourceTableIndex = null;

	private String targetDataGrid		= "";
	private String targetDataGridColumn1 = "";
	private String targetDataGridColumn2 = "";
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected = null;
	
	private final String split = ",";
	
	private DataGrid<Equipment_i> dataGrid = null;
	
	private UIDataGridDatabase uiDataGridDatabase = null;
	
	private String scsEnvIdsStr = null;

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
												
												uiDataGridDatabase = UIDataGridDatabase.getInstance(strDataGrid);
												String scsEnvId = equipmentSelected.getStringValue(targetDataGridColumn1);
												String dbaddress = equipmentSelected.getStringValue(targetDataGridColumn2);												
												String clientKey = strDataGrid + "_" + "RowSelected";
												
												uiDataGridDatabase.loadData(clientKey, scsEnvId, dbaddress);
									
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
			
			strDataGridColumnsType			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsType.toString(), strHeader);
			strDataGridColumnsLabel			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsLabel.toString(), strHeader);
			strDataGridColumnsWidth			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsWidth.toString(), strHeader);
			strDataGridColumnsFilter		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsFilter.toString(), strHeader);
			strDataGridColumnsSourceTableIndex = dictionariesCache.getStringValue(optsXMLFile, ParameterName.DataGridColumnsSourceTableIndex.toString(), strHeader);

			targetDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_A.toString(), strHeader);
			targetDataGridColumn1	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A.toString(), strHeader);
			targetDataGridColumn2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A2.toString(), strHeader);
		}
		
		logger.info(className, function, "strDataGrid[{}]", strDataGrid);
		
		scsEnvIdsStr = getStringParameter(WidgetParameterName.ScsEnvIds.toString());
		logger.debug(className, function, "scsEnvIdsStr [{}]", scsEnvIdsStr);
		logger.debug(className, function, "strDataGridColumnsType [{}]", strDataGridColumnsType);
		logger.debug(className, function, "strDataGridColumnsLabel [{}]", strDataGridColumnsLabel);
		logger.debug(className, function, "strDataGridColumnsWidth [{}]", strDataGridColumnsWidth);
		logger.debug(className, function, "strDataGridColumnsFilter [{}]", strDataGridColumnsFilter);

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
		
				
				logger.info(className, function, "Init uiEventActionProcessorContextMenu");

				uiEventActionProcessorContextMenu_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");
				
				uiEventActionProcessorContextMenu_i.setUINameCard(uiNameCard);
				uiEventActionProcessorContextMenu_i.setPrefix(className);
				uiEventActionProcessorContextMenu_i.setElement(element);
				uiEventActionProcessorContextMenu_i.setDictionariesCacheName("UIWidgetGeneric");
//				uiEventActionProcessorContextMenu.setEventBus(eventBus);
				uiEventActionProcessorContextMenu_i.setOptsXMLFile(optsXMLFile);
//				uiEventActionProcessorContextMenu.setUIGeneric(uiWidgetGeneric);
				uiEventActionProcessorContextMenu_i.setActionSetTagName(UIActionEventType.actionset.toString());
				uiEventActionProcessorContextMenu_i.setActionTagName(UIActionEventType.action.toString());
				uiEventActionProcessorContextMenu_i.init();

		
		uiEventActionProcessor_i.executeActionSetInit();
		
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
		
		logger.info(className, function, "strDataGridColumnsType[{}]", strDataGridColumnsType);
		logger.info(className, function, "strDataGridColumnsLabel[{}]", strDataGridColumnsLabel);
		logger.info(className, function, "strDataGridColumnsWidth[{}]", strDataGridColumnsWidth);
		logger.info(className, function, "strDataGridColumnsFilter[{}]", strDataGridColumnsFilter);
		
		String [] strDataGridColumnsTypes = UIWidgetUtil.getStringArray(strDataGridColumnsType, split);
		String [] strDataGridColumnsLabels = UIWidgetUtil.getStringArray(strDataGridColumnsLabel, split);
		int [] strDataGridColumnsWidths = UIWidgetUtil.getIntArray(strDataGridColumnsWidth, split);
		String [] strDataGridColumnsFilters = UIWidgetUtil.getStringArray(strDataGridColumnsFilter, split);
		String [] strDataGridColumnsSourceTableIndexes = UIWidgetUtil.getStringArray(strDataGridColumnsSourceTableIndex, split);
		
	    UIDataGridDatabaseMgr databaseMgr = UIDataGridDatabaseMgr.getInstance();
	    UIDataGridFomatter_i dataGridFomatter = null;
	    dataGridFomatter = databaseMgr.getDataGrid(strDataGrid, strDataGridColumnsTypes, strDataGridColumnsLabels, strDataGridColumnsWidths);
	    
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
	    //UIDataGridDatabase uiDataGridDatabase = new UIDataGridDatabase();
	    uiDataGridDatabase = UIDataGridDatabase.getInstance(strDataGrid);
	    uiDataGridDatabase.addDataDisplay(dataGrid);
	    uiDataGridDatabase.setScsEnv(strDataGrid, scsEnvIdsStr, strDataGridColumnsLabels, strDataGridColumnsTypes, strDataGridColumnsFilters, strDataGridColumnsSourceTableIndexes);
	    uiDataGridDatabase.connect();
	    
	    logger.end(className, function);
	    
	    return dataGrid;
	}
	
}
