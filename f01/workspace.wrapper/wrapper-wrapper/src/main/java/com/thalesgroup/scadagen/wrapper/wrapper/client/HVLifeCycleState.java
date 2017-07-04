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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.presenter.AScsComponentPresenterClient;

public class HVLifeCycleState {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(HVLifeCycleState.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static String getCurrentState(String prefix1, String prefix2, AScsComponentPresenterClient<?> aScsComponentPresenterClient) {
		String function = "getCurrentState";
		String state = null;
		if ( null != aScsComponentPresenterClient ) {
			state = getCurrentState(prefix1, prefix2, aScsComponentPresenterClient.getCurrentState());
		} else {
			logger.debug(className, function, "aScsComponentPresenterClient IS NULL");
		}
		logger.debug(className, function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
		return state;
	}
	
	public static String getCurrentState(String prefix1, String prefix2, HypLifeCycleStateClientAbstract hypLifeCycleStateClient) {
		String function = "getCurrentState";
		String state = null;
		if ( null != hypLifeCycleStateClient ) {
			if ( hypLifeCycleStateClient instanceof HLCStateActivatedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateActivatedClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateInitialClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateInitialClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateInitializedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateInitializedClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStatePassivatedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStatePassivatedClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateStartedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateStartedClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateStoppedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateStoppedClient.class.getName());	
			}
			else
			if ( hypLifeCycleStateClient instanceof HLCStateTerminatedClient ) {
				state = UIWidgetUtil.getClassSimpleName(HLCStateTerminatedClient.class.getName());	
			}
		} else {
			logger.debug(className, function, "hypLifeCycleStateClient IS NULL");
		}

		logger.debug(className, function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
		return state;
	}
	
	public static void ensureIsActivated(String prefix1, String prefix2, AScsComponentPresenterClient<?> aScsComponentPresenterClient) {
		String function = "ensureIsActivated";
		String state = null;
		if ( getCurrentState(prefix1, prefix2, aScsComponentPresenterClient) != UIWidgetUtil.getClassSimpleName(HLCStateActivatedClient.class.getName()) ) {
			try {
				aScsComponentPresenterClient.activate();
			} catch (IllegalStatePresenterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.debug(className, function, "prefix1[{}] prefix2[{}] state[{}]", new Object[]{prefix1, prefix2, state});
	}
}