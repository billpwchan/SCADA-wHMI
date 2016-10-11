package com.thalesgroup.scadagen.wrapper.wrapper.client.dpc;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.IDPCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.ScsDPCComponentAccess;

/**
 * @author syau
 *
 */
public class DpcMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DpcMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, DpcMgr> instances = new HashMap<String, DpcMgr>();
	public static DpcMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new DpcMgr()); }
		DpcMgr instance = instances.get(key);
		return instance;
	}

	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsDPCComponentAccess dpcAccess = null;
	private DpcMgr () {
		final String function = "DpcMgr";
		
		logger.begin(className, function);
		
		this.subject = new Subject();
		
		dpcAccess = new ScsDPCComponentAccess(new IDPCComponentClient() {
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setIsVarStatusValidResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusValidResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvOperatorResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvOperatorResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvOperandResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvOperandResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvEqpResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvEqpResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvConcResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvConcResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvComResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvComResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvChatteringResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvChatteringResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusInvApplicationResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusInvApplicationResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusForcedResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusForcedResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusAlaInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusAlaInhResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsVarStatusAlaInhEqpResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsVarStatusAlaInhEqpResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpTagStatusTag2Result(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpTagStatusTag2Result";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpTagStatusTag1Result(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpTagStatusTag1Result";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpTagStatusNormalResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpTagStatusNormalResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpStatusNormalResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpStatusNormalResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpStatusMonInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpStatusMonInhResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpStatusCtrlInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpStatusCtrlInhResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setIsEqpStatusAlarmInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setIsEqpStatusAlarmInhResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeVarStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeVarStatusResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeStringVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeStringVarForceResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeIntVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeIntVarForceResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeFloatVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeFloatVarForceResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeEqpTagResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeEqpTagResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeEqpStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeEqpStatusResult";
				logger.info(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
		});
		
		logger.end(className, function);
	}
	
	public void connect() {
		final String function = "connect";
		logger.beginEnd(className, function);
	}
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		try {
			dpcAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dpcAccess=null;
		logger.end(className, function);
	}
	
	public void sendChangeVarStatus(String key, String scsEnvId, String alias, DCP_i.ValidityStatus status) {
		final String function = "sendChangeVarStatus";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] alias[{}] status[{}]", new Object[]{key, scsEnvId, alias, status.toString()});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		sendChangeVarStatus(key, scsEnvId, alias, status.getValue());
		logger.end(className, function);
	}
	
	public void sendChangeVarStatus(String key, String scsEnvId, String alias, int status) {
		final String function = "sendChangeVarStatus";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] alias[{}] status[{}]", new Object[]{key, scsEnvId, alias, status});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		dpcAccess.changeVarStatus(key, scsEnvId, alias, status);
		logger.end(className, function);
	}

	public void sendChangeEqpTag(String key, String scsEnvId, String alias, TaggingStatus status, String taggingLabel1, String taggingLabel2) {
		final String function = "sendChangeEqpTag";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] name[{}] status[{}] taggingLabel1[{}] taggingLabel2[{}]", new Object[]{key, scsEnvId, alias, status, taggingLabel1, taggingLabel2});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		dpcAccess.changeEqpTag(key, scsEnvId, alias, status.getValue(), taggingLabel1, taggingLabel2);
		logger.end(className, function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, int value) {
		final String function = "sendChangeVarForce";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] alias[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, alias, enable, value});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		if ( enable ) {
			dpcAccess.changeIntVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeIntVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(className, function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, float value) {
		final String function = "sendChangeVarForce";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] alias[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, alias, enable, value});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		if ( enable ) {
			dpcAccess.changeFloatVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeFloatVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(className, function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, String value) {
		final String function = "sendChangeVarForce";
		logger.begin(className, function);
		logger.info(className, function, "key[{}], scsEnvId[{}] alias[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, alias, enable, value});
		if ( !alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.info(className, function, "alias[{}]", alias);
		if ( enable ) {
			dpcAccess.changeStringVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeStringVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(className, function);
	}
}
