package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.CaptionPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixConfigType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.MatrixContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.IMatrixSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.MatrixNoSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.MatrixSingleSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.GenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MatrixCss;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.ScsMatrixPanel_i.ParameterName;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer.ScsMatrixRenderer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.MwtEventBuses;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsMatrixPresenterClient;

public class ScsMatrixPanel extends UIWidget_i {

	private static final String className = UIWidgetUtil.getClassSimpleName(ScsMatrixPanel.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

    /**
     * Client presenter of this matrix widget
     */
    protected ScsMatrixPresenterClient matrixPresenter_ = null;

    /**
     * The view that represents the alarm matrix
     */
    protected GenericMatrixView matrixView_ = null;
    
    protected IMatrixSelectionManager selectionManager_ = null;
    
    /**
     * Bus used to subscribe to and publish matrix events
     */
    protected EventBus eventBus_ = null;
    
    public EventBus getEventBus() {
    	return eventBus_;
    }

    /**
     * Used to remove event handler
     */
    protected List<HandlerRegistration> m_handlerRegistrations;

    // flag to know state
    protected boolean isTerminated_ = false;

    /**
     * To get the label of the list for the panel caption
     */
    protected static final String DICTIONATY_CAPTION_SUFFIX = "_caption";

    /**
     * The configuration id of the matrix
     */
    protected String matrixConfigId_ = null;
    
    boolean withCaption_ = false;
    
    protected String selectionMode_ = null;
    
    protected String matrixViewStyle_ = MatrixCss.CSS_GM;
    
    protected Boolean displayCounterTooltip_ = false;
    protected String counterCssPrefix_ = "scsmx";
    protected String coeffAttributeName_ = null;

    @Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			matrixConfigId_			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MatrixConfigId.toString(), strHeader);
			selectionMode_			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.SelectionMode.toString(), strHeader);
			if (selectionMode_.compareToIgnoreCase("Single")==0) {
				selectionManager_ = new MatrixSingleSelectionManager();
			} else if (selectionMode_.compareToIgnoreCase("Multiple")==0) {
				selectionManager_ = new ScsMatrixMultipleSelectionManager();
			} else {
				selectionManager_ = new MatrixNoSelectionManager();
			}
			String style			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MatrixViewStyle.toString(), strHeader);
			if (style != null && !style.isEmpty()){
				matrixViewStyle_ = style;
			}
			coeffAttributeName_		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CoeffAttributeName.toString(), strHeader);
			String displayCounterStr	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DisplayCounterTooltip.toString(), strHeader);
			if (displayCounterStr != null && displayCounterStr.compareToIgnoreCase("True") == 0) {
				displayCounterTooltip_ = true;
			}
			String cssPrefix		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CounterCssPrefix.toString(), strHeader);
			if (cssPrefix != null) {
				counterCssPrefix_ = cssPrefix;
			}

			String mwtEventBusName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusName.toString(), strHeader);
			String mwtEventBusScope	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MwtEventBusScope.toString(), strHeader);
			
			logger.debug(className, function, "init mwtEventBusName["+mwtEventBusName+"]");
			logger.debug(className, function, "init mwtEventBusScope["+mwtEventBusScope+"]");
			
			if ( null == mwtEventBusName || mwtEventBusName.trim().length() == 0) {
				mwtEventBusName = this.viewXMLFile;
			}
//			if ( mwtEventBusScope != null && ParameterValue.Global.toString().equals(mwtEventBusScope) ) {
//			} else {
//				mwtEventBusName = mwtEventBusName + "_" + uiNameCard.getUiScreen();
//			}
//			LOGGER.debug(LOG_PREFIX + "init mwtEventBusName["+mwtEventBusName+"]");
			
			eventBus_ = MwtEventBuses.getInstance().getEventBus(mwtEventBusName);
			
			setParameter(ParameterName.MatrixConfigId.toString(), matrixConfigId_);
			setParameter(ParameterName.SelectionMode.toString(), selectionMode_);
			setParameter(ParameterName.EventBus.toString(), eventBus_);
		}

        initPresenter();
//        initHandler();
        initMainPanel();
        
        logger.end(className, function);
	}

    /**
     * Create and initialize the presenter with its view, selectionModel,
     * context menu and Handlers
     */
    private void initPresenter() {
    	final String function = "initPresenter";
		
		logger.begin(className, function);
		
        if (matrixConfigId_ != null && eventBus_ != null) {
            // A context
            final MatrixContext context = new MatrixContext(matrixConfigId_, MatrixConfigType.STATIC);
            
            // A view
            final IMatrixRenderer renderer = new ScsMatrixRenderer(counterCssPrefix_, coeffAttributeName_, displayCounterTooltip_);
            matrixView_ = new GenericMatrixView(renderer);
            //matrixView_ = new ScsMatrixView(null);
            matrixView_.addStyleName(matrixViewStyle_);

            // presenter
            matrixPresenter_ = new ScsMatrixPresenterClient(context, matrixView_, eventBus_);

            // Selection manager
            
            
            selectionManager_.setPresenter(matrixPresenter_);
            selectionManager_.setView(matrixView_);

            matrixPresenter_.setSelectionManager(selectionManager_);
        } else {
        	logger.error(className, function,
                    " matrixConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
        logger.end(className, function);
    }

//    private void initHandler() {
//        if (eventBus_ != null && matrixPresenter_ != null) {
//            m_handlerRegistrations = new ArrayList<HandlerRegistration>();
//            m_handlerRegistrations.add(eventBus.addHandler(AlarmSelectionChangeEvent.TYPE, m_matrixPresenter));
//        }
//    }

    /**
     * Create the main container with a Caption or not
     * 
     * @param withCaption
     * @param matrixConfigId
     */
    private Widget initMainPanel() {
    	final String function = "initMainPanel";
		
		logger.begin(className, function);

        if (withCaption_) {
            rootPanel = new CaptionPanel();
            final String captionLabel = Dictionary.getWording(matrixConfigId_ + DICTIONATY_CAPTION_SUFFIX);
            ((CaptionPanel) rootPanel).setCaption(captionLabel);

        } else {
            rootPanel = new DockLayoutPanel(Unit.PX);
        }

        // Create matrix container
        DockLayoutPanel containerPanel = new DockLayoutPanel(Unit.PX);

        // Add containers in the main panel
        if (matrixView_ != null) {
            containerPanel.add(matrixView_);
        } else {
        	logger.error(className, function, " initMainPanel : matrix view does not exists");
        }
        rootPanel.add(containerPanel);
        
        rootPanel.addStyleName("project-gwt-panel-scsmatrixpanel");
        
        String cssWithElement = "project-"+className+"-"+element;
        logger.debug(className, function, "getStyleNames className["+className+"] element["+element+"] cssWithElement["+cssWithElement+"]");
        rootPanel.addStyleName(cssWithElement);
        
        logger.end(className, function);

        return rootPanel;
    }
    
    public ScsMatrixPresenterClient getPresenter() {
    	return matrixPresenter_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
    	final String function = "terminate";
		
		logger.begin(className, function);
		
        isTerminated_ = true;
//        for (final HandlerRegistration registration : m_handlerRegistrations) {
//            registration.removeHandler();
//        }
//        m_handlerRegistrations.clear();

        try {
            matrixPresenter_.terminate();
            
            matrixPresenter_ = null;
            
            matrixView_ = null;
            
        } catch (final IllegalStatePresenterException e) {
        	logger.error(className, function, "Error while trying to terminate the Alarm Matrix Panel.", e);
        }
        
        logger.end(className, function);
    }

    public boolean isTerminated() {
        return isTerminated_;
    }


	
}
