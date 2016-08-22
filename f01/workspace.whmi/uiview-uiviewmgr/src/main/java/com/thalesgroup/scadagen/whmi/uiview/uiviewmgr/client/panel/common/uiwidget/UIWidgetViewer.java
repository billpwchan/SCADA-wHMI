package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.ColumnFilterData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.PrintViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.View_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class UIWidgetViewer extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus		= null;
	
	private String listConfigId			= "ptwdciset";
	private String menuEnable			= "false";
	private String selectionMode		= "Multiple";
	
	private UILayoutGeneric uiLayoutGeneric					= null;
	
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
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		String op = (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
		String od1 = (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
		String od2 = (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
		
		logger.warn(className, function, "op[{}]", op);
		logger.warn(className, function, "od1[{}]", od1);
		logger.warn(className, function, "od2[{}]", od2);
		
		if ( null != op ) {
			if ( op.equals(FilterViewEvent.AddFilter.toString()) ) {
				if ( null != od1 && null != od2) {
					applyFilter(od1, od2);
				} else if ( null == od1 ) {
					logger.warn(className, function, "od1 IS NULL");
				} else if ( null == od2 ) {
					logger.warn(className, function, "od2 IS NULL");
				}
			} else if ( op.equals(FilterViewEvent.RemoveFilter.toString()) ) {
				removeFilter();
			} else if ( op.equals(PrintViewEvent.Print.toString()) ) {
				Window.alert("Print Event");
			}
		} else {
			logger.warn(className, function, "op IS NULL");
		}
		
		logger.end(className, function);
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		if ( containsParameterKey(ParameterName.SimpleEventBus.toString()) ) {
			Object o = parameters.get(ParameterName.SimpleEventBus.toString());
			if ( null != o ) {
				String eventBusName = (String) o;
				this.eventBus = UIEventActionBus.getInstance().getEventBus(eventBusName);
			}
		}
		
		if ( containsParameterKey(ParameterName.ListConfigId.toString()) ) {
			Object o = parameters.get(ParameterName.ListConfigId.toString());
			if ( null != o ) {
				this.listConfigId = (String) o;
			}
		}
		
		if ( containsParameterKey(ParameterName.MenuEnable.toString()) ) {
			Object o = parameters.get(ParameterName.MenuEnable.toString());
			if ( null != o ) {
				this.menuEnable = (String) o;
			}
		}
		
		if ( containsParameterKey(ParameterName.SelectionMode.toString()) ) {
			Object o = parameters.get(ParameterName.SelectionMode.toString());
			if ( null != o ) {
				this.selectionMode = (String) o;
			}
		}

		logger.info(className, function, "this.listConfigId1[{}]", this.listConfigId);
		logger.info(className, function, "this.menuEnable[{}]", this.menuEnable);
		logger.info(className, function, "this.selectionMode[{}]", this.selectionMode);
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				return message;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		
		UIWidgetMgr.getInstance().addUIWidgetFactory(className, new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String widget) {
				UIWidget_i uiWidget_i = null;
				if ( 0 == widget.compareTo(ViewWidget.ScsOlsListPanel.toString()) ) {
					uiWidget_i = new ScsOlsListPanel();
					uiWidget_i.setParameter(ParameterName.MwtEventBus.toString(), new MwtEventBus());
					uiWidget_i.setParameter(ParameterName.ListConfigId.toString(), listConfigId);
					uiWidget_i.setParameter(ParameterName.MenuEnable.toString(), menuEnable);
					uiWidget_i.setParameter(ParameterName.SelectionMode.toString(), selectionMode);
					uiWidget_i.init();
				}
				return uiWidget_i;
			}
		});
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(xmlFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);
			
		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);
		
		scsOlsListPanel = (ScsOlsListPanel)uiLayoutGeneric.getPredefineWidget(ViewWidget.ScsOlsListPanel.toString());
		
		if ( null != scsOlsListPanel ) {
			gridPresenter = scsOlsListPanel.getPresenter();
			if ( null != gridPresenter ) {
				gridPresenter.setSelectionEvent(new SelectionEvent() {

					@Override
					public void onSelection(Set<HashMap<String, String>> entities) {
						final String function = "onSelection fireFilterEvent";
						
						logger.begin(className, function);

						UIEventAction uiEventAction = new UIEventAction();
						uiEventAction.setParameters(ViewAttribute.Operation.toString(), ViewerViewEvent.RowSelected.toString());
						uiEventAction.setParameters(ViewAttribute.OperationObject1.toString(), entities);
						eventBus.fireEventFromSource(uiEventAction, this);
						
						logger.end(className, function);
					}
				});
				
				gridPresenter.setFilterEvent(new FilterEvent() {
					
					@Override
					public void onFilterChange(ArrayList<String> columns) {
						final String function = "onFilterChange fireFilterEvent";

						logger.begin(className, function);
						
						UIEventAction event = new UIEventAction();
						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
						if ( null != columns && columns.size() > 0 ) {
							viewerViewEvent = ViewerViewEvent.FilterAdded;
						}
						event.setParameters(ViewAttribute.Operation.toString(), viewerViewEvent.toString());
						eventBus.fireEventFromSource(event, this);
						
						logger.end(className, function);
					}
				});
			} else {
				logger.warn(className, function, "gridPresenter columns.size()");
			}
		} else {
			logger.warn(className, function, "scsOlsListPanel columns.size()");
		}
		
		logger.end(className, function);
	}
}
