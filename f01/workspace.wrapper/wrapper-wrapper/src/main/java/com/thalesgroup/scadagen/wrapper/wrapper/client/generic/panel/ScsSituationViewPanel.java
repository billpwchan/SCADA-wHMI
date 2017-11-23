package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.handler.IPresenterStateHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.tools.BaseClassTools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.ISituationView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.SituationView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.selection.ISelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.selection.MultipleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.selection.SingleSelectionModel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.widget.ScsWidgetFactory;


/**
 * A widget implementing a situation view widget.
 */
public class ScsSituationViewPanel extends ResizeComposite implements IClientLifeCycle {

    /**
     * The configuration id
     */
    private final String configId_;

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private final SimpleLayoutPanel mainPanel_;

    /**
     * Presenter managing the view
     */
    private final SituationViewPresenterClient presenter_;

    /**
     * Bus used to publish and listen to equipment events
     */
    private final EventBus eventBus_;

    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    /** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();

    private final ISelectionModel selectionModelEqpt_;

    private final ISelectionModel selectionModelAlarm_;

    /**
     * Builds a {@link ScsSituationViewPanel}.
     *
     * @param configurationId
     *            configuration id
     * @param eventBus
     *            a bus used to subscribe to and publish events
     */
    public ScsSituationViewPanel(final String configurationId, final EventBus eventBus, final SCADAgenSituationViewPanelEvent wrapperScsSituationViewPanelEvent) {
        this(configurationId, eventBus, wrapperScsSituationViewPanelEvent, null);
    }

    /**
     * Builds a {@link ScsSituationViewPanel}.
     *
     * @param configurationId
     *            configuration id
     * @param eventBus
     *            a bus used to subscribe to and publish events
     * @param presenterStateHandler
     *            Presenter State handler
     */
    public ScsSituationViewPanel(final String configurationId, final EventBus eventBus,
    		final SCADAgenSituationViewPanelEvent wrapperScsSituationViewPanelEvent,
            final IPresenterStateHandler presenterStateHandler) {
        configId_ = configurationId;
        eventBus_ = eventBus;
        this.wrapperScsSituationViewPanelEvent = wrapperScsSituationViewPanelEvent;
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();

        mainPanel_ = new SimpleLayoutPanel();
        initWidget(mainPanel_);

        // CUSTOM code BEGIN
        mainPanel_.addStyleName(configId_);
        // A view with a custom widgetfactory
        ScsWidgetFactory widgetFactory = new ScsWidgetFactory(this.wrapperScsSituationViewPanelEvent);
        final SituationView svView = new SituationView(widgetFactory);

        // A presenter
        presenter_ = new SituationViewPresenterClient(configId_, svView, eventBus_, presenterStateHandler);
        // CUSTOM code END
        mainPanel_.add(presenter_.getView().asWidget());

        selectionModelEqpt_ = new MultipleSelectionModel(BaseClassTools.EQUIPMENT_CLASS_NAME);
        presenter_.setEquipmentSelectionModel(selectionModelEqpt_);
        selectionModelAlarm_ = new SingleSelectionModel(BaseClassTools.ALARM_CLASS_NAME);
        presenter_.setAlarmSelectionModel(selectionModelAlarm_);

        // Register in order to react to equipment selection events
        if (eventBus_ != null) {
            handlerRegistrations_.add(eventBus_.addHandler(EqptSelectionChangeEvent.TYPE, presenter_));
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, presenter_));
        }
    }

    /**
     * Get {@link ISituationView} View component
     * 
     * @return {@link ISituationView} View component
     */
    public ISituationView getView() {
        return presenter_.getView();
    }

    /**
     * Get {@link SituationViewPresenterClient} Presenter component
     * 
     * @return {@link SituationViewPresenterClient} Presenter component
     */
    public SituationViewPresenterClient getPresenter() {
        return presenter_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        if (mainPanel_ != null) {
            mainPanel_.clear();
        }

        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        if (presenter_ != null) {
            try {
                presenter_.terminate();
            } catch (final IllegalStatePresenterException e) {
                logger_.error("An error occurred when the situation view presenter [" + presenter_.getConfigurationId()
                        + "] try to terminate.", e);
            }

            presenter_.destroy();
        }
        if (selectionModelEqpt_ != null) {
            selectionModelEqpt_.destroy();
        }
        if (selectionModelAlarm_ != null) {
            selectionModelAlarm_.destroy();
        }
    }
    
	private SCADAgenSituationViewPanelEvent wrapperScsSituationViewPanelEvent = null;

}
