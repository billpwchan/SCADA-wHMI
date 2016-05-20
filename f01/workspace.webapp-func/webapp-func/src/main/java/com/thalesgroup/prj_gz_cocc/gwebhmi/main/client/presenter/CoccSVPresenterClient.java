package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.handler.IPresenterStateHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.ISituationView;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.life.CoccSVInitStateTransitionReturn;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.view.CoccSituationView;

public class CoccSVPresenterClient extends SituationViewPresenterClient {
	//private static final ClientLogger logger_ = ClientLogger.getClientLogger();

	/**
	 * Constructs a situation view presenter.
	 * @param configurationId the configuration id of the view.
	 * @param situationView the view.
	 */
	public CoccSVPresenterClient(final String configurationId,
			final ISituationView situationView) {
		super(configurationId, situationView);
	}

	/**
	 * Constructs a situation view presenter.
	 * @param configurationId the configuration id of the view.
	 * @param situationView the view.
	 * @param eventBus the event bus.
	 */
	public CoccSVPresenterClient(final String configurationId,
			final ISituationView situationView, final EventBus eventBus) {
		super(configurationId, situationView, eventBus);
	}

	/**
	 * Constructs a situation view presenter.
	 * @param configurationId the configuration id of the view.
	 * @param situationView the view.
	 * @param eventBus the event bus.
	 * @param presenterStateHandler Presenter State handler
	 */
	public CoccSVPresenterClient(final String configurationId,
			final ISituationView situationView,
			final EventBus eventBus,
			final IPresenterStateHandler presenterStateHandler) {
		super(configurationId, situationView, eventBus, presenterStateHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInitialize(final StateTransitionReturn transitionResult) {
		super.onInitialize(transitionResult);

		// Retrieve Customized Data from Transition Return
		if(transitionResult instanceof CoccSVInitStateTransitionReturn) {
			// Retrieve Buttons and Camera IDs and Pass them to Situation View
			((CoccSituationView)getView()).appendButtons(((CoccSVInitStateTransitionReturn) transitionResult).getButtons());
			((CoccSituationView)getView()).addCameraIDs(((CoccSVInitStateTransitionReturn) transitionResult).getCameraIDs());
		}
	}

	@Override
	public void onStart(final StateTransitionReturn transitionResult) {
		super.onStart(transitionResult);

		// In-line style has to be set now, not before, or the value will be overwritten.
		((CoccSituationView)getView()).initButtons();
	}
}
