package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.CaptionPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.selection.SingleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event.EqptFilterChangeEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.EquipmentQueryFormPresenterClient;

public class EquipmentListPanel extends ResizeComposite implements
		IClientLifeCycle {

	/**
     * The GenericDataGrid widget configuration id to use.
     */
    private static final String WIDGET_CONFIG_ID = "equipmentList";

    /** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent.
     */
    private final CaptionPanel mainPanel_;

    /**
     * Client presenter of this Equipment list widget.
     */
    private EquipmentQueryFormPresenterClient gridPresenter_;
    
    private GenericDataGridView gridView_;

    /**
     * Bus used to subscribe to and publish Equipment-related events.
     */
    private final EventBus eventBus_;
    
    /**
     * Used to remove event handler.
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    /**
     * Builds the default Equipment list panel which uses a given event bus.
     *
     * @param eventBus bus used to subscribe to and publish Equipment-related events.
     */
    public EquipmentListPanel(final EventBus eventBus) {
        this(WIDGET_CONFIG_ID, eventBus);
    }

    /**
     * Builds an Connection list panel with an event bus and a context menu.
     *
     * @param configurationId the id of the grid configuration.
     * @param eventBus bus used to subscribe to and publish Connection-related events.
     */
    public EquipmentListPanel(final String configurationId,
                               final EventBus eventBus) {
        eventBus_ = eventBus;
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();
        mainPanel_ = new CaptionPanel();

        //final String captionLabel = Dictionary.getWording("equipmentListPanel_caption");
        //mainPanel_.setCaption(captionLabel);
        
//        EquipmentQueryContext eqptContext = new EquipmentQueryContext(configurationId);
        
//        EqptQuery eqptQuery = new EqptQuery();
//        eqptContext.setEqptQuery(eqptQuery);

        gridView_ = new GenericDataGridView();
        gridPresenter_ = new EquipmentQueryFormPresenterClient(configurationId, gridView_, eventBus_);

        mainPanel_.add(gridPresenter_.getView().asWidget());

        gridPresenter_.setSelectionModel(new SingleSelectionModel());

        if (eventBus_ != null) {
            handlerRegistrations_.add(eventBus_.addHandler(EqptFilterChangeEvent.TYPE, gridPresenter_));
        }

        initWidget(mainPanel_);
    }
   
	@Override
	public void terminate() {
		if (mainPanel_ != null) {
            mainPanel_.clear();
        }
        
        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        try {
            gridPresenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
            logger_.error("Error while trying to terminate the Equipment List Panel.", e);
        }
	}
}
