package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.ArrayList;
import java.util.LinkedList;
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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i.GDGAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i.ParameterName;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i.ParameterValue;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsAlarmDataGridPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

/**
 * A widget displaying an alamm list panel.
 */
public class ScsOlsListPanel extends UIWidget_i {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName());

    /** Logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "["+className+"] ";

	private String strOlsCssPrefix = "OLS_LIST_";
    
    private String selectionMode = null;
    private boolean hasSCADAgenPager = false;
    
    private String debugId = null;
    
    private LinkedList<String> attributes = new LinkedList<String>();
    
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
    private EventBus eventBus;
    
    public EventBus getEventBus() {
    	return eventBus;
    }

    /**
     * Used to remove event handler
     */
    private List<HandlerRegistration> handlerRegistrations_;

    /**
     * The alarm list context menu
     */
    private ScsOlsListPanelMenu contextMenu_;
    
    public ScsOlsListPanelMenu getContextMenu() {
    	return contextMenu_;
    }
    
    private String menuEnableCallImage = null;

    /**
     * The configuration id of the datagrid
     */
    private String listConfigId_;
    /**
     * The view that represents the alarm datagrid
     */
    private ScsGenericDataGridView gridView_;
    
    public ScsGenericDataGridView getView() { return gridView_; }
    
    public ScsAlarmDataGridPresenterClient getPresenter() { return gridPresenter_; }

    private boolean isTerminated_ = false;
    public boolean isTerminated() {
        return isTerminated_;
    }
    
    private ISelectionModel getSelectionModel(String key) {
    	ISelectionModel iSelectionModel = null;
        if ( null != key ) {
        	if ( ParameterValue.Multiple.toString().equalsIgnoreCase(key) ) {
        		iSelectionModel = new MultipleSelectionModel();
        	} else if ( ParameterValue.Single.toString().equalsIgnoreCase(key) ) {
        		iSelectionModel = new SingleSelectionModel();
        	}
        }
        return iSelectionModel;
    }
    
    private void setDebugId(ScsGenericDataGridView gridView_) {
        LOGGER.debug(LOG_PREFIX + "setDebugId debugId["+debugId+"]");
        String strDebugId = uiNameCard.getUiPath()+uiNameCard.getUiScreen();
        if ( null != debugId ) {
        	strDebugId = debugId;
        }
        LOGGER.debug(LOG_PREFIX + "setDebugId strDebugId["+strDebugId+"]");
        gridView_.ensureDebugId(strDebugId);
    }

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter() {
        if (listConfigId_ != null && gridView_ != null && eventBus != null ) {
            gridPresenter_ = new ScsAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus);
            
            ISelectionModel iSelectionModel = null;
            iSelectionModel = getSelectionModel(selectionMode);
            if ( null != iSelectionModel ) {
            	gridPresenter_.setSelectionModel(iSelectionModel);
            } else {
            	LOGGER.debug(LOG_PREFIX + " initPresenter : iSelectionModel IS null");
            }
            
            if ( null != contextMenu_) {
            	gridPresenter_.setMenu(contextMenu_);
            } else {
            	LOGGER.debug(LOG_PREFIX + " initPresenter : contextMenu_ IS null");
            }

            initHandler();
        } else {
            LOGGER.warn(LOG_PREFIX + " initPresenter : listConfigId_ gridView_ eventBus_ should not be null");
        }
    }

    private void initHandler() {
        if (eventBus != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
            handlerRegistrations_.add(eventBus.addHandler(FilterChangeEventAbstract.TYPE, gridPresenter_));
        } else {
            LOGGER.warn(LOG_PREFIX + " eventBus IS null");
        }
    }
    
    /**
     * Initialize and create all needed components
     */
    private void initComponents() {
    	if ( null != menuEnableCallImage && menuEnableCallImage.equalsIgnoreCase(ParameterValue.True.toString())) {
    		contextMenu_ = new ScsOlsListPanelMenu(eventBus);
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
        
        String cssWithElement = "project-"+className+"-"+element;
        LOGGER.debug(LOG_PREFIX + "getStyleNames className["+className+"] element["+element+"] cssWithElement["+cssWithElement+"]");
        rootPanel.addStyleName(cssWithElement);
        
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
        
        setDebugId(gridView_);
       
        gridView_.setHasSCADAgenPager(hasSCADAgenPager);
        
        // Customize CSS class according to the alarm state
        gridView_.setRowStyles(new RowStyles<EntityClient>() {

            @Override
            public String getStyleNames(EntityClient row, int rowIndex) {

        		String strCssResult = strOlsCssPrefix + listConfigId_;
        		
        		java.util.Iterator<String> it = attributes.iterator();
        		while ( it.hasNext() ) {
        			
        			String strGDGAttribute = it.next();
        			
                	LOGGER.debug(LOG_PREFIX + "getStyleNames rowIndex["+rowIndex+"] strGDGAttribute["+strGDGAttribute+"]");
                	
                	strCssResult += " " + strOlsCssPrefix + strGDGAttribute;
                			
					AttributeClientAbstract<?> gdgAttribute = row.getAttribute(strGDGAttribute);
					if ( null != gdgAttribute && gdgAttribute.isValid() ) {
						strCssResult += "_" + gdgAttribute.getValue();
					}

        		}

                LOGGER.debug(LOG_PREFIX + "getStyleNames strCssResult["+strCssResult+"]");
                
                return strCssResult;
 
            }
        });
    }
    
	@Override
	public void init() {
        
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			listConfigId_			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ListConfigId.toString(), strHeader);
			menuEnableCallImage		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MenuEnableCallImage.toString(), strHeader);
			selectionMode			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.SelectionMode.toString(), strHeader);
			
			debugId					= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DebugId.toString(), strHeader);

			String mwtEventBusName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusName.toString(), strHeader);
			String mwtEventBusScope	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusScope.toString(), strHeader);
			
			LOGGER.debug(LOG_PREFIX + "init listConfigId_["+listConfigId_+"]");
			LOGGER.debug(LOG_PREFIX + "init menuEnableCallImage["+menuEnableCallImage+"]");
			LOGGER.debug(LOG_PREFIX + "init selectionMode["+selectionMode+"]");
			
			LOGGER.debug(LOG_PREFIX + "init debugId["+debugId+"]");
			
			LOGGER.debug(LOG_PREFIX + "init mwtEventBusName["+mwtEventBusName+"]");
			LOGGER.debug(LOG_PREFIX + "init mwtEventBusScope["+mwtEventBusScope+"]");
			
			if ( null == mwtEventBusName || mwtEventBusName.trim().length() == 0) {
				mwtEventBusName = this.viewXMLFile;
			}
			if ( mwtEventBusScope != null && ParameterValue.Global.toString().equals(mwtEventBusScope) ) {
			} else {
				mwtEventBusName = mwtEventBusName + "_" + uiNameCard.getUiScreen();
			}
			LOGGER.debug(LOG_PREFIX + "init mwtEventBusName["+mwtEventBusName+"]");
			
			eventBus = MwtEventBuses.getInstance().getEventBus(mwtEventBusName);

			String tmpStrOlsCssPrefix = dictionariesCache.getStringValue(optsXMLFile, ParameterName.OlsCssPrefix.toString(), strHeader);
			if ( null != tmpStrOlsCssPrefix ) strOlsCssPrefix = tmpStrOlsCssPrefix;
			
			String pagerName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.PagerName.toString(), strHeader);
			if ( null != pagerName && ParameterValue.SCADAgenPager.toString().equals(pagerName) ) {
				this.hasSCADAgenPager = true;
			}
			
            for ( String strGDGAttribute : GDGAttribute.toStrings() ) {
            	
            	String gdgAttribute	= dictionariesCache.getStringValue(optsXMLFile, strGDGAttribute, strHeader);
            	
            	LOGGER.debug(LOG_PREFIX + "init gdgAttribute["+gdgAttribute+"]");
            	
            	if ( null != gdgAttribute ) {
            		
            		LOGGER.debug(LOG_PREFIX + "init gdgAttribute["+gdgAttribute+"] Added");
            		
            		attributes.add(gdgAttribute);
            	}
            	
            }
			
			setParameter(ParameterName.ListConfigId.toString(), listConfigId_);
			setParameter(ParameterName.MenuEnableCallImage.toString(), menuEnableCallImage);
			setParameter(ParameterName.SelectionMode.toString(), selectionMode);
			setParameter(ParameterName.EventBus.toString(), eventBus);
		}
        initComponents();
        initPresenter();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        isTerminated_ = true;
        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        try {
            gridPresenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
        	LOGGER.error(LOG_PREFIX + "Error while trying to terminate the Alarm List Panel.", e);
        }
    }

}
