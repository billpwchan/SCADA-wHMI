package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.CaptionPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixConfigType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.AlarmMatrixPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.MatrixContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.IMatrixSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.GenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MatrixCss;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;

/**
 * A widget displaying an alarm matrix panel.
 */
public class CoccMatrixPanel extends ResizeComposite implements IClientLifeCycle {


    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private HasWidgets m_mainPanel;

    /**s
     * Client presenter of this alarm matrix widget
     */
    private AlarmMatrixPresenterClient m_matrixPresenter;

    /**
     * Bus used to subscribe to and publish alarm-related events
     */
    private EventBus m_eventBus;

    /**
     * Used to remove event handler
     */
    private List<HandlerRegistration> m_handlerRegistrations;

    /**
     *  Include or not the banner in a CaptionPanel
     */
    private final boolean m_withCaption;

    /**
     * The configuration id of the matrix
     */
    private final String m_matrixConfigId;

    /**
     * The view that represents the alarm matrix
     */
    private GenericMatrixView m_matrixView;

    private boolean m_isTerminated = false;
    
    private final String m_axisRowId;
    
    private final String m_axisColId;
    
    private final MatrixConfigType m_mxType;


    /**
     * To get the label of the list for the panel caption
     */
    private static final String DICTIONATY_CAPTION_SUFFIX = "_caption";

    /**
     * Builds an alarm list panel which uses a given event bus.
     *
     * @param eventBus bus used to subscribe to and publish alarm-related events
     * @param matrixConfigId the id of the datagrid configuration
     * @param withCaption display or not a caption with the name of the alarm list
     */
    public CoccMatrixPanel(final EventBus eventBus, String matrixConfigId, boolean withCaption) {

        m_eventBus = eventBus;
        m_withCaption = withCaption;
        m_matrixConfigId = matrixConfigId;
        m_axisRowId = null;
        m_axisColId = null;
        m_mxType = MatrixConfigType.STATIC;

        initComponents();
        initPresenter();
        initWidget((Widget)m_mainPanel);
    }
    
    public CoccMatrixPanel(final EventBus eventBus, String matrixConfigId, String axisRowId, String axisColId, MatrixConfigType mxType, boolean withCaption) {

        m_eventBus = eventBus;
        m_withCaption = withCaption;
        m_matrixConfigId = matrixConfigId;
        m_axisRowId = axisRowId;
        m_axisColId = axisColId;
        m_mxType = mxType;

        initComponents();
        initPresenter();
        initWidget((Widget)m_mainPanel);
    }

    public boolean isTerminated() {
        return m_isTerminated ;
    }

    /**
     * Create and initialize the presenter with its view, selectionModel, context menu and Handlers
     */
    public void initPresenter() {
        if (m_matrixConfigId != null && m_matrixView != null && m_eventBus != null) {
            // A context
            final MatrixContext context = new MatrixContext();
            context.setConfId(m_matrixConfigId);
            context.setAxisRowId(m_axisRowId);
            context.setAxisColumnId(m_axisColId);
            context.setMxConfigType(m_mxType);
            
            // presenter
            m_matrixPresenter = new AlarmMatrixPresenterClient(context, m_matrixView, m_eventBus);

            // Selection manager
            final IMatrixSelectionManager selectionManager = new CoccMatrixSingleSelectionManager();
            selectionManager.setPresenter(m_matrixPresenter);
            selectionManager.setView(m_matrixView);

            m_matrixPresenter.setSelectionManager(selectionManager);

            initHandler();
        }else{
            s_logger.error(CoccMatrixPanel.class.getName()+" initPresenter : listConfigId_,gridView_,eventBus_,contextMenu_ should not be null");
        }
    }

    public void initHandler(){
        if (m_eventBus != null) {
            m_handlerRegistrations = new ArrayList<HandlerRegistration>();
            m_handlerRegistrations.add(m_eventBus.addHandler(AlarmSelectionChangeEvent.TYPE, m_matrixPresenter));
        } 
    }

    /**
     * Initialize and create all needed components
     */
    private void initComponents(){
        // A view
        final IMatrixRenderer renderer = new CoccMatrixRenderer();
        m_matrixView = new GenericMatrixView(renderer);
        m_matrixView.addStyleName(MatrixCss.CSS_GM_PANEL_ALARM);

        initMainPanel();
    }

    /**
     * Create the main container with a Caption or not
     */
    private void initMainPanel(){
        if (m_withCaption) {
            m_mainPanel = new CaptionPanel();
            final String captionLabel = Dictionary.getWording(m_matrixConfigId+DICTIONATY_CAPTION_SUFFIX);
            ((CaptionPanel)m_mainPanel).setCaption(captionLabel);

        } else {
            m_mainPanel = new DockLayoutPanel(Unit.PX);
        }

        //Create matrix container
        DockLayoutPanel containerPanel  = new DockLayoutPanel(Unit.PX);
       
        //Add containers in the main panel
        containerPanel.add(m_matrixView);
        m_mainPanel.add(containerPanel);
    }

    @Override
    public void onResize() {
        super.onResize();
        m_matrixView.onResize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        m_isTerminated = true;
        for (final HandlerRegistration registration : m_handlerRegistrations) {
            registration.removeHandler();
        }
        m_handlerRegistrations.clear();

        try {
            m_matrixPresenter.terminate();
        } catch (final IllegalStatePresenterException e) {
            s_logger.error("Error while trying to terminate the Alarm Matrix Panel.", e);
        }
    }
}
