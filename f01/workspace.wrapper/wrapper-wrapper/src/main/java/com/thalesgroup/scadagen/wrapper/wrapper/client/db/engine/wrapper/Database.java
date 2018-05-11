package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper;

import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.IPollerComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.ScsPollerComponentAccess;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

/**
 * Wrapper in SCADAgen WHMI Level for the SCADAsoft ScsRTDBComponent
 * 
 * @author syau
 *
 */
public class Database {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	/**
	 * Interface for the async call reading operation result
	 * 
	 * @author syau
	 *
	 */
	public interface ScsRTDBComponentAccessResult {
		
		/**
		 * Return the Reading result
		 * 
		 * @param key          Client Key for the reading operation
		 * @param value        Result value from the reading operation
		 * @param errorCode    Error Code return from the reading operation
		 * @param errorMessage Error Message return from the reading operation
		 */
		void setReadResult(String key, String[] value, int errorCode, String errorMessage);
	}
	
	private ScsRTDBComponentAccessResult scsRTDBComponentAccessResult = null;
	
	/**
	 *  Setter for the asynchronous callback for the Get Children result
	 *  
	 * @param scsRTDBComponentAccessResult
	 */
	public void setScsRTDBComponentAccessResult(ScsRTDBComponentAccessResult scsRTDBComponentAccessResult) {
		this.scsRTDBComponentAccessResult = scsRTDBComponentAccessResult;
	}
	
	/**
	 * Interface for the Asynchronous callback of Get Children result
	 * 
	 * @author syau
	 *
	 */
	public interface ScsRTDBComponentAccessGetChildrenResult {
		
		/**
		 * @param clientKey    Client Key for the reading operation
		 * @param instances    Result value from the reading operation
		 * @param errorCode    Error Code return from the reading operation
		 * @param errorMessage Error Message return from the reading operation
		 */
		void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage);
	}
	
	private ScsRTDBComponentAccessGetChildrenResult scsRTDBComponentAccessGetChildrenResult = null;
	
	/**
	 * Setter for the asynchronous callback for the Get Children result
	 * 
	 * @param scsRTDBComponentAccessGetChildrenResult
	 */
	public void setScsRTDBComponentAccessGetChildenResult(ScsRTDBComponentAccessGetChildrenResult scsRTDBComponentAccessGetChildrenResult) {
		this.scsRTDBComponentAccessGetChildrenResult = scsRTDBComponentAccessGetChildrenResult;
	}
	
	/**
	 * Interface for the Asynchronous callback of Get Children result
	 * 
	 * @author syau
	 *
	 */
	public interface ScsRTDBComponentAccessGetFullPathResult {
		
		/**
		 * @param clientKey    Client Key for the reading operation
		 * @param fullPath    Result value from the reading operation
		 * @param errorCode    Error Code return from the reading operation
		 * @param errorMessage Error Message return from the reading operation
		 */
		void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage);
	}
	
	private ScsRTDBComponentAccessGetFullPathResult scsRTDBComponentAccessGetFullPathResult = null;
	
	/**
	 * Setter for the asynchronous callback for the Get Children result
	 * 
	 * @param scsRTDBComponentAccessGetChildrenResult
	 */
	public void setScsRTDBComponentAccessGetFullPathResult(ScsRTDBComponentAccessGetFullPathResult scsRTDBComponentAccessGetFullPathResult) {
		this.scsRTDBComponentAccessGetFullPathResult = scsRTDBComponentAccessGetFullPathResult;
	}
	
	/**
	 * Interface for the Asynchronous callback of Poller result
	 * 
	 * @author syau
	 *
	 */
	public interface ScsPollerComponentAccessResult {
		
		/**
		 * Interface for the Asynchronous callback of Get Children result
		 * 
		 * @param clientKey    Client Key for the reading operation
		 * @param subUUID
		 * @param pollerState
		 * @param dbaddress
		 * @param values       Result values from the reading operation
		 * @param errorCode    Error Code return from the reading operation
		 * @param errorMessage Error Message return from the reading operation
		 */
		void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
				String[] values, int errorCode, String errorMessage);
	}
	
	private ScsPollerComponentAccessResult scsPollerComponentAccessResult = null;
	/**
	 * Setter for the asynchronous callback for the Poller Component result
	 * 
	 * @param scsPollerComponentAccessResult
	 */
	public void setScsPollerComponentAccessResult(ScsPollerComponentAccessResult scsPollerComponentAccessResult) {
		this.scsPollerComponentAccessResult = scsPollerComponentAccessResult;
	}
	
	/**
	 * Instance for the ScsRTDBComponentAccess
	 */
	private ScsRTDBComponentAccess scsRTDBComponentAccess = null;
	
	/**
	 * Instance for the ScsPollerComponentAccess
	 */
	private ScsPollerComponentAccess scsPollerComponentAccess = null;
	
	/**
	 * Init the database object.
	 * Do the connection
	 */
	public void connect() {
		String function = "connect";
		logger.begin(className, function);
		
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
				if ( null != scsRTDBComponentAccessGetFullPathResult ) {
					scsRTDBComponentAccessGetFullPathResult.setGetFullPathResult(clientKey, fullPath, errorCode, errorMessage);
				}
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
		
		logger.end(className, function);
	}
	
	
	/**
	 * Disconnect the database and terminate the database object
	 */
	public void disconnect() {
		String function = "disconnect";
		logger.begin(className, function);
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
		logger.end(className, function);
	}

	
	/**
	 * Write Date Value Request 
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param second   Second value to write to target RTDB Address
	 * @param usecond  USecond alue to write to target RTDB Address
	 */
	public void writeDateValueRequest(String key, String scsEnvId, String address, long second, long usecond) {
//		String function = "writeDateValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.writeDateValueRequest(key, scsEnvId, address, second, usecond);
	}
	
	/**
	 * Write Int Value Request 
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address 
	 */
	public void writeIntValueRequest(String key, String scsEnvId, String address, int value) {
//		String function = "writeIntValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.writeIntValueRequest(key, scsEnvId, address, value);
	}
	
	/**
	 * Write Float Value Request 
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address
	 */
	public void writeFloatValueRequest(String key, String scsEnvId, String address, float value) {
//		String function = "writeFloatValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.writeFloatValueRequest(key, scsEnvId, address, value);
	}
	
	/**
	 * Write String Value Request
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address
	 */
	public void writeStringValueRequest(String key, String scsEnvId, String address, String value) {
//		String function = "writeStringValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.writeStringValueRequest(key, scsEnvId, address, value);
	}
	
	/**
	 * Write Value Result 
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address
	 */
	public void writeValueRequest(String key, String scsEnvId, String address, String value) {
//		String function = "writeValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.writeValueRequest(key, scsEnvId, address, value);
	}
	
	/**
	 * Multi Read Value Request
	 * Wrapper function base on the ScsRTDBComponentAccess
	 * 
	 * @param key       Client Key for the reading operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbaddress DBAddress for the target RTDB Address
	 */
	public void multiReadValueRequest(String key, String scsEnvId, String[] dbaddress) {
//		String function = "multiReadValueRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.multiReadValueRequest(key, scsEnvId, dbaddress);
	}
	
	/**
	 * Get Children Request
	 * Wrapper function base on the ScsRTDBComponentAccess
	 *  
	 * @param key       Client Key for the reading operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbaddress DBAddress for the target RTDB Address
	 */
	public void getChildren(String key, String scsEnvId, String dbaddress) {
//		String function = "getChildren";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.getChildren(key, scsEnvId, dbaddress);
	}
	
	/**
	 * Get FullPath Request
	 * Wrapper function base on the ScsRTDBComponentAccess
	 *  
	 * @param key       Client Key for the reading operation
	 * @param scsEnvId  ScsEnvId for the target connector
	 * @param dbaddress DBAddress for the target RTDB Address
	 */
	public void getFullPath(String key, String scsEnvId, String dbaddress) {
//		String function = "getFullPath";
//		HVLifeCycleState.ensureIsActivated(className, function, scsRTDBComponentAccess);
		scsRTDBComponentAccess.getFullPath(key, scsEnvId, dbaddress);
	}

	/**
	 * Add Subscription Request
	 * Wrapper function base on the ScsPollerComponentAccess
	 * 
	 * @param key        Client Key for the subscription operation
	 * @param scsEnvId   ScsEnvId for the target connector
	 * @param groupName  Group Name to UnSubscription Request
	 * @param dataFields DBAddresses for the target RTDB Addreses
	 * @param periodMS   period in the MS
	 */
	public void addSubscriptionRequest(String key, String scsEnvId, String groupName, String[] dataFields, int periodMS) {
//		String function = "addSubscriptionRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsPollerComponentAccess);
		scsPollerComponentAccess.subscribe(key, scsEnvId, groupName, dataFields, periodMS);
	}
	
	/**
	 * Add UnSubscription Request
	 * Wrapper function base on the ScsPollerComponentAccess
	 *  
	 * @param key            Client Key for the subscription operation
	 * @param scsEnvId       ScsEnvId for the target connector
	 * @param groupName      Group Name to UnSubscription Request
	 * @param subscriptionId Subscription Id to UnSubscription Request
	 */
	public void addUnSubscriptionRequest(String key, String scsEnvId, String groupName, String subscriptionId) {
//		String function = "addUnSubscriptionRequest";
//		HVLifeCycleState.ensureIsActivated(className, function, scsPollerComponentAccess);
		scsPollerComponentAccess.unSubscribe(key, scsEnvId, groupName, subscriptionId);
	}
}
