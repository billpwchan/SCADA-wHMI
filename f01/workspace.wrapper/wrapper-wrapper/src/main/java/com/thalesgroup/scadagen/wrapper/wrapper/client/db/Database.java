package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

/**
 * Database Wrapper for the Inspector Panel
 * <p>
 * Single connector only
 * <p>
 * Recommend to using namespace com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper instead of this class.
 *  
 * @author syau
 *
 */
public class Database implements Connectable_i {
	
	private static final String className = Database.class.getSimpleName();
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(Database.class.getName());

	private String scsEnvId;
	private String parent;
	
	/**
	 * Setter to set the database connection scsEnvId and Parent
	 * @param scsEnvId ScsEnvId to connect
	 * @param parent   parent to connect
	 */
	public void setDynamic(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
	}
	
	private String dynamicClientKey = null;
	/**
	 * Setter to set the Dynamic Client Key
	 * @param dynamicClientKey Dynamic Client Key to set
	 */
	public void setDynamicClientKey(String dynamicClientKey) {
		this.dynamicClientKey = dynamicClientKey;
	}
	
	private DatabaseEvent dynaimcDatabaseEvent = null;
	/**
	 * Setter for the async call reading operation result
	 * 
	 * @param dynaimcDatabaseEvent Callback method for the DatabaseEvent
	 */
	public void setDynamicEvent(DatabaseEvent dynaimcDatabaseEvent) {
		this.dynaimcDatabaseEvent = dynaimcDatabaseEvent;
	}
	
	/**
	 * Store the Request: key and address
	 */
	private Map<String, String[]> KeyAndAddress			= new HashMap<String, String[]>();
	/**
	 * Getter to get the Value by Key
	 * 
	 * @param key Key to mapping the address
	 * @return Values linked to key in parameter
	 */
	public String[] getKeyAndAddress(String key) { return this.KeyAndAddress.get(key); }
	
	/**
	 * Store the Request: key and address
	 */
	private Map<String, String[]> KeyAndValues			= new HashMap<String, String[]>();
	/**
	 * Getter to get the Value by Key
	 * 
	 * @param key Key to mapping the value
	 * @return Values linked to key in parameter
	 */
	public String[] getKeyAndValues(String key) { return this.KeyAndValues.get(key); }
	
	/**
	 * ScsRTDBComponentAccess handle
	 */
	private ScsRTDBComponentAccess rtdb = null;
	
	private LinkedList<JSONRequest> requestStatics		= new LinkedList<JSONRequest>();
	private Map<String, String[]> requestDynamics		= new HashMap<String, String[]>();
	private Map<String, DatabaseEvent> databaseEvents	= new HashMap<String, DatabaseEvent>();
	
	/**
	 * Helper function to print the Caches
	 * 
	 * @param logPrefix Log Prefix
	 */
	private void printCachesStatic(String logPrefix) {
		final String function = "printCachesStatic";
		logger.trace(className, function, logPrefix+" Number of KeyAndAddress.size[{}] KeyAndValues.size[{}]", KeyAndAddress.size(), KeyAndValues.size());
	}
	
	/**
	 * Write Date Value Request 
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param second   Second value to write to target RTDB Address
	 * @param usecond  USecond alue to write to target RTDB Address
	 */
	public void addWriteDateValueRequest(String key, String scsEnvId, String address, long second, long usecond) {
		final String function = "addWriteDateValueRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}] scsEnvId[{}] address[{}]", new Object[]{key, scsEnvId, address});
		logger.info(className, function, "second[{}] usecond[{}]", second, usecond);
		if ( null != rtdb ) {
			rtdb.writeDateValueRequest(key, scsEnvId, address, second, usecond);
		} else {
			logger.warn(className, function, "rtdb IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Write Int Value Request
	 *  
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address 
	 */
	public void addWriteIntValueRequest(String key, String scsEnvId, String address, int value) {
		final String function = "addWriteIntValueRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}] scsEnvId[{}] address[{}]", new Object[]{key, scsEnvId, address});
		logger.info(className, function, "value[{}]", value);
		if ( null != rtdb ) {
			rtdb.writeIntValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "rtdb IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Write Float Value Request 
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address 
	 */
	public void addWriteFloatValueRequest(String key, String scsEnvId, String address, float value) {
		final String function = "addWriteFloatValueRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}] scsEnvId[{}] address[{}]", new Object[]{key, scsEnvId, address});
		logger.info(className, function, "value[{}]", value);
		if ( null != rtdb ) {
			rtdb.writeFloatValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "rtdb IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Write String Value Request
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address
	 */
	public void addWriteStringValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteStringValueRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}] scsEnvId[{}] address[{}]", new Object[]{key, scsEnvId, address});
		logger.info(className, function, "value[{}]", value);
		if ( null != rtdb ) {
			rtdb.writeStringValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "rtdb IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Write Value Result 
	 * 
	 * @param key      Client Key for the writing operation
	 * @param scsEnvId ScsEnvId for the target connector
	 * @param address  DBAddress for the target RTDB Address
	 * @param value    Value to write to target RTDB Address
	 */
	public void addWriteValueRequest(String key, String scsEnvId, String address, String value) {
		final String function = "addWriteValueRequest";
		logger.begin(className, function);
		logger.info(className, function, "key[{}] scsEnvId[{}] address[{}]", new Object[]{key, scsEnvId, address});
		logger.info(className, function, "value[{}]", value);
		if ( null != rtdb ) {
			rtdb.writeValueRequest(key, scsEnvId, address, value);
		} else {
			logger.warn(className, function, "rtdb IS NULL");
		}
		logger.end(className, function);
	}	
	
	/**
	 * Add Static Reading Request
	 * 
	 * @param api           Database API to call
	 * @param clientKey     Key for the Reading and Result
	 * @param scsEnvId      ScsEnvId to connect
	 * @param dbaddresses   Database address to read
	 * @param databaseEvent Callback for result
	 */
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String [] dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "addStaticRequest";
		logger.begin(className, function);
		logger.info(className, function, "api[{}] clientKey[{}] scsEnvId[{}]", new Object[]{api, clientKey, scsEnvId});
		printCachesStatic("Before");		
		if ( logger.isTraceEnabled() ) {
			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
				logger.trace(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
		}
		if ( null != databaseEvent ) {
			if ( KeyAndValues.containsKey(clientKey) ) {
				logger.info(className, function, "clientKey[{}] found in caches, return the value in caches", clientKey);
				String [] values = KeyAndValues.get(clientKey);
				databaseEvent.update(clientKey, values);
			} else {
				logger.info(className, function, "clientKey[{}] not found in caches, send request to database", clientKey);
				requestStatics.add(new JSONRequest(api, clientKey, scsEnvId, dbaddresses));
				KeyAndAddress.put(clientKey, dbaddresses);
				databaseEvents.put(clientKey, databaseEvent);
			}			
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}		
		printCachesStatic("After");
		logger.end(className, function);
	}
	
	/**
	 * Add Static Reading Request
	 * 
	 * @param api           Database API to call
	 * @param clientKey     Key for the Reading and Result
	 * @param scsEnvId      ScsEnvId to connect
	 * @param dbaddresses   Database address to read
	 * @param databaseEvent Callback for result
	 */
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "addStaticRequest";
		logger.begin(dbaddresses, function);
		logger.info(className, function, "api[{}] clientKey[{}] scsEnvId[{}] dbaddresses[{}]", new Object[]{api, clientKey, scsEnvId, dbaddresses});
		printCachesStatic("Before");
		if ( null != databaseEvent ) {
			if ( KeyAndValues.containsKey(clientKey) ) {
				logger.info(className, function, "clientKey[{}] found in caches, return the value in caches", clientKey);
				String [] values = KeyAndValues.get(clientKey);
				databaseEvent.update(clientKey, values);
			} else {
				logger.info(className, function, "clientKey[{}] not found in caches, send request to database", clientKey);
				requestStatics.add(new JSONRequest(api, clientKey, scsEnvId, dbaddresses));
				KeyAndAddress.put(clientKey, new String[]{dbaddresses});
				databaseEvents.put(clientKey, databaseEvent);
			}
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		printCachesStatic("After");
		logger.end(dbaddresses, function);
	}
	
	/**
	 * Add Dynamic Reading Request
	 * 
	 * @param clientKey     Key for the Reading and Result
	 * @param dbaddresses   DBAddress to read
	 * @param databaseEvent Callback for result
	 */
	public void addDynamicRequest(String clientKey, String[] dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "addDynamicRequest";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}] dbaddresses[{}]", clientKey, dbaddresses);
		if ( null != databaseEvent) {
			logger.info(className, function, "send request to database", clientKey);
			requestDynamics.put(clientKey, dbaddresses);
			KeyAndAddress.put(clientKey, dbaddresses);
			databaseEvents.put(clientKey, databaseEvent);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Add Subscription Request
	 * 
	 * @param clientKey     Key for the Reading and Result
	 * @param dbaddresses   DBAddress to read
	 * @param databaseEvent Callback for result
	 */
	public void subscribe(String clientKey, String[] dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "subscribe";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}] dbaddresses[{}]", clientKey, dbaddresses);
		if ( null != databaseEvent) {
			logger.info(className, function, "send request to database", clientKey);
			requestDynamics.put(clientKey, dbaddresses);
			KeyAndAddress.put(clientKey, dbaddresses);
			databaseEvents.put(clientKey, databaseEvent);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Add UnSubscription Request
	 * 
	 * @param clientKey     Key for the Reading and Result
	 * @param dbaddresses   dbaddress to read
	 * @param databaseEvent Callback for result
	 */
	public void unSubscribe(String clientKey) {
		final String function = "unSubscribe";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}]", clientKey);
		
		requestDynamics.remove(clientKey);
		KeyAndAddress.remove(clientKey);
		databaseEvents.remove(clientKey);
		
		logger.end(className, function);
	}
	
	/**
	 * Init and connect to database
	 */
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		rtdb = new ScsRTDBComponentAccess(new IRTDBComponentClient() {

			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
				final String function = "setReadResult";
				logger.begin(className, function);
				
				if ( logger.isTraceEnabled() ) {
				    logger.trace(className, function, "key[{}] errorCode[{}] errorMessage[{}]", new Object[]{key, errorCode, errorMessage});
					for(int i = 0; i < value.length; ++i) {
						logger.trace(className, function, "value[{}][{}]", i, value[i]);
					}
				}
		    	
		    	KeyAndValues.put(key, value);

		    	// Static
				DatabaseEvent databaseEvent = databaseEvents.get(key);
				if ( null != databaseEvent ) databaseEvent.update(key, value);
				
				DataBaseClientKey clientKey = new DataBaseClientKey("_", key);
				// Dynamic
				if ( clientKey.isDynaimc() ) {
					if ( null != dynaimcDatabaseEvent ) {
						dynaimcDatabaseEvent.update(key, value);
					}
				}

				logger.end(className, function);
			}

			@Override
			public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode,
					String errorMessage) {
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
			public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByClassNameResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByUserClassIdResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				final String function = "setGetChildrenResult";
				logger.begin(className, function);

				if ( logger.isTraceEnabled() ) {
					logger.trace(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
					for(int i = 0; i < instances.length; ++i) {
						logger.trace(className, function, "instances[{}][{}]", i, instances[i]);
					}	
				}
		    	
				KeyAndValues.put(clientKey, instances);
				
				// Static
				DatabaseEvent databaseEvent = databaseEvents.get(clientKey);
				if ( null != databaseEvent ) {
					databaseEvent.update(clientKey, instances);
					databaseEvents.remove(clientKey);
				}
				
				logger.end(className, function);
			}

			@Override
			public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo,
					int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount,
					int recordCount, int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula,
					int nbInstances, String scsPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		logger.end(className, function);
	}

	/**
	 * Timer for the database reading
	 */
	private Timer timer = null;
	
	private final String strGetChildren = "GetChildren";
	private final String strMultiReadValue = "multiReadValue";
	
	/**
	 * Timer to execute the request
	 * 
	 * @param periodMillis
	 */
	public void connectTimer(int periodMillis) {
		final String function = "connectTimer";
		logger.begin(className, function);
		
		if ( null == timer ) {
			timer = new Timer() {

				@Override
				public void run() {
					if ( null != rtdb ) {
					
						if ( requestStatics.size() > 0 ) {
							
							logger.begin(className, function+" sendJSONRequest");
							
							JSONRequest jsonRequest = requestStatics.removeFirst();
							
							if ( strGetChildren.equals(jsonRequest.api) ) {
								
								String api = jsonRequest.api;
								String clientKey = jsonRequest.clientKey;
								String scsEnvId = jsonRequest.scsEnvId;
								String dbaddress = jsonRequest.dbaddress;
								
								logger.trace(className, function, "api[{}] key[{}] scsEnvId[{}]",new Object[]{ api, clientKey, scsEnvId});
								
								rtdb.getChildren(clientKey, scsEnvId, dbaddress);
								
							} else if ( strMultiReadValue.equals(jsonRequest.api) ) {
								
								String api = jsonRequest.api;
								String clientKey = jsonRequest.clientKey;
								String scsEnvId = jsonRequest.scsEnvId;
								String[] dbaddresses = jsonRequest.dbaddresses;
								
								logger.trace(className, function, "api[{}] key[{}] scsEnvId[{}]",new Object[]{ api, clientKey, scsEnvId});
								
								rtdb.multiReadValueRequest(clientKey, scsEnvId, dbaddresses);
							}

							logger.end(className, function+" sendJSONRequest");
							
						} else if ( requestDynamics.size() > 0 ) {
							
							LinkedList<String> dbaddresslist = new LinkedList<String>();
							for ( String key : requestDynamics.keySet() ) {
								for ( String dbaddress : requestDynamics.get(key) ) {
									dbaddresslist.add(dbaddress);
								}
							}
							String[] dbaddresses = dbaddresslist.toArray(new String[0]);
							
							KeyAndAddress.put(dynamicClientKey, dbaddresses);

							rtdb.multiReadValueRequest(dynamicClientKey, scsEnvId, dbaddresses);
						}
					}
				}
				
			};
			
			timer.scheduleRepeating(periodMillis);
		}
		logger.end(className, function);
	}
	
	/**
	 * Disconnect the timer
	 */
	public void disconnectTimer() {
		final String function = "disconnectTimer";
		logger.begin(className, function);
		requestStatics.clear();
		requestDynamics.clear();
		
		if ( null != timer ) timer.cancel();
		timer = null;
		logger.end(className, function);
	}
	

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.common.Connectable_i#disconnect()
	 */
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		try {
			rtdb.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtdb=null;
		logger.end(className, function);
	}
}

