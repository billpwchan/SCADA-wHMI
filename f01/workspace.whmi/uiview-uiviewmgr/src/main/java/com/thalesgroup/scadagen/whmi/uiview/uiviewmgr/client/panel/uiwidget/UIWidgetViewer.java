package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.ColumnFilterData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.FilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringEnumFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringFilterDescription.StringFilterTypes;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
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
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class UIWidgetViewer extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus		= null;

	private UILayoutGeneric uiLayoutGeneric	= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor uiEventActionProcessor = null;
	
	private ScsOlsListPanel scsOlsListPanel					= null;
	private ScsAlarmDataGridPresenterClient gridPresenter	= null;
	
	public void removeFilter() {
		final String function = "removeFilter";
		
		logger.begin(className, function);
		if ( null != gridPresenter ) {
			LinkedList<String> entitles = gridPresenter.getFilterColumns();
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

	UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
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
							String listConfigId = scsOlsListPanel.getStringParameter("listConfigId_");
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
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				return message;
			}
		});
		
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
			gridPresenter = scsOlsListPanel.getPresenter();
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
						
						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
						if ( null != columns && columns.size() > 0 ) {
							viewerViewEvent = ViewerViewEvent.FilterAdded;
						}
						String actionsetkey = viewerViewEvent.toString();
						uiEventActionProcessor.executeActionSet(actionsetkey);

						logger.end(className, function);
					}
				});
			} else {
				logger.warn(className, function, "gridPresenter columns.size()");
			}
		} else {
			logger.warn(className, function, "scsOlsListPanel columns.size()");
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
