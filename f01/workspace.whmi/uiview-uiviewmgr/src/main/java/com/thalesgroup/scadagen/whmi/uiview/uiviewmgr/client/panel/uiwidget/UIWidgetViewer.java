package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.FilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.IntFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringEnumFilterDescription;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.control.PrintGDGPage;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UILayoutRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.event.ScsOlsListPanelMenuHandler;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanelMenu;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.CounterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.SelectionEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event.UpdateEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ButtonOperation_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.CreateText_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.SCADAgenPager;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

public class UIWidgetViewer extends UILayoutRealize {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private UIEventActionProcessor_i uiEventActionProcessorContextMenu_i = null;
	
	private String scsOlsListElement = null;
	
	private ScsOlsListPanel scsOlsListPanel					= null;
	private ScsAlarmDataGridPresenterClient gridPresenter	= null;
	private ScsGenericDataGridView gridView					= null;
	private ScsOlsListPanelMenu contextMenu					= null;
	
	private boolean enableRowUpdated = false;
	
	private String printDataDebugId	= null;
	private String printDataColumns	= null;
	private String printDataIndexs	= null;
	private String printDataAttachement = null;
	private String printDataDivIndexes = null;
	
	private int printDataStart = 0;
	private int printDataLength = 8000;
	
	private int printDataReceviedWait = 5000;
	private int printDataWalkthoughWait = 5000;
	
	private String menuHandlerUIOpts = "";
	
	PrintGDGPage printGDGPage = null;
	
	public void removeFilter() {
		final String function = "removeFilter";
		
		logger.begin(function);
		if ( null != gridPresenter ) {
			Set<String> entitles = gridPresenter.getFilterColumns();
			for ( String entitle : entitles ) {
				logger.warn(function, "entitle[{}]", entitle);
				gridPresenter.removeContainerFilter(entitle);
			}
		} else {
			logger.warn(function, "gridPresenter IS NULL");
		}
		logger.end(function);
	}
	
	public void applyFilter(String column, String value) {
		final String function = "applyFilter";
		
		logger.begin(function);
		logger.debug(function, "column[{}] value[{}]", column, value);
		
		FilterDescription fd = null;

		Set<String> s = new HashSet<String>();
		s.add(value);
		fd = new StringEnumFilterDescription(s);
		
		gridPresenter.setContainerFilter(column, fd);
		
		logger.end(function);
	}
	
	public void applyFilter(String column, int start, int end) {
		final String function = "applyFilter";
		
		logger.begin(function);
		logger.debug(function, "column[{}] start[{}] end[{}]", new Object[]{column, start, end});

		FilterDescription fd = new IntFilterDescription(start, end);

		gridPresenter.setContainerFilter(column, fd);

		logger.end(function);
	}

	@Override
	public void init() {
		super.init();
		
		final String function = "init";		
		logger.begin(function);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			scsOlsListElement			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.ScsOlsListElement.toString(), strHeader);
		
			String strEnableRowUpdated	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.EnableRowUpdated.toString(), strHeader);
			if ( null != strEnableRowUpdated && ! strEnableRowUpdated.isEmpty() ) {
				enableRowUpdated = Boolean.parseBoolean(strEnableRowUpdated);
			}
			
			printDataDebugId			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataDebugId.toString(), strHeader);
			printDataColumns			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataColumns.toString(), strHeader);
			printDataIndexs				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataIndexs.toString(), strHeader);
			printDataAttachement		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataAttachement.toString(), strHeader);
			printDataDivIndexes			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataDivIndexes.toString(), strHeader);
			
			String strPrintDataStart	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataStart.toString(), strHeader);
			try {
				printDataStart = Integer.parseInt(strPrintDataStart);
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "sPrintDataStart[{}] IS INVALID", strPrintDataStart);
			}
			
			String strPrintDataLength	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataLength.toString(), strHeader);
			try {
				printDataLength = Integer.parseInt(strPrintDataLength);
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strPrintDataLength[{}] IS INVALID", strPrintDataLength);
			}
			
			String strPrintDataReceviedWait	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataReceviedWait.toString(), strHeader);
			try {
				printDataReceviedWait = Integer.parseInt(strPrintDataReceviedWait);
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strPrintDataReceviedWait[{}] IS INVALID", strPrintDataReceviedWait);
			}			
			
			String strPrintDataWalkthoughWait	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.PrintDataWalkthoughWait.toString(), strHeader);
			try {
				printDataWalkthoughWait = Integer.parseInt(strPrintDataWalkthoughWait);
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strPrintDataWalkthoughWait[{}] IS INVALID", strPrintDataWalkthoughWait);
			}
			
			menuHandlerUIOpts		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetViewer_i.ParameterName.MenuHandlerUIOpts.toString(), strHeader);
		}
		logger.debug(function, "scsOlsListElement[{}]", scsOlsListElement);
		logger.debug(function, "enableRowUpdated[{}]", enableRowUpdated);
		
		logger.debug(function, "printDataDebugId[{}]", printDataDebugId);
		logger.debug(function, "printDataColumns[{}]", printDataColumns);
		logger.debug(function, "printDataIndexs[{}]", printDataIndexs);
		logger.debug(function, "printDataAttachement[{}]", printDataAttachement);
		logger.debug(function, "printDataDivIndexes[{}]", printDataDivIndexes);
		
		logger.debug(function, "printDataStart[{}]", printDataStart);
		logger.debug(function, "printDataLength[{}]", printDataLength);
		
		logger.debug(function, "printDataReceviedWait[{}]", printDataReceviedWait);
		logger.debug(function, "printDataWalkthoughWait[{}]", printDataWalkthoughWait);
		
		logger.debug(function, "menuHandlerUIOpts[{}]", menuHandlerUIOpts);
		
		if ( null == scsOlsListElement ) {
			
			logger.warn(function, "scsOlsListElement IS NULL");
			
			scsOlsListElement = ScsOlsListPanel.class.getSimpleName();
			
			logger.warn(function, "Using default ScsOlsListPanel ClassName for scsOlsListElement[{}] AS DEFAULT", scsOlsListElement);
		}
		
		Object object = ((UILayoutGeneric)uiGeneric).getUIWidget(scsOlsListElement);
		if ( null != object ) {
			if ( object instanceof ScsOlsListPanel ) {
				scsOlsListPanel = (ScsOlsListPanel)object;
			} else {
				logger.warn(function, "scsOlsListElement[{}] instanceof ScsOlsListPanel IS FALSE");
			}
		} else {
			logger.warn(function, "scsOlsListElement[{}] IS NULL");
		}
		
		if ( null != scsOlsListPanel ) {
			
			gridView = scsOlsListPanel.getView();
			gridPresenter = scsOlsListPanel.getPresenter();
			contextMenu = scsOlsListPanel.getContextMenu();
			
			if ( null != gridPresenter ) {
				
				gridPresenter.setUpdateEvent(new UpdateEvent() {

					@Override
					public void onUpdate(Set<Map<String, String>> entities) {
						final String function = "onUpdate fireUpdateEvent";
						logger.begin(function);
						
						if ( enableRowUpdated ) {
							
							String actionsetkey = "RowUpdated";
							Map<String, Object> parameter = new HashMap<String, Object>();
							parameter.put(ViewAttribute.OperationObject1.toString(), entities);
							
							Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
							override.put("RowUpdated", parameter);
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						}
						
						logger.end(function);
					}
				});
				
				gridPresenter.setSelectionEvent(new SelectionEvent() {

					@Override
					public void onSelection(Set<Map<String, String>> entities) {
						final String function = "onSelection fireFilterEvent";
						logger.begin(function);
						
						String actionsetkey = "RowSelected";
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ViewAttribute.OperationObject1.toString(), entities);
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put("RowSelected", parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

						logger.end(function);
					}
				});
				
				gridPresenter.setFilterEvent(new FilterEvent() {
					
					@Override
					public void onFilterChange(ArrayList<String> columns) {
						final String function = "onFilterChange fireFilterEvent";
						logger.begin(function);
						
						// Dump
						logger.debug(function, "columns.size[{}]", columns.size());
						for ( String column : columns ) {
							logger.debug(function, "column[{}]", column);
						}
						
						
						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
						if ( null != columns && columns.size() > 0 ) {
							viewerViewEvent = ViewerViewEvent.FilterAdded;
						}
						String actionsetkey = viewerViewEvent.toString();
						uiEventActionProcessor_i.executeActionSet(actionsetkey);

						logger.end(function);
					}
				});
				
				gridPresenter.setCounterEvent(new CounterEvent() {
					
					@Override
					public void onCounterChange(Map<String, Integer> countersValue) {
						final String function = "onCounterChange fire onCounterChange";
						logger.begin(function);
						
						if ( null != countersValue ) {
							for ( Entry<String, Integer> keyValue : countersValue.entrySet() ) {
								
								String key = keyValue.getKey();
								Integer value = keyValue.getValue();
								String strValue = value.toString();
								
								logger.debug(function, "key[{}] value[{}] strValue[{}]", new Object[]{key, value, strValue});								
								
								String actionsetkey = "CounterValueChanged";
								String actionkey = "SetWidgetValue";
								Map<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString2.toString(), key);
								parameter.put(ActionAttribute.OperationString3.toString(), strValue);
								
								Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
								
							}
						}

						logger.end(function);
					}
				});
				
				gridView.setButtonOperation(new ButtonOperation_i() {
					
					@Override
					public void buttonOperation(String operation, boolean status) {
						final String function = "setButtonOperation buttonOperation";
						logger.begin(function);
						
						logger.debug(function, "operation[{}] status[{}]", operation, status);
						
						String actionsetkey = "PagerButtonChanged_"+operation;
						String actionkey = "PagerButtonChanged_"+operation;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), operation);
						parameter.put(ActionAttribute.OperationString2.toString(), Boolean.toString(status));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(function);
					}
				});
				
				gridView.setCreateText(new CreateText_i() {
					
					@Override
					public String CreateText(int pageStart, int endIndex, boolean exact, int dataSize) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void pageStart(int pageStart) {
						final String function = "setCreateText pageStart";
						logger.begin(function);
						
						String strType = "PageStart";
						
						logger.debug(function, "Type[{}]", strType);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(pageStart));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(function);
					}

					@Override
					public void endIndex(int endIndex) {
						final String function = "setCreateText endIndex";
						logger.begin(function);
						
						String strType = "EndIndex";
						
						logger.debug(function, "strType[{}]", strType);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(endIndex));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(function);
					}

					@Override
					public void exact(boolean exact, int dataSize) {
						final String function = "setCreateText exact";
						logger.begin(function);
						
						String strType = "Exact";
						
						logger.debug(function, "exact[{}] dataSize[{}]", exact, dataSize);
						
						String strPageValueChanged = "PagerValueChanged_";
						String actionsetkey = strPageValueChanged+strType;
						String actionkey = strPageValueChanged+strType;
						Map<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ActionAttribute.OperationString1.toString(), strPageValueChanged+strType);
						parameter.put(ActionAttribute.OperationString2.toString(), Integer.toString(dataSize));
						
						Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
						override.put(actionkey, parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(function);
					}
				});

			} else {
				logger.warn(function, "gridPresenter IS NULL");
			}
			
			
			if ( null != contextMenu ) {
				
				logger.debug(function, "Init uiEventActionProcessorContextMenu");

				UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
				uiEventActionProcessorContextMenu_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");
				
				uiEventActionProcessorContextMenu_i.setUINameCard(uiNameCard);
				uiEventActionProcessorContextMenu_i.setPrefix(className);
				uiEventActionProcessorContextMenu_i.setElement(element);
				uiEventActionProcessorContextMenu_i.setDictionariesCacheName("UIWidgetGeneric");
//				uiEventActionProcessorContextMenu.setEventBus(eventBus);
				uiEventActionProcessorContextMenu_i.setOptsXMLFile(menuHandlerUIOpts);
//				uiEventActionProcessorContextMenu.setUIGeneric(uiWidgetGeneric);
				uiEventActionProcessorContextMenu_i.setActionSetTagName(UIActionEventType.actionset.toString());
				uiEventActionProcessorContextMenu_i.setActionTagName(UIActionEventType.action.toString());
				uiEventActionProcessorContextMenu_i.init();

	            contextMenu.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
	    			
	    			@Override
	    			public void onSelection(Set<Map<String, String>> entity) {
	    				logger.warn(function, "entity[{}]", entity);
	    				
	    				if ( null != entity ) {
	    					Map<String, String> map = entity.iterator().next();
	    					if ( null != map ) {
	    						String actionsetkey = map.get("sourceID");
	    						logger.debug(function, "actionsetkey[{}]", actionsetkey);
	    						if ( null != actionsetkey ) {
	    							if ( null != uiEventActionProcessorContextMenu_i ) {
	    								uiEventActionProcessorContextMenu_i.executeActionSet(actionsetkey);
	    							} else {
	    								logger.warn(function, "uiEventActionProcessorContextMenu IS NULL");	
	    							}
	    						} else {
	    							logger.warn(function, "sourceID IS NULL");	
	    						}
	    					} else {
	    						logger.warn(function, "hashMap IS NULL");	
	    					}
	    				} else {
	    					logger.warn(function, "entity IS NULL");	
	    				}
	    			}
	    		});
			} else {
				logger.warn(function, "contextMenu IS NULL");
			}
			
			printGDGPage = new PrintGDGPage(gridView, uiEventActionProcessor_i);
			printGDGPage.setGDGParameter(printDataDebugId);
			printGDGPage.setPageContentParameter(printDataColumns, printDataIndexs);
			printGDGPage.setPageRangeParameter(printDataStart, printDataLength);
			printGDGPage.setTimerParameter(printDataReceviedWait, printDataWalkthoughWait);
			printGDGPage.setDownloadParameter(printDataAttachement);
			printGDGPage.setDivIndexsParameter(printDataDivIndexes);
			
		} else {
			logger.warn(function, "scsOlsListPanel IS NULL");
		}
	
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
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
				
				logger.begin(function);
				
				if ( null != uiEventAction ) {
					
					String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
					String os2 = (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
					String os3 = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
					String os4 = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
					String os5 = (String) uiEventAction.getParameter(ActionAttribute.OperationString5.toString());
					
					logger.debug(function, "os1[{}]", os1);
					logger.debug(function, "os2[{}]", os2);
					logger.debug(function, "os3[{}]", os3);
					logger.debug(function, "os4[{}]", os4);
					logger.debug(function, "os5[{}]", os5);
					
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.debug(function, "oe[{}] element[{}]", oe, element);
					
					if ( null != oe ) {
						
						if ( oe.equals(element) ) {
					
							if ( null != os1 ) {
						
								AbstractPager pager = gridView.getPager();
								
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
										else
										if ( os1.equals(ViewerViewEvent.FastBackwardPageSelected.toString()) ) {
												
											simplePager.fastBackwardPage();

										}
									} else {
										logger.warn(function, "pager instanceof SimplePager IS NOT");
									}
									

								} else {
									logger.warn(function, "pager IS NULL");
								}
							}
						}
					}
					
					if ( null != os1 ) {
						
						if ( os1.equals(FilterViewEvent.AddIntRangeFilter.toString()) ) {
							if ( null != os2 && null != os3 && null != os4 && null != os5 ) {
								String listConfigId = scsOlsListPanel.getStringParameter(ScsOlsListPanel_i.ParameterName.ListConfigId.toString());
								logger.debug(function, "listConfigId[{}]", listConfigId);
								if ( null != listConfigId ) {
									if ( os2.equals(listConfigId) ) {
										int start = 0;
										int end = 0;
										
										try {
											start = Integer.parseInt(os4);
										} catch ( NumberFormatException ex ) {
											logger.warn(function, "os4[{}] IS INVALID", os4);
											logger.warn(function, "NumberFormatException[{}]", ex.toString());
										}
										
										try {
											end = Integer.parseInt(os5);
										} catch ( NumberFormatException ex ) {
											logger.warn(function, "os5[{}] IS INVALID", os5);
											logger.warn(function, "NumberFormatException[{}]", ex.toString());
										}
										
										logger.debug(function, "start[{}] end[{}]", start, end);
										
										applyFilter(os3, start, end);
										
									} else {
										logger.warn(function, "od1[{}] AND listConfigId[{}] IS NOT EQUALS", os1, listConfigId);
									}
								} else {
									logger.warn(function, "listConfigId IS NULL", listConfigId);
								}
			
							} else if ( null == os2 ) {
								logger.warn(function, "od1 IS NULL");
							} else if ( null == os3 ) {
								logger.warn(function, "od2 IS NULL");
							}
						} 
						else
						if ( os1.equals(FilterViewEvent.AddFilter.toString()) ) {
							if ( null != os2 && null != os3 && null != os4) {
								String listConfigId = scsOlsListPanel.getStringParameter(ScsOlsListPanel_i.ParameterName.ListConfigId.toString());
								logger.debug(function, "listConfigId[{}]", listConfigId);
								if ( null != listConfigId ) {
									if ( os2.equals(listConfigId) ) {
										applyFilter(os3, os4);
									} else {
										logger.warn(function, "od1[{}] AND listConfigId[{}] IS NOT EQUALS", os1, listConfigId);
									}
								} else {
									logger.warn(function, "listConfigId IS NULL", listConfigId);
								}
			
							} else if ( null == os2 ) {
								logger.warn(function, "od1 IS NULL");
							} else if ( null == os3 ) {
								logger.warn(function, "od2 IS NULL");
							}
						} 
						else if ( os1.equals(FilterViewEvent.RemoveFilter.toString()) ) {
							removeFilter();
						} 
						else if ( os1.equals(ViewerViewEvent.AckVisible.toString()) ) {
							if ( null != gridView ) {
								boolean validRequested = false, isValid = false;
								String attributeName = null;
								int start = -1, end = -1;
								if ( null != os2 && null != os3 && null != os4 ) {
									validRequested = true;
									attributeName = os2;
									try {
										start = Integer.parseInt(os3);
										end = Integer.parseInt(os4);
										isValid = true;
									} catch ( NumberFormatException ex ) {
										logger.warn(function, "ex:", ex.toString());
									}
								} else {
									logger.warn(function, "os2[{}] os3[{}] os4[{}]", new Object[]{os2, os3, os4});
								}
								logger.warn(function, "validRequested[{}] isValid[{}]", validRequested, isValid);
								if ( validRequested && isValid ) {
									gridView.ackVisible(attributeName, start, end);
								} else {
									gridView.ackVisible();
								}
							} else {
								logger.warn(function, "gridView IS NULL");
							}
						} 
						else if ( os1.equals(ViewerViewEvent.AckVisibleSelected.toString()) ) {
							if ( null != gridView ) {
								boolean validRequested = false, isValid = false;
								String attributeName = null;
								int start = -1, end = -1;
								if ( null != os2 && null != os3 && null != os4 ) {
									validRequested = true;
									attributeName = os2;
									try {
										start = Integer.parseInt(os3);
										end = Integer.parseInt(os4);
										isValid = true;
									} catch ( NumberFormatException ex ) {
										logger.warn(function, "ex:", ex.toString());
									}
								}
								logger.warn(function, "validRequested[{}] isValid[{}]", validRequested, isValid);
								if ( validRequested && isValid ) {
									gridView.ackVisibleSelected(attributeName, start, end);
								} else {
									gridView.ackVisibleSelected();
								}
							} else {
								logger.warn(function, "gridView IS NULL");
							}
						} 
						else if ( os1.equals(ViewerViewEvent.SetPageSize.toString())) {
							
							int startIndex = Integer.parseInt(os2);
							int pageSize = Integer.parseInt(os3);
							
							printGDGPage.setPageSize(startIndex, pageSize, false);
						}
						else if ( os1.equals(ViewerViewEvent.Print.toString()) ) {
							
							printGDGPage.setPageSizePrint();

						}
						else if ( os1.equals(ViewerViewEvent.PrintCurPage.toString()) ) {
							 
							printGDGPage.printCurPage();
							 
						}
					} else {
						logger.warn(function, "os1 IS NULL");
					}
				} else {
					logger.warn(function, "uiEventAction IS NULL");
				}
				
				logger.end(function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {

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
				if ( null != scsOlsListPanel ) {
					scsOlsListPanel.terminate();
					scsOlsListPanel = null;
				}
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
