package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.IDPCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.ScsDPCComponentAccess;

public class DpcMgr {
	
	private static Logger logger = Logger.getLogger(DpcMgr.class.getName());

	private static HashMap<String, DpcMgr> instances = new HashMap<String, DpcMgr>();
	public static DpcMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new DpcMgr()); }
		DpcMgr instance = instances.get(key);
		return instance;
	}
	
//	private static DpcMgr instance = null;
//	public static DpcMgr getInstance() {
//		if ( null == instance ) instance = new DpcMgr(); 
//		return instance;
//	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsDPCComponentAccess dpcAccess = null;
	private DpcMgr () {
		logger.log(Level.SEVERE, "DpcMgr Begin");
		
		this.subject = new Subject();
		
		dpcAccess = new WrapperScsDPCComponentAccess(new IDPCComponentClient() {
			
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
				logger.log(Level.SEVERE, "setIsVarStatusValidResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvOperatorResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvOperatorResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvOperandResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvOperandResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvEqpResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvEqpResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvConcResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvConcResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvComResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvComResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvChatteringResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvChatteringResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusInvApplicationResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusInvApplicationResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusForcedResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusForcedResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusAlaInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusAlaInhResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsVarStatusAlaInhEqpResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsVarStatusAlaInhEqpResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpTagStatusTag2Result(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpTagStatusTag2Result clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpTagStatusTag1Result(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpTagStatusTag1Result clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpTagStatusNormalResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpTagStatusNormalResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpStatusNormalResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpStatusNormalResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpStatusMonInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpStatusMonInhResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpStatusCtrlInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpStatusCtrlInhResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setIsEqpStatusAlarmInhResult(String clientKey, boolean bool, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setIsEqpStatusAlarmInhResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeVarStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeVarStatusResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeStringVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeStringVarForceResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeIntVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeIntVarForceResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeFloatVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeFloatVarForceResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeEqpTagResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeEqpTagResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
			
			@Override
			public void setChangeEqpStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				logger.log(Level.SEVERE, "setChangeEqpStatusResult clientKey["+clientKey+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
		});
		
		logger.log(Level.SEVERE, "DpcMgr End");
	}
	
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		logger.log(Level.SEVERE, "connect End");
	}
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		try {
			dpcAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dpcAccess=null;
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	public void sendChangeVarStatus(String key, String scsEnvId, String alias, DCP_i.ValidityStatus status) {
		
		dpcAccess.changeVarStatus(key, scsEnvId, alias, status.getValue());
		
	}
//	
//	public void sendChangeVarStatus(String key, String scsEnvId, String alias, DCP_i.WorkingStatus status) {
//		
//		dpcAccess.changeVarStatus(key, scsEnvId, alias, status.getValue());
//		
//	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, int value) {
		if ( enable ) {
			dpcAccess.changeIntVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeIntVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, float value) {
		if ( enable ) {
			dpcAccess.changeFloatVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeFloatVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String alias, boolean enable, String value) {
		if ( enable ) {
			dpcAccess.changeStringVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeStringVarForce(key, scsEnvId, alias, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		
	}
}
