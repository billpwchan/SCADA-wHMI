package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common.PTWCommonEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common.PTWCommonEventHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.AbsolutePanelOlsList;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.FilterEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class PTWViewer implements UIWidget_i {
	
    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

	// External
	private SimpleEventBus eventBus = null;
	
	public SimpleEventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(SimpleEventBus eventBus) {
		this.eventBus = eventBus;
	}

	private String listConfigId = null;
	public String getListConfigId() { return listConfigId; }
	public void setListConfigId(String listConfigId) { this.listConfigId = listConfigId; }
	
	private String listConfigId2 = null;
	public String getListConfigId2() { return listConfigId2; }
	public void setListConfigId2(String listConfigId2) { this.listConfigId2 = listConfigId2; }

	private ComplexPanel root = null;
	public PTWViewer (SimpleEventBus eventBus, String listConfigId) {
		this( eventBus, listConfigId, listConfigId);
	}
	
	public PTWViewer (SimpleEventBus eventBus, String listConfigId, String listConfigId2) {
		
		this.eventBus = eventBus;
		this.listConfigId = listConfigId;
		this.listConfigId2 = listConfigId2;
		
		s_logger.debug(PTWViewer.class.getName() + "this.listConfigId[" + this.listConfigId + "]");
		
		s_logger.debug(PTWViewer.class.getName() + "this.listConfigId2[" + this.listConfigId2 + "]");
		
		this.eventBus.addHandler(PTWCommonEvent.TYPE, new PTWCommonEventHandler() {
			
			@Override
			public void onOperation(PTWCommonEvent ptwCommonEvent) {

				if ( ptwCommonEvent.getSource() != this ) {
					
					String operation = ptwCommonEvent.getOperation();
					String operationDetails = ptwCommonEvent.getOperationDetails();
					
					s_logger.debug(PTWViewer.class.getName() + "Operation: [" + operation + "]");
					
					s_logger.debug(PTWViewer.class.getName() + "Operation Details: [" + operationDetails + "]");
					
					if ( null != operation ) {
						if ( null != operationDetails ) {
							if ( "Filter".equals(operation) ) {
								if ( "NAN".equals(operationDetails) ) {
									removeFilter();
								}
							}
						}
					}
					
				} else {
					
					s_logger.debug(PTWViewer.class.getName() + "Event from itself");
					
				}
			}
		});
	}
	
	private void fireSelecionEvent(HashMap<String, String> entities) {
		String message = "NAN";
		if ( null != entities && entities.size() > 0 ) {
			message = entities.get("alias");
		}
		if ( null != entities ) {
			for ( String key :  entities.keySet() ) {
				if ( "alias".equals(key) ) {
					message = entities.get(key);
				}
			}
		}
		this.eventBus.fireEventFromSource(new PTWCommonEvent("Selection", message), this);
	}
	
	private void fireFilterEvent(ArrayList<String> columns) {
		String message = "NAN";
		if ( null != columns && columns.size() > 0 ) {
			message = "Set";
		}
		this.eventBus.fireEventFromSource(new PTWCommonEvent("Filter", message), this);
		
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

	private UINameCard uiNameCard = null;
	public void setUINameCard (UINameCard uiNameCard) { 
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}

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
	public void init(String xmlFile) {
		
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
				UIWidget_i uiWidget = null;
				if ( 0 == widget.compareTo("AbsolutePanelOlsList") ) {
					uiWidget = new AbsolutePanelOlsList(getListConfigId(), getListConfigId2());
					uiWidget.init(null);
				}
				return uiWidget;
			}
		});
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.init(strUIPanelPTWViewer);
		ComplexPanel complexPanel = uiLayoutGeneric.getMainPanel();
		
		uiPanelGenericButton	= uiLayoutGeneric.getUIWidget(strUIPanelPTWViewerButton);
	
		if ( null != uiPanelGenericButton ) {
			uiPanelGenericButton.setUIWidgetEvent(new UIWidgetEvent() {
				
				@Override
				public void onClickHandler(ClickEvent event) {
					
					Widget widget = (Widget) event.getSource();
					String element = uiPanelGenericButton.getWidgetElement(widget);
					if ( null != element ) {
						if ( 0 == element.compareTo("ptwset") ) {
							
							gridView10.getMainPanel().addStyleName("project-gwt-panel-ptwviewer-grid-invisible");
							gridView10.getMainPanel().removeStyleName("project-gwt-panel-ptwviewer-grid-visible");
							
							gridView12.getMainPanel().removeStyleName("project-gwt-panel-ptwviewer-grid2-invisible");
							gridView12.getMainPanel().addStyleName("project-gwt-panel-ptwviewer-grid2-visible");							
							
						} else if ( 0 == element.compareTo("ptwunset") ){

							gridView10.getMainPanel().addStyleName("project-gwt-panel-ptwviewer-grid-visible");
							gridView10.getMainPanel().removeStyleName("project-gwt-panel-ptwviewer-grid-invisible");
							
							gridView12.getMainPanel().removeStyleName("project-gwt-panel-ptwviewer-grid2-visible");
							gridView12.getMainPanel().addStyleName("project-gwt-panel-ptwviewer-grid2-invisible");
							
						}
					}
				}
				
				@Override
				public void onKeyPressHandler(KeyPressEvent event) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onValueChange(String name, String value) {
					// TODO Auto-generated method stub
					
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
		
		root = new VerticalPanel();
		root.addStyleName("project-gwt-panel-ptwviewer-root");
		root.add(complexPanel);

	}

	@Override
	public ComplexPanel getMainPanel() {
		return root;
	}

	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(String key, String value) {
		// TODO Auto-generated method stub
		
	}

}
