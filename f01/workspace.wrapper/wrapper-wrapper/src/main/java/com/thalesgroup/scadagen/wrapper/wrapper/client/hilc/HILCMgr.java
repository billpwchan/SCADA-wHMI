package com.thalesgroup.scadagen.wrapper.wrapper.client.hilc;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc.FasHILCComponentAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc.IHILCComponentClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class HILCMgr {

	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());

	private static Map<String, HILCMgr> instances = new HashMap<String, HILCMgr>();
	public static HILCMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new HILCMgr()); }
		HILCMgr instance = instances.get(key);
		return instance;
	}

	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private FasHILCComponentAccess hilcAccess = null;
	
	private HILCMgr_i hilgMgr_i = null;
	
	private HILCMgr () {
		final String function = "HILCMgr";
		
		logger.begin(className, function);
				
		hilcAccess = new FasHILCComponentAccess(new IHILCComponentClient() {

			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				
			}

			@Override
			public Widget asWidget() {
				return null;
			}

			@Override
			public void destroy() {

			}

			@Override
			public void setHILCPreparationResult(String clientKey, int errorCode, String errorMessage) {
				final String function = "setHILCPreparationResult";
				logger.begin(className, function);
				
				HILCPreparationResult(clientKey, errorCode, errorMessage);
				
				logger.end(className, function);
			}

			@Override
			public void setHILCConfirmResult(String clientKey, int errorCode, String errorMessage) {
				final String function = "setHILCConfirmResult";
				logger.begin(className, function);
				
				HILCConfirmResult(clientKey, errorCode, errorMessage);
				
				logger.end(className, function);
			}			
		});
	}
	
	public void connect() {
		final String function = "connect";
		logger.beginEnd(className, function);
	}
	
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		try {
			hilcAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			e.printStackTrace();
		}
		hilcAccess=null;
		logger.end(className, function);
	}
	
	public void setHILCMgr_i(HILCMgr_i hilcMgr_i) {
		this.hilgMgr_i = hilcMgr_i;
	}
	
	public void HILCPreparationResult(String clientKey, int errorCode, String errorMessage) {
		if (hilgMgr_i != null) {
			hilgMgr_i.HILCPreparationResult(clientKey, errorCode, errorMessage);
		}
	}
	
	public void HILCConfirmResult(String clientKey, int errorCode, String errorMessage) {
		if (hilgMgr_i != null) {
			hilgMgr_i.HILCConfirmResult(clientKey, errorCode, errorMessage);
		}
	}
	
	
	public void hilcPreparationRequest(String key, String scsEnvId, String operatorName, String workstationName, int cmdType,
    		int cmdValue, int cmdValueDiv, String eqpAlias, String eqpType, String cmdName) {
		final String function = "hilcPreparationRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] operatorName[{}] workstationName[{}] cmdType[{}], cmdValue[{}], cmdValueDiv[{}], eqpAlias[{}], eqpType[{}], cmdName[{}]", new Object[]{key, scsEnvId,operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName});

		hilcAccess.hilcPreparationRequest(key, scsEnvId, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
		
		logger.end(className, function);
	}
	
	public void hilcConfirmRequest(String key, String scsEnvId, String operatorName, String workstationName, int cmdType,
    		int cmdValue, int cmdValueDiv, String eqpAlias, String eqpType, String cmdName) {
		final String function = "hilcPreparationRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] operatorName[{}] workstationName[{}] cmdType[{}], cmdValue[{}], cmdValueDiv[{}], eqpAlias[{}], eqpType[{}], cmdName[{}]", new Object[]{key, scsEnvId,operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName});

		hilcAccess.hilcConfirmRequest(key, scsEnvId, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
		
		logger.end(className, function);
	}
}
