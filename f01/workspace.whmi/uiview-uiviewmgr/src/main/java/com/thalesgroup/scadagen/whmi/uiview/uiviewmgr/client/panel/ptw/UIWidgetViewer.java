package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.PrintViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class UIWidgetViewer extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(UIWidgetViewer.class.getName());
	
	private String className		= UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName());
	
	private String logPrefix		= "["+className+"] ";

	// External
	private SimpleEventBus eventBus		= null;
	
	private String listConfigId			= "ptwdciset";
	private String menuEnable			= "false";
	private String selectionMode		= "Multiple";
	
	private UILayoutGeneric uiLayoutGeneric					= null;
	
	private ScsOlsListPanel scsOlsListPanel					= null;
	private ScsAlarmDataGridPresenterClient gridPresenter	= null;
	
	public void removeFilter() {
		logger.log(Level.SEVERE, logPrefix+"removeFilter Begin");
		if ( null != gridPresenter ) {
			LinkedList<String> entitles = gridPresenter.getFilterColumns();
			for ( String entitle : entitles ) {
				logger.log(Level.SEVERE, logPrefix+"removeFilter : entitle[" + entitle + "]");
				gridPresenter.removeContainerFilter(entitle);
			}
		}
		logger.log(Level.SEVERE, logPrefix+"removeFilter End");
	}
	
	public void applyFilter(String column, String value) {
		logger.log(Level.SEVERE, logPrefix+"applyFilter : column[" + column + "] value[" + value + "]");

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
		
	}
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	void onActionReceived(UIEventAction uiEventAction) {
		
		logger.log(Level.SEVERE, logPrefix+" onActionReceived Begin");
		
		String op = (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
		String od1 = (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
		String od2 = (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
		
		logger.log(Level.SEVERE, logPrefix+"op[" + op + "]");
		logger.log(Level.SEVERE, logPrefix+"od1[" + od1 + "]");
		logger.log(Level.SEVERE, logPrefix+"od2[" + od2 + "]");
		
		if ( null != op ) {
			if ( op.equals(FilterViewEvent.AddFilter.toString()) ) {
				if ( null != od1 && null != od2) {
					applyFilter(od1, od2);
				} else if ( null == od1 ) {
					logger.log(Level.SEVERE, logPrefix+"od1 IS NULL");
				} else if ( null == od2 ) {
					logger.log(Level.SEVERE, logPrefix+"od2 IS NULL");
				}
			} else if ( op.equals(FilterViewEvent.RemoveFilter.toString()) ) {
				removeFilter();
			} else if ( op.equals(PrintViewEvent.Print.toString()) ) {
				Window.alert("Print Event");
			}
		} else {
			logger.log(Level.SEVERE, logPrefix+"op IS NULL");
		}
		
		logger.log(Level.SEVERE, logPrefix+"onActionReceived End");
	}

	@Override
	public void init() {
		
		logger.log(Level.FINE, logPrefix+"init Begin");
		
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

		logger.log(Level.FINE, logPrefix+"init this.listConfigId1[" + this.listConfigId + "]");
		logger.log(Level.FINE, logPrefix+"init this.menuEnable[" + this.menuEnable + "]");
		logger.log(Level.FINE, logPrefix+"init this.selectionMode[" + this.selectionMode + "]");
		
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
						
						logger.log(Level.FINE, logPrefix+"onSelection columns.size()");
						
						logger.log(Level.SEVERE, logPrefix+"fireSelecionEvent Begin");

						UIEventAction uiEventAction = new UIEventAction();
						uiEventAction.setParameters(ViewAttribute.Operation.toString(), ViewerViewEvent.RowSelected.toString());
						uiEventAction.setParameters(ViewAttribute.OperationObject1.toString(), entities);
						eventBus.fireEventFromSource(uiEventAction, this);
						
						logger.log(Level.SEVERE, logPrefix+"fireSelecionEvent End");
					}
				});
				
				gridPresenter.setFilterEvent(new FilterEvent() {
					
					@Override
					public void onFilterChange(ArrayList<String> columns) {

						logger.log(Level.FINE, logPrefix+"onFilterChange columns.size()");
						
						logger.log(Level.SEVERE, logPrefix+"fireFilterEvent Begin");
						
						UIEventAction event = new UIEventAction();
						ViewerViewEvent viewerViewEvent = ViewerViewEvent.FilterRemoved;
						if ( null != columns && columns.size() > 0 ) {
							viewerViewEvent = ViewerViewEvent.FilterAdded;
						}
						event.setParameters(ViewAttribute.Operation.toString(), viewerViewEvent.toString());
						eventBus.fireEventFromSource(event, this);
						
						logger.log(Level.SEVERE, logPrefix+"fireFilterEvent Begin");
					}
				});
			} else {
				logger.log(Level.SEVERE, logPrefix+"init gridPresenter columns.size()");
			}
		} else {
			logger.log(Level.SEVERE, logPrefix+"init scsOlsListPanel columns.size()");
		}
		
		logger.log(Level.FINE, logPrefix+"init End");
	}
}
