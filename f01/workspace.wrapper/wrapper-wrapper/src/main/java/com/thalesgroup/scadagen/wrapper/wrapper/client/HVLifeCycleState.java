package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateActivatedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateInitialClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateInitializedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStatePassivatedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateStartedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateStoppedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HLCStateTerminatedClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.state.HypLifeCycleStateClientAbstract;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.presenter.AScsComponentPresenterClient;

public class HVLifeCycleState {

	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(HVLifeCycleState.class.getName());
	
	public static String getCurrentState(String prefix1, String prefix2, AScsComponentPresenterClient<?> aScsComponentPresenterClient) {
		String function = "getCurrentState";
		String state = null;
		if ( null != aScsComponentPresenterClient ) {
			state = getCurrentState(prefix1, prefix2, aScsComponentPresenterClient.getCurrentState());
		} else {
			logger.debug(function, "aScsComponentPresenterClient IS NULL");
		}
		logger.debug(function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
		return state;
	}
	
	public static String getCurrentState(String prefix1, String prefix2, HypLifeCycleStateClientAbstract hypLifeCycleStateClient) {
		String function = "getCurrentState";
		String state = null;
		if ( null != hypLifeCycleStateClient ) {
			if ( hypLifeCycleStateClient instanceof HLCStateActivatedClient ) {
				state = HLCStateActivatedClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateInitialClient ) {
				state = HLCStateInitialClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateInitializedClient ) {
				state = HLCStateInitializedClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStatePassivatedClient ) {
				state = HLCStatePassivatedClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateStartedClient ) {
				state = HLCStateStartedClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateStoppedClient ) {
				state = HLCStateStoppedClient.class.getSimpleName();	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateTerminatedClient ) {
				state = HLCStateTerminatedClient.class.getSimpleName();	
			}
		} else {
			logger.debug(function, "hypLifeCycleStateClient IS NULL");
		}

		logger.debug(function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
		return state;
	}
	
	public static void ensureIsActivated(String prefix1, String prefix2, AScsComponentPresenterClient<?> aScsComponentPresenterClient) {
		String function = "ensureIsActivated";
		String state = null;
		if ( getCurrentState(prefix1, prefix2, aScsComponentPresenterClient) != HLCStateActivatedClient.class.getSimpleName() ) {
			try {
				aScsComponentPresenterClient.activate();
			} catch (IllegalStatePresenterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.debug(function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
	}
}
