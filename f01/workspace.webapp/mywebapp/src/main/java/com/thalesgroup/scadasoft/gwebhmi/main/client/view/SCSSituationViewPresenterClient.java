package com.thalesgroup.scadasoft.gwebhmi.main.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.handler.IPresenterStateHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.ISituationView;

/**
 * Client presenter of the situation view.
 *
 */

public class SCSSituationViewPresenterClient extends SituationViewPresenterClient {

    /**
     * Constructs a situation view presenter.
     * 
     * @param configurationId
     *            the configuration id of the view.
     * @param situationView
     *            the view.
     * @param eventBus
     *            the event bus.
     * @param presenterStateHandler
     *            Presenter State handler
     */
    public SCSSituationViewPresenterClient(final String configurationId, final ISituationView situationView,
            final EventBus eventBus, final IPresenterStateHandler presenterStateHandler) {
        super(configurationId, situationView, eventBus, presenterStateHandler);
    }

    private final List<HandlerRegistration> m_handlersRegistration = new ArrayList<HandlerRegistration>();

    private ClickHandler m_linkClickHandler = null;

    /***
     * {@inheritDoc}
     */
    @Override
    public void destroy() {

        for (final HandlerRegistration hr : m_handlersRegistration) {
            hr.removeHandler();
        }

        m_handlersRegistration.clear();

        if (m_linkClickHandler != null) {
            m_linkClickHandler = null;
        }

        super.destroy();
    }
}
