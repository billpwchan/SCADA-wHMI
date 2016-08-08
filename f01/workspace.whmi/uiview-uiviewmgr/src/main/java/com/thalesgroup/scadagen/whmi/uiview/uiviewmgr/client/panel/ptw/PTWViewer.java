package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.PTW_i.PTW;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.PTW_i.PTWOperation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.PTW_i.PTWOperationDetail;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.AbsolutePanelOlsList;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class PTWViewer extends UIWidget_i {
	
    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
    
    private String panelname = "ptwviewer";
    
    private String logPrefix = "PTWViewer";

	// External
	private SimpleEventBus eventBus = null;
	public SimpleEventBus getEventBus() { return eventBus; }
	public void setEventBus(SimpleEventBus eventBus) { this.eventBus = eventBus; }

	private String listConfigId = null;
	public String getListConfigId() { return listConfigId; }
	public void setListConfigId(String listConfigId) { this.listConfigId = listConfigId; }
	
	private String listConfigId2 = null;
	public String getListConfigId2() { return listConfigId2; }
	public void setListConfigId2(String listConfigId2) { this.listConfigId2 = listConfigId2; }

	public PTWViewer (SimpleEventBus eventBus, String listConfigId) {
		this( eventBus, listConfigId, listConfigId);
	}
	
	public PTWViewer (SimpleEventBus eventBus, String listConfigId, String listConfigId2) {
		
		this.eventBus = eventBus;
		this.listConfigId = listConfigId;
		this.listConfigId2 = listConfigId2;
		
		s_logger.debug(logPrefix + "this.listConfigId[" + this.listConfigId + "]");
		
		s_logger.debug(logPrefix + "this.listConfigId2[" + this.listConfigId2 + "]");
		
		this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
			
			@Override
			public void onAction(UIEventAction uiEventAction) {
				if ( uiEventAction.getSource() != this ) {
					String op = uiEventAction.getAction(PTW.Operation.toString());
					String od1 = uiEventAction.getAction(PTW.OperationDetail1.toString());
//					String od2 = uiEventAction.getAction(PTW.OperationDetail2.toString());
					
					s_logger.debug(logPrefix + PTW.Operation.toString() + ": [" + op + "]");
					s_logger.debug(logPrefix + PTW.OperationDetail1.toString() + " : [" + od1 + "]");
//					s_logger.debug(logPrefix + PTW.OperationDetail2.toString() + " : [" + od2 + "]");
					
					if ( null != op ) {
						if ( null != od1 ) {
							if ( PTWOperation.Filter.toString().equals(op) ) {
								if ( PTWOperationDetail.NAN.toString().equals(od1) ) {
									removeFilter();
								}
							} else {
								s_logger.error(logPrefix + "operationDetail IS NULL");
							}
						} else {
							s_logger.error(logPrefix + "operation IS NULL");
						}
					}
				} else {
					s_logger.debug(logPrefix + "Event from itself");
				}
			}
		});

	}
	
	private void fireSelecionEvent(HashMap<String, String> entities) {
		String column_alias		= "ptwdciset_alias";
		String column_status	= "ptwdciset_value";
		String alias			= PTWOperationDetail.NAN.toString();
		String status			= PTWOperationDetail.NAN.toString();
		if ( null != entities && entities.size() > 0 ) {
			if ( entities.containsKey(column_alias) ) 	alias	= entities.get(column_alias);
			if ( entities.containsKey(column_status) ) 	status	= entities.get(column_status);
		}
		
		UIEventAction uiEventAction = new UIEventAction();
		uiEventAction.setAction(PTW.Operation.toString(), PTWOperation.Selection.toString());
		uiEventAction.setAction(PTW.OperationDetail1.toString(), alias);
		uiEventAction.setAction(PTW.OperationDetail2.toString(), status);
		
		this.eventBus.fireEventFromSource(uiEventAction, this);
	}
	
	private void fireFilterEvent(ArrayList<String> columns) {
		String message = PTWOperationDetail.NAN.toString();
		if ( null != columns && columns.size() > 0 ) {
			message = PTWOperationDetail.Set.toString();
		}
		
		UIEventAction event = new UIEventAction();
		event.setAction(PTW.Operation.toString(), PTWOperation.Filter.toString());
		event.setAction(PTW.OperationDetail1.toString(), message);
		
		this.eventBus.fireEventFromSource(event, this);
		
	}
	
	private String strUIPanelPTWViewer				= "UIPanelPTWViewer.xml";
	private String strUIPanelPTWViewerButton		= "UIPanelPTWViewerButton.xml";
	
	private UILayoutGeneric uiLayoutGeneric			= null;
	private UIWidget_i uiPanelGenericButton			= null;

	private AbsolutePanelOlsList absolutePanelOlsList		= null;
	
	private ScsOlsListPanel gridView10						= null;
	private ScsAlarmDataGridPresenterClient gridPresenter10	= null;	
	
	private ScsOlsListPanel gridView12						= null;
	private ScsAlarmDataGridPresenterClient gridPresenter12	= null;	

	public void removeFilter() {

		if ( null != gridPresenter10 ) {
			LinkedList<String> columns = gridPresenter10.getFilterColumns();
			removeFilter(columns);
		}
		if ( null != gridPresenter12 ) {
			LinkedList<String> columns = gridPresenter12.getFilterColumns();
			removeFilter(columns);
		}
	}
	
	public void removeFilter(LinkedList<String> entitles) {

		if ( null != gridPresenter10 ) {
			for ( String entitle : entitles ) {
				gridPresenter10.removeContainerFilter(entitle);
			}
		}
		if ( null != gridPresenter12 ) {
			for ( String entitle : entitles ) {
				gridPresenter12.removeContainerFilter(entitle);
			}
		}
	}

	@Override
	public void init() {
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				return message;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		
		UIWidgetMgr.getInstance().addUIWidgetFactory(strUIPanelPTWViewer, new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String widget) {
				UIWidget_i uiWidget_i = null;
				if ( 0 == widget.compareTo("AbsolutePanelOlsList") ) {
					uiWidget_i = new AbsolutePanelOlsList(getListConfigId(), getListConfigId2());
					uiWidget_i.init();
				}
				return uiWidget_i;
			}
		});
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(strUIPanelPTWViewer);
		uiLayoutGeneric.init();
		ComplexPanel complexPanel = uiLayoutGeneric.getMainPanel();
		
		uiPanelGenericButton	= uiLayoutGeneric.getUIWidget(strUIPanelPTWViewerButton);
	
		if ( null != uiPanelGenericButton ) {
			uiPanelGenericButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
				
				@Override
				public void onClickHandler(ClickEvent event) {
					Widget widget = (Widget) event.getSource();
					String element = uiPanelGenericButton.getWidgetElement(widget);
					if ( null != element ) {
						if ( 0 == element.compareTo( PTWOperationDetail.Set.toString() ) ) {
							
							gridView10.getMainPanel().addStyleName("project-gwt-panel-"+panelname+"-grid-invisible");
							gridView10.getMainPanel().removeStyleName("project-gwt-panel-"+panelname+"-grid-visible");
							
							gridView12.getMainPanel().removeStyleName("project-gwt-panel-"+panelname+"-grid2-invisible");
							gridView12.getMainPanel().addStyleName("project-gwt-panel-"+panelname+"-grid2-visible");							
							
						} else if ( 0 == element.compareTo( PTWOperationDetail.UnSet.toString() ) ){

							gridView10.getMainPanel().addStyleName("project-gwt-panel-"+panelname+"-grid-visible");
							gridView10.getMainPanel().removeStyleName("project-gwt-panel-"+panelname+"-grid-invisible");
							
							gridView12.getMainPanel().removeStyleName("project-gwt-panel-"+panelname+"-grid2-visible");
							gridView12.getMainPanel().addStyleName("project-gwt-panel-"+panelname+"-grid2-invisible");
							
						}
					}
				}
			});
			
		} else {
//			logger.log(Level.SEVERE, "getMainPanel uiPanelGeneric.get(strUIPanelLoginButton) IS NULL");
		}
		
		absolutePanelOlsList = (AbsolutePanelOlsList)uiLayoutGeneric.getPredefineWidget("AbsolutePanelOlsList");
		
		gridView10 = (ScsOlsListPanel)absolutePanelOlsList.getUIWidget();
				
		gridPresenter10 = gridView10.getPresenter();
		
		if ( null != gridPresenter10 ) {
			
			gridPresenter10.setSelectionEvent(new SelectionEvent() {

				@Override
				public void onSelection(HashMap<String, String> entities) {
					
					s_logger.debug(ScsOlsListPanel.class.getName() + entities.toString());
					
					fireSelecionEvent(entities);
				}
			});
			
			gridPresenter10.setFilterEvent(new FilterEvent() {
				
				@Override
				public void onFilterChange(ArrayList<String> columns) {
					
					s_logger.debug(ScsOlsListPanel.class.getName() + columns.size());

					fireFilterEvent(columns);
				}
			});
		}
		
		gridView12 = (ScsOlsListPanel)absolutePanelOlsList.getUIWidget2();
		
		gridPresenter12 = gridView12.getPresenter();
		
		if ( null != gridPresenter12 ) {
			
			gridPresenter12.setSelectionEvent(new SelectionEvent() {

				@Override
				public void onSelection(HashMap<String, String> entities) {
					
					s_logger.debug(ScsOlsListPanel.class.getName() + entities.toString());
					
					fireSelecionEvent(entities);
				}
			});
			
			gridPresenter12.setFilterEvent(new FilterEvent() {
				
				@Override
				public void onFilterChange(ArrayList<String> columns) {
					
					s_logger.debug(ScsOlsListPanel.class.getName() + columns.size());

					fireFilterEvent(columns);
				}
			});
		}
		
		rootPanel = new VerticalPanel();
		rootPanel.addStyleName("project-gwt-panel-"+panelname+"-root");
		rootPanel.add(complexPanel);

	}

}
