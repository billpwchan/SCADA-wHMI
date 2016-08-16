package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterChangeEventAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.ISelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.SingleSelectionModel;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

/**
 * A widget displaying an alamm list panel.
 */
public class ScsOlsListPanel extends UIWidget_i {

    /** Logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[ScsOlsListPanel] ";
    
    private final String strTrue = "true";
    private final String strMultiple		= "Multiple";
    
    private final String strMwtEventBus		= "MwtEventBus";
    private final String strListConfigId	= "ListConfigId";
    private final String strMenuEnable		= "MenuEnable";
    private final String strSelectionMode	= "SelectionMode";
    
    private String selectionMode = null;
    
    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
//    private HasWidgets mainPanel_;

    /**
     * s Client presenter of this alarm list widget
     */
    private ScsAlarmDataGridPresenterClient gridPresenter_;

    /**
     * Bus used to subscribe to and publish alarm-related events
     */
    private EventBus eventBus_;

    /**
     * Used to remove event handler
     */
    private List<HandlerRegistration> handlerRegistrations_;

    /**
     * The alarm list context menu
     */
    private ScsOlsListPanelMenu contextMenu_;
    
    private String menuEnable = null;

    /**
     * The configuration id of the datagrid
     */
    private String listConfigId_;

    /**
     * The view that represents the alarm datagrid
     */
    private ScsGenericDataGridView gridView_;

    private boolean isTerminated_ = false;
    public boolean isTerminated() {
        return isTerminated_;
    }

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter() {
        if (listConfigId_ != null && gridView_ != null && eventBus_ != null ) {
            gridPresenter_ = new ScsAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus_);
            
            ISelectionModel iSelectionModel = null;
            if ( null != selectionMode ) {
            	if ( selectionMode.equals(strMultiple) ) {
            		iSelectionModel = new MultipleSelectionModel();
            	} else {
            		iSelectionModel = new SingleSelectionModel();
            	}
            }
            if ( null != iSelectionModel ) {
            	gridPresenter_.setSelectionModel(iSelectionModel);
            } else {
            	LOGGER.error(LOG_PREFIX
                        + " initPresenter : iSelectionModel IS null");
            }
            
            if ( null != contextMenu_) {
            	gridPresenter_.setMenu(contextMenu_);
            } else {
            	LOGGER.error(LOG_PREFIX
                        + " initPresenter : contextMenu_ IS null");
            }

            initHandler();
        } else {
            LOGGER.error(LOG_PREFIX
                    + " initPresenter : listConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
    }

    private void initHandler() {
        if (eventBus_ != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
            handlerRegistrations_.add(eventBus_.addHandler(FilterChangeEventAbstract.TYPE, gridPresenter_));

//            handlerRegistrations_.add(
//            		eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, new AlarmSelectionChangeEvent.AlarmSelectionEventHandler() {
//				
//						@Override
//						public void onSelectionChange(AlarmSelectionChangeEvent event) {
//							LOGGER.error(LOG_PREFIX
//				                    + " AlarmSelectionChangeEvent AlarmSelectionEventHandler onSelectionChange Begin");
//							if ( null != event ) {
//								LOGGER.error(LOG_PREFIX
//				                    + " AlarmSelectionChangeEvent AlarmSelectionEventHandler onSelectionChange ["+event+"]");
//							} else {
//								LOGGER.error(LOG_PREFIX
//				                    + " AlarmSelectionChangeEvent AlarmSelectionEventHandler onSelectionChange event IS NULL");
//							}
//							LOGGER.error(LOG_PREFIX
//				                    + " AlarmSelectionChangeEvent AlarmSelectionEventHandler onSelectionChange End");
//						}
//					})
//            );
//
//            handlerRegistrations_.add(
//            		eventBus_.addHandler(FilterChangeEventAbstract.TYPE, new FilterChangeEventAbstract.Handler() {
//				
//						@Override
//						public void onFilterChange(FilterChangeEventAbstract event) {
//							LOGGER.error(LOG_PREFIX  + " FilterChangeEventAbstract onFilterChange Begin");
//							if ( null != event ) {
//								if (event instanceof FilterSetEvent) {
//						            LOGGER.error(LOG_PREFIX + " FilterChangeEventAbstract onFilterChange FilterSetEvent");
//								} else if (event instanceof FilterRemoveEvent) {
//						            LOGGER.error(LOG_PREFIX  + " FilterChangeEventAbstract onFilterChange FilterRemoveEvent");
//								} else {
//									LOGGER.error(LOG_PREFIX  + " FilterChangeEventAbstract onFilterChange event IS UNKNOW");
//								}
//							} else {
//								LOGGER.error(LOG_PREFIX  + " FilterChangeEventAbstract onFilterChange event IS NULL");
//							}
//							LOGGER.error(LOG_PREFIX  + " FilterChangeEventAbstract onFilterChange End");
//						}
//					})
//            );
        }
    }
    
    public ScsAlarmDataGridPresenterClient getPresenter() {
    	return gridPresenter_;
    }

    /**
     * Initialize and create all needed components
     */
    private void initComponents() {
    	if ( null != menuEnable && menuEnable.equals(strTrue)) {
    		contextMenu_ = new ScsOlsListPanelMenu(eventBus_);
    	}
        initDataGridView();
        initMainPanel();
    }

    /**
     * Create the main container with a Caption or not
     */
    private void initMainPanel() {

        rootPanel = new DockLayoutPanel(Unit.PX);
        rootPanel.addStyleName("project-gwt-panel-scsolslistpanel");
        Widget gridWidget = gridView_.asWidget();
        gridWidget.setVisible(true);

        // Add containers in the main panel
        rootPanel.add(gridWidget);
    }

    /**
     * Create the datagrid view and customize the CSS rules
     */
    private void initDataGridView() {
        gridView_ = new ScsGenericDataGridView();
        // Customize CSS class according to the alarm state
        gridView_.setRowStyles(new RowStyles<EntityClient>() {

            @Override
            public String getStyleNames(EntityClient row, int rowIndex) {
                if (row != null) {
                    for (String attname : row.attributeNames()) {
                        AttributeClientAbstract<Object> att = row.getAttribute(attname);
                        if (!att.isValid()) {
                            return "gdg_invalid";
                        }
                    }
                }

                return "gdg_normal";
            }
        });
    }

	@Override
	public void init() {
        eventBus_			= (EventBus) parameters.get(strMwtEventBus);
        listConfigId_		= (String) parameters.get(strListConfigId);
        menuEnable			= (String) parameters.get(strMenuEnable);
        selectionMode		= (String) parameters.get(strSelectionMode);
        
        initComponents();
        initPresenter();
	}
}
