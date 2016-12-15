package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.ColumnFilterData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.FilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringEnumFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringFilterDescription.StringFilterTypes;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint_i.PrintViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.event.ScsOlsListPanelMenuHandler;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanelMenu;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.CounterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

public class UIWidgetViewer extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String contextMenuOptsXMLFile = "UIEventActionProcessor_CallImage.opts.xml";

	// External
	private SimpleEventBus eventBus		= null;

	private UILayoutGeneric uiLayoutGeneric	= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor uiEventActionProcessor = null;
	private UIEventActionProcessor uiEventActionProcessorContextMenu = null;
	
	private ScsOlsListPanel scsOlsListPanel					= null;
	private ScsAlarmDataGridPresenterClient gridPresenter	= null;
	private ScsGenericDataGridView gridView					= null;
	private ScsOlsListPanelMenu contextMenu					= null;
	
	public void removeFilter() {
		final String function = "removeFilter";
		
		logger.begin(className, function);
		if ( null != gridPresenter ) {
			Set<String> entitles = gridPresenter.getFilterColumns();
			for ( String entitle : entitles ) {
				logger.warn(className, function, "entitle[{}]", entitle);
				gridPresenter.removeContainerFilter(entitle);
			}
		} else {
			logger.warn(className, function, "gridPresenter IS NULL");
		}
		logger.end(className, function);
	}
	
	public void applyFilter(String column, String value) {
		final String function = "applyFilter";
		
		logger.begin(className, function);
		logger.info(className, function, "column[{}] value[{}]", column, value);

		ColumnFilterData cfd = new ColumnFilterData(column, value);
		
		FilterDescription fd = null;
		boolean isEnumType = true;
		if ( isEnumType ) {
			Set<String> s = new HashSet<String>();
			s.add(value);
			fd = new StringEnumFilterDescription(s);			
		} else {
			fd = new StringFilterDescription(StringFilterTypes.EQUALS, cfd.getFilterValue());
		}
		
		gridPresenter.setContainerFilter(cfd.getColumnName(), fd);
		logger.info(className, function, "column[{}] value[{}]", column, value);
		logger.end(className, function);
	}

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
					if ( os1.equals(FilterViewEvent.AddFilter.toString()) ) {
						if ( null != os2 && null != os3 && null != os4) {
							String listConfigId = scsOlsListPanel.getStringParameter(ScsOlsListPanel_i.ParameterName.ListConfigId.toString());
							logger.info(className, function, "listConfigId[{}]", listConfigId);
							if ( null != listConfigId ) {
								if ( os2.equals(listConfigId) ) {
									applyFilter(os3, os4);
								} else {
									logger.warn(className, function, "od1[{}] AND listConfigId[{}] IS NOT EQUALS", os1, listConfigId);
								}
							} else {
								logger.warn(className, function, "listConfigId IS NULL", listConfigId);
							}
		
						} else if ( null == os2 ) {
							logger.warn(className, function, "od1 IS NULL");
						} else if ( null == os3 ) {
							logger.warn(className, function, "od2 IS NULL");
						}
					} else if ( os1.equals(FilterViewEvent.RemoveFilter.toString()) ) {
						removeFilter();
					} else if ( os1.equals(ViewerViewEvent.AckVisible.toString()) ) {
						if ( null != gridView ) { 
							gridView.ackVisible();
						} else {
							logger.warn(className, function, "gridView IS NULL");
						}
					} else if ( os1.equals(ViewerViewEvent.AckVisibleSelected.toString()) ) {
						if ( null != gridView ) { 
							gridView.ackVisibleSelected();
						} else {
							logger.warn(className, function, "gridView IS NULL");
						}
					} else if ( os1.equals(PrintViewEvent.Print.toString()) ) {
						Window.alert("Print Event");
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
		
		String strEventBusName = getStringParameter(WidgetParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);
		
		uiLayoutGeneric = new UILayoutGeneric();
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiEventActionProcessor = new UIEventActionProcessor();
		uiEventActionProcessor.setUINameCard(uiNameCard);
		uiEventActionProcessor.setPrefix(className);
		uiEventActionProcessor.setElement(element);
		uiEventActionProcessor.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor.setEventBus(eventBus);
		uiEventActionProcessor.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor.setUIWidgetGeneric(uiWidgetGeneric);
		uiEventActionProcessor.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor.init();
		
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
		
		String strScsOlsListPanel = UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName());
		scsOlsListPanel = (ScsOlsListPanel)uiLayoutGeneric.getPredefineWidget(strScsOlsListPanel);
		
		if ( null != scsOlsListPanel ) {
			
			gridView = scsOlsListPanel.getView();
			gridPresenter = scsOlsListPanel.getPresenter();
			contextMenu = scsOlsListPanel.getContextMenu();
			
			if ( null != gridPresenter ) {
				gridPresenter.setSelectionEvent(new SelectionEvent() {

					@Override
					public void onSelection(Set<HashMap<String, String>> entities) {
						final String function = "onSelection fireFilterEvent";
						
						logger.begin(className, function);
						
						String actionsetkey = "RowSelected";
						HashMap<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ViewAttribute.OperationObject1.toString(), entities);
						
						HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
						override.put("RowSelected", parameter);
						
						uiEventActionProcessor.executeActionSet(actionsetkey, override);

						logger.end(className, function);
					}
				});
				
				gridPresenter.setFilterEvent(new FilterEvent() {
					
					@Override
					public void onFilterChange(ArrayList<String> columns) {
						final String function = "onFilterChange fireFilterEvent";

						logger.begin(className, function);
						
						// Dump
						logger.debug(className, function, "columns.size[{}]", columns.size());
						for ( String column : columns ) {
							logger.debug(className, function, "column[{}]", column);
						}
						
						
						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
						if ( null != columns && columns.size() > 0 ) {
							viewerViewEvent = ViewerViewEvent.FilterAdded;
						}
						String actionsetkey = viewerViewEvent.toString();
						uiEventActionProcessor.executeActionSet(actionsetkey);

						logger.end(className, function);
					}
				});
				
				gridPresenter.setCounterEvent(new CounterEvent() {
					
					@Override
					public void onCounterChange(Map<String, Integer> countersValue) {
						final String function = "onCounterChange fire onCounterChange";
						
						logger.begin(className, function);
						
						if ( null != countersValue ) {
							for ( Entry<String, Integer> keyValue : countersValue.entrySet() ) {
								
								String key = keyValue.getKey();
								Integer value = keyValue.getValue();
								String strValue = value.toString();
								
								String actionsetkey = "CounterValueChanged";
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ViewAttribute.OperationString2.toString(), key);
								parameter.put(ViewAttribute.OperationString3.toString(), strValue);
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put("CounterValueChanged", parameter);
								
								uiEventActionProcessor.executeActionSet(actionsetkey, override);
								
							}
						}

						logger.end(className, function);
					}
				});

			} else {
				logger.warn(className, function, "gridPresenter IS NULL");
			}
			
			
			if ( null != contextMenu ) {
				
				logger.info(className, function, "Init uiEventActionProcessorContextMenu");

				uiEventActionProcessorContextMenu = new UIEventActionProcessor();
				uiEventActionProcessorContextMenu.setUINameCard(uiNameCard);
				uiEventActionProcessorContextMenu.setPrefix(className);
				uiEventActionProcessorContextMenu.setElement(element);
				uiEventActionProcessorContextMenu.setDictionariesCacheName("UIWidgetGeneric");
//				uiEventActionProcessorContextMenu.setEventBus(eventBus);
				uiEventActionProcessorContextMenu.setOptsXMLFile(contextMenuOptsXMLFile);
//				uiEventActionProcessorContextMenu.setUIWidgetGeneric(uiWidgetGeneric);
				uiEventActionProcessorContextMenu.setActionSetTagName(UIActionEventType.actionset.toString());
				uiEventActionProcessorContextMenu.setActionTagName(UIActionEventType.action.toString());
				uiEventActionProcessorContextMenu.init();

	            contextMenu.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
	    			
	    			@Override
	    			public void onSelection(Set<HashMap<String, String>> entity) {
	    				logger.warn(className, function, "entity[{}]", entity);
	    				
	    				if ( null != entity ) {
	    					HashMap<String, String> hashMap = entity.iterator().next();
	    					if ( null != hashMap ) {
	    						String sourceID = hashMap.get("sourceID");
	    						logger.info(className, function, "sourceID[{}]", sourceID);
	    						if ( null != sourceID ) {
	    							if ( null != uiEventActionProcessorContextMenu ) {
	    								uiEventActionProcessorContextMenu.executeActionSet(sourceID);
	    							} else {
	    								logger.warn(className, function, "uiEventActionProcessorContextMenu IS NULL");	
	    							}
	    						} else {
	    							logger.warn(className, function, "sourceID IS NULL");	
	    						}
	    					} else {
	    						logger.warn(className, function, "hashMap IS NULL");	
	    					}
	    				} else {
	    					logger.warn(className, function, "entity IS NULL");	
	    				}
	    			}
	    		});
			} else {
				logger.warn(className, function, "contextMenu IS NULL");
			}
			
		} else {
			logger.warn(className, function, "scsOlsListPanel IS NULL");
		}
		
		uiEventActionProcessor.executeActionSetInit();
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
		if ( null != scsOlsListPanel ) {
			scsOlsListPanel.terminate();
		}
	}
}
