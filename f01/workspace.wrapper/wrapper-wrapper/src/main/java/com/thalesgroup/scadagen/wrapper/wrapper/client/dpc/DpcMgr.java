package com.thalesgroup.scadagen.wrapper.wrapper.client.dpc;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.IDPCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.ScsDPCComponentAccess;

/**
 * @author syau
 *
 */
public class DpcMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private static Map<String, DpcMgr> instances = new HashMap<String, DpcMgr>();
	public static DpcMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) { instances.put(key, new DpcMgr()); }
		DpcMgr instance = instances.get(key);
		return instance;
	}

	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsDPCComponentAccess dpcAccess = null;
	private DpcMgr () {
		final String function = "DpcMgr";
		
		logger.begin(function);
		
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
			public void setChangeVarStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeVarStatusResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeStringVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeStringVarForceResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeIntVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeIntVarForceResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeFloatVarForceResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeFloatVarForceResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeEqpTagResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeEqpTagResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
			
			@Override
			public void setChangeEqpStatusResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setChangeEqpStatusResult";
				logger.debug(function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
			}
		});
		
		logger.end(function);
	}
	
	public void connect() {
		final String function = "connect";
		logger.beginEnd(function);
	}
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		try {
			dpcAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dpcAccess=null;
		logger.end(function);
	}
	
	public void sendChangeVarStatus(String key, String scsEnvId, String name, DCP_i.ValidityStatus status) {
		final String function = "sendChangeVarStatus";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] status[{}]", new Object[]{key, scsEnvId, name, status.toString()});
		sendChangeVarStatus(key, scsEnvId, name, status.getValue());
		logger.end(function);
	}
	
	public void sendChangeVarStatus(String key, String scsEnvId, String name, int status) {
		final String function = "sendChangeVarStatus";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] status[{}]", new Object[]{key, scsEnvId, name, status});
		dpcAccess.changeVarStatus(key, scsEnvId, name, status);
		logger.end(function);
	}

	public void sendChangeEqpTag(String key, String scsEnvId, String name, TaggingStatus status, String taggingLabel1, String taggingLabel2) {
		final String function = "sendChangeEqpTag";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] status[{}] taggingLabel1[{}] taggingLabel2[{}]", new Object[]{key, scsEnvId, name, status, taggingLabel1, taggingLabel2});
		dpcAccess.changeEqpTag(key, scsEnvId, name, status.getValue(), taggingLabel1, taggingLabel2);
		logger.end(function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String name, boolean enable, int value) {
		final String function = "sendChangeVarForce";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, name, enable, value});
		if ( enable ) {
			dpcAccess.changeIntVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeIntVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String name, boolean enable, float value) {
		final String function = "sendChangeVarForce";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, name, enable, value});
		if ( enable ) {
			dpcAccess.changeFloatVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeFloatVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(function);
	}
	
	public void sendChangeVarForce ( String key, String scsEnvId, String name, boolean enable, String value) {
		final String function = "sendChangeVarForce";
		logger.begin(function);
		logger.debug(function, "key[{}], scsEnvId[{}] name[{}] enable[{}] value[{}]", new Object[]{key, scsEnvId, name, enable, value});
		if ( enable ) {
			dpcAccess.changeStringVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.FORCED.getValue(), value);
		} else {
			dpcAccess.changeStringVarForce(key, scsEnvId, name, DCP_i.ForcedStatus.NOT_FORCED.getValue(), value);
		}
		logger.end(function);
	}
}
