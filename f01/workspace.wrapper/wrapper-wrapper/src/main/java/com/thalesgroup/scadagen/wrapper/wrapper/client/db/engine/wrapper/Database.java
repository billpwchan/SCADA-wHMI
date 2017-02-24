package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper;

import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.IPollerComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.ScsPollerComponentAccess;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class Database {
	
	public interface ScsRTDBComponentAccessResult {
		void setReadResult(String key, String[] value, int errorCode, String errorMessage);
	}
	
	private ScsRTDBComponentAccessResult scsRTDBComponentAccessResult = null;
	public void setScsRTDBComponentAccessResult(ScsRTDBComponentAccessResult scsRTDBComponentAccessResult) {
		this.scsRTDBComponentAccessResult = scsRTDBComponentAccessResult;
	}
	
	public interface ScsRTDBComponentAccessGetChildrenResult {
		void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage);
	}
	
	private ScsRTDBComponentAccessGetChildrenResult scsRTDBComponentAccessGetChildrenResult = null;
	public void setScsRTDBComponentAccessGetChildenResult(ScsRTDBComponentAccessGetChildrenResult scsRTDBComponentAccessGetChildrenResult) {
		this.scsRTDBComponentAccessGetChildrenResult = scsRTDBComponentAccessGetChildrenResult;
	}
	
	public interface ScsPollerComponentAccessResult {
		void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
				String[] values, int errorCode, String errorMessage);
	}
	private ScsPollerComponentAccessResult scsPollerComponentAccessResult = null;
	public void setScsPollerComponentAccessResult(ScsPollerComponentAccessResult scsPollerComponentAccessResult) {
		this.scsPollerComponentAccessResult = scsPollerComponentAccessResult;
	}
	
	private ScsRTDBComponentAccess scsRTDBComponentAccess = null;
	private ScsPollerComponentAccess scsPollerComponentAccess = null;
	
	public void connect() {
		
		scsRTDBComponentAccess = new ScsRTDBComponentAccess(new IRTDBComponentClient() {
			
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
			public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
				if ( null != scsRTDBComponentAccessResult ) {
					scsRTDBComponentAccessResult.setReadResult(key, value, errorCode, errorMessage);
				}
			}
			
			@Override
			public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByUserClassIdResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByClassNameResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula, int nbInstances,
					String scsPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				if ( null != scsRTDBComponentAccessGetChildrenResult ) {
					scsRTDBComponentAccessGetChildrenResult.setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
				}
			}
			
			@Override
			public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount, int recordCount,
					int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
		
		scsPollerComponentAccess = new ScsPollerComponentAccess(new IPollerComponentClient() {
			
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
			public void setUnSubscribeResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
					String[] values, int errorCode, String errorMessage) {
				if ( null != scsPollerComponentAccessResult ) {
					scsPollerComponentAccessResult.setSubscribeResult(clientKey, subUUID, pollerState, dbaddress, values, errorCode, errorMessage);
				}
			}
			
			@Override
			public void setDeleteGroupResult(String key, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void disconnect() {
		try {
			scsRTDBComponentAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			scsPollerComponentAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void writeDateValueRequest(String key, String scsEnvId, String address, long second, long usecond) {
		scsRTDBComponentAccess.writeDateValueRequest(key, scsEnvId, address, second, usecond);
	}
	public void writeIntValueRequest(String key, String scsEnvId, String address, int value) {
		scsRTDBComponentAccess.writeIntValueRequest(key, scsEnvId, address, value);
	}
	public void writeFloatValueRequest(String key, String scsEnvId, String address, float value) {
		scsRTDBComponentAccess.writeFloatValueRequest(key, scsEnvId, address, value);
	}
	public void writeStringValueRequest(String key, String scsEnvId, String address, String value) {
		scsRTDBComponentAccess.writeStringValueRequest(key, scsEnvId, address, value);
	}
	public void writeValueRequest(String key, String scsEnvId, String address, String value) {
		scsRTDBComponentAccess.writeValueRequest(key, scsEnvId, address, value);
	}
	
	public void multiReadValueRequest(String key, String scsEnvId, String[] dbaddress) {
		scsRTDBComponentAccess.multiReadValueRequest(key, scsEnvId, dbaddress);
	}
	public void getChildren(String key, String scsEnvId, String dbaddress) {
		scsRTDBComponentAccess.getChildren(key, scsEnvId, dbaddress);
	}

	public void addSubscriptionRequest(String key, String scsEnvId, String groupName, String[] dataFields, int periodMS) {
		scsPollerComponentAccess.subscribe(key, scsEnvId, groupName, dataFields, periodMS);
	}
	public void addUnSubscriptionRequest(String key, String scsEnvId, String groupName, String subscriptionId) {
		scsPollerComponentAccess.unSubscribe(key, scsEnvId, groupName, subscriptionId);
	}
}
