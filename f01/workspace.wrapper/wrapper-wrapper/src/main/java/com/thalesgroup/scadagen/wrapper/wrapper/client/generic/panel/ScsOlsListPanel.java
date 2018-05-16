package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterChangeEventAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.ISelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.SingleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
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

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private String strOlsCssPrefix = "OLS_LIST_";
	private String strDefaultDateTimeValue = "Sat Jan 01 00:00:00 GMT+800 2000";
	private String strDateTimeCssSubfix = "default_time";
    
    private String selectionMode = null;
    private boolean hasSCADAgenPager = false;
    
    private String debugId = null;
    
    private List<String> attributes = new LinkedList<String>();
    
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
    
    private String menuCallImageEnable = null;
    private String menuCallImageLabel = null;
    private String menuCallImageGdgAttribute = null;

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
    	String f = "setDebugId";
        logger.debug(f, "setDebugId debugId[{}]", debugId);
        String strDebugId = uiNameCard.getUiPath()+uiNameCard.getUiScreen();
        if ( null != debugId ) {
        	strDebugId = debugId;
        }
        logger.debug(f, "setDebugId strDebugId[{}]", strDebugId);
        gridView_.ensureDebugId(strDebugId);
    }

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter() {
    	String f = "initPresenter";
    	
        if (listConfigId_ != null && gridView_ != null && eventBus != null ) {
            gridPresenter_ = new ScsAlarmDataGridPresenterClient(listConfigId_, gridView_, eventBus);
            
            ISelectionModel iSelectionModel = null;
            iSelectionModel = getSelectionModel(selectionMode);
            if ( null != iSelectionModel ) {
            	gridPresenter_.setSelectionModel(iSelectionModel);
            } else {
            	logger.debug(f, "iSelectionModel IS null");
            }
            
            if ( null != contextMenu_) {
            	gridPresenter_.setMenu(contextMenu_);
            } else {
            	logger.debug(f, "contextMenu_ IS null");
            }

            initHandler();
        } else {
            logger.warn(f, "listConfigId_ gridView_ eventBus_ should not be null");
        }
    }

    private void initHandler() {
    	String f = "initHandler";
        if (eventBus != null) {
            handlerRegistrations_ = new ArrayList<HandlerRegistration>();
            handlerRegistrations_.add(eventBus.addHandler(AlarmSelectionChangeEvent.TYPE, gridPresenter_));
            handlerRegistrations_.add(eventBus.addHandler(FilterChangeEventAbstract.TYPE, gridPresenter_));
        } else {
            logger.warn(f, " eventBus IS null");
        }
    }
    
    /**
     * Initialize and create all needed components
     */
    private void initComponents() {
    	if ( null != menuCallImageEnable && menuCallImageEnable.equalsIgnoreCase(ParameterValue.True.toString())) {
    		contextMenu_ = new ScsOlsListPanelMenu(menuCallImageLabel, menuCallImageGdgAttribute);
    	}
        initDataGridView();
        initMainPanel();
    }

    /**
     * Create the main container with a Caption or not
     */
    private void initMainPanel() {
    	String f = "initMainPanel";

        rootPanel = new DockLayoutPanel(Unit.PX);
        rootPanel.addStyleName("project-gwt-panel-scsolslistpanel");
        
        String cssWithElement = "project-"+className+"-"+element;
        logger.debug(f, "getStyleNames className[{}] element[{}] cssWithElement[{}]", new Object[]{className, element, cssWithElement});
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
    	final String f = "initDataGridView";

        gridView_ = new ScsGenericDataGridView();
        
        setDebugId(gridView_);
       
        gridView_.setHasSCADAgenPager(hasSCADAgenPager);
        
        // Customize CSS class according to the alarm state
        gridView_.setRowStyles(new RowStyles<EntityClient>() {

            @Override
            public String getStyleNames(EntityClient row, int rowIndex) {
            	final String f2 = f + " getStyleNames";

        		String strCssResult = strOlsCssPrefix + listConfigId_;
        		
        		java.util.Iterator<String> it = attributes.iterator();
        		while ( it.hasNext() ) {
        			
        			String strGDGAttribute = it.next();
        			
                	logger.debug(f2, "rowIndex[{}] strGDGAttribute[{}]", rowIndex, strGDGAttribute);
                	
                	strCssResult += " " + strOlsCssPrefix + strGDGAttribute;
                			
					AttributeClientAbstract<?> gdgAttribute = row.getAttribute(strGDGAttribute);
					if ( null != gdgAttribute && gdgAttribute.isValid() ) {
						if ( gdgAttribute.getValue() instanceof Date ) {
							logger.debug(f2, "gdgAttribute.getValue() [Date] is:" + gdgAttribute.getValue());
							if( gdgAttribute.getValue().toString().equals(strDefaultDateTimeValue)){
								logger.debug(f2, " date value is default value!!!");
								strCssResult += "_" + strDateTimeCssSubfix;
							}
						} else {
							strCssResult += "_" + gdgAttribute.getValue();
						}
					}
        		}

                logger.debug(f2, "strCssResult[{}]", strCssResult);
                
                return strCssResult;
 
            }
        });
    }
    
	@Override
	public void init() {
		String f = "init";
        
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			listConfigId_			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ListConfigId.toString(), strHeader);
			
			menuCallImageEnable			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MenuCallImageEnable.toString(), strHeader);
			menuCallImageLabel			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MenuCallImageLabel.toString(), strHeader);
			menuCallImageGdgAttribute	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MenuCallImageGdgAttribute.toString(), strHeader);
			
			selectionMode			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.SelectionMode.toString(), strHeader);
			
			debugId					= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DebugId.toString(), strHeader);

			String mwtEventBusName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusName.toString(), strHeader);
			String mwtEventBusScope	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusScope.toString(), strHeader);
			
			logger.debug(f, "init listConfigId_[{}]", listConfigId_);
			
			logger.debug(f, "init menuCallImageLabel[{}]", menuCallImageLabel);
			logger.debug(f, "init menuCallImageEnable[{}]", menuCallImageEnable);
			logger.debug(f, "init menuCallImageGdgAttribute[{}]", menuCallImageGdgAttribute);
			
			logger.debug(f, "init selectionMode[{}]", selectionMode);
			
			logger.debug(f, "init debugId[{}]", debugId);
			
			logger.debug(f, "init mwtEventBusName[{}]", mwtEventBusName);
			logger.debug(f, "init mwtEventBusScope[{}]", mwtEventBusScope);
			
			if ( null == mwtEventBusName || mwtEventBusName.trim().length() == 0) {
				mwtEventBusName = this.viewXMLFile;
			}
			if ( mwtEventBusScope != null && ParameterValue.Global.toString().equals(mwtEventBusScope) ) {
			} else {
				mwtEventBusName = mwtEventBusName + "_" + uiNameCard.getUiScreen();
			}
			logger.debug(f, "init mwtEventBusName[{}]", mwtEventBusName);
			
			eventBus = MwtEventBuses.getInstance().getEventBus(mwtEventBusName);

			String tmpStrOlsCssPrefix = dictionariesCache.getStringValue(optsXMLFile, ParameterName.OlsCssPrefix.toString(), strHeader);
			if ( null != tmpStrOlsCssPrefix ) strOlsCssPrefix = tmpStrOlsCssPrefix;
			
			String defaultDateTimeValue = dictionariesCache.getStringValue(optsXMLFile, ParameterName.DefaultDateTimeValue.toString(), strHeader);
			if ( null != defaultDateTimeValue ) strDefaultDateTimeValue = defaultDateTimeValue;
			
			String dateTimeCssSubfix = dictionariesCache.getStringValue(optsXMLFile, ParameterName.DateTimeCssSubfix.toString(), strHeader);
			if ( null != dateTimeCssSubfix ) strDateTimeCssSubfix = dateTimeCssSubfix;
			
			String pagerName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.PagerName.toString(), strHeader);
			if ( null != pagerName && ParameterValue.SCADAgenPager.toString().equals(pagerName) ) {
				this.hasSCADAgenPager = true;
			}
			
            for ( String strGDGAttribute : GDGAttribute.toStrings() ) {
            	
            	String gdgAttribute	= dictionariesCache.getStringValue(optsXMLFile, strGDGAttribute, strHeader);
            	
            	logger.debug(f, "init gdgAttribute[{}]", gdgAttribute);
            	
            	if ( null != gdgAttribute ) {
            		
            		logger.debug(f, "init gdgAttribute[{}] Added", gdgAttribute);
            		
            		attributes.add(gdgAttribute);
            	}
            	
            }
			
			setParameter(ParameterName.ListConfigId.toString(), listConfigId_);
			
			setParameter(ParameterName.MenuCallImageEnable.toString(), menuCallImageEnable);
			setParameter(ParameterName.MenuCallImageLabel.toString(), menuCallImageLabel);
			setParameter(ParameterName.MenuCallImageGdgAttribute.toString(), menuCallImageGdgAttribute);
			
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
    	String f = "terminate";
        isTerminated_ = true;
        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        try {
            gridPresenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
        	logger.error(f, "Error while trying to terminate the Alarm List Panel.", e);
        }
    }

}
