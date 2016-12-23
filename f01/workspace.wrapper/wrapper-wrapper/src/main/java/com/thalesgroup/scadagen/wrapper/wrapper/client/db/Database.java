package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.IPollerComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.ScsPollerComponentAccess;

/**
 * @author syau
 *
 */
public class Database {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(Database.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public Database() {}
	
	private String scsEnvId;
	private String parent;

	
	public void setDynamic(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
	}
	
	private DatabaseEvent dynamicDatabaseEvent = null;
	public void setDynamicEvent(DatabaseEvent dynamicDatabaseEvent) {
		this.dynamicDatabaseEvent = dynamicDatabaseEvent;
		logger.debug(className, "setDynamicEvent", "dynamicDatabaseEvent is set");
	}
	
	/**
	 * Store the Request: key and address
	 */
	private HashMap<String, String[]> KeyAndAddress			= new HashMap<String, String[]>();
	public String[] getKeyAndAddress(String key) { return this.KeyAndAddress.get(key); }
	
	
	/**
	 * Store the Request: key and address
	 */
	private HashMap<String, String[]> KeyAndValues			= new HashMap<String, String[]>();
	public String[] getKeyAndValues(String key) { return this.KeyAndValues.get(key); }
	
	
	/**
	 * Store the Request: key and subscriptionId
	 */
	private HashMap<String, Set<String>> KeyAndSubId			= new HashMap<String, Set<String>>();
	public Set<String> getKeyAndSubId(String key) { return this.KeyAndSubId.get(key); }
	
	
	/**
	 * SCSDatabase handle
	 */
	private ScsRTDBComponentAccess rtdb = null;
	
	/**
	 * SCSDBPoller handle
	 */
	private ScsPollerComponentAccess poller = null;
	
	private LinkedList<JSONRequest> requestStatics			= new LinkedList<JSONRequest>();
	private HashMap<String, String[]> requestDynamics		= new HashMap<String, String[]>();
	private HashMap<String, DatabaseEvent> databaseEvents	= new HashMap<String, DatabaseEvent>();
	
	private void printCachesStatic(String logPrefix) {
		final String function = "printCachesStatic";
		logger.debug(className, function, logPrefix+" Number of KeyAndAddress.size[{}] KeyAndValues.size[{}]", KeyAndAddress.size(), KeyAndValues.size());
	}
	
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
	 * @param api : Database API to call
	 * @param clientKey : Key for the Reading and Result
	 * @param scsEnvId : scsEnvId to connect
	 * @param dbaddresses : database address to read
	 * @param databaseEvent : Callback for result
	 */
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String [] dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "addStaticRequest";
		logger.begin(className, function);
		logger.info(className, function, "api[{}] clientKey[{}] scsEnvId[{}]", new Object[]{api, clientKey, scsEnvId});
		printCachesStatic("Before");		
		if ( logger.isDebugEnabled() ) {
			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
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
				logger.debug(className, function, "requestStatics.put([{}], [{}]", clientKey, dbaddresses);
				KeyAndAddress.put(clientKey, dbaddresses);
				logger.debug(className, function, "KeyAndAddress.put([{}], [{}]", clientKey, dbaddresses);
				databaseEvents.put(clientKey, databaseEvent);
				logger.debug(className, function, "databaseEvents.put([{}], databaseEvent)", clientKey);
			}			
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}		
		printCachesStatic("After");
		logger.end(className, function);
	}
	
	/**
	 * @param api : Database API to call
	 * @param clientKey : Key for the Reading and Result
	 * @param scsEnvId : scsEnvId to connect
	 * @param dbaddresses : database address to read
	 * @param databaseEvent : Callback for result
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
				logger.debug(className, function, "requestStatics.put([{}], [{}]", clientKey, dbaddresses);
				KeyAndAddress.put(clientKey, new String[]{dbaddresses});
				logger.debug(className, function, "KeyAndAddress.put([{}], [{}]", clientKey, dbaddresses);
				databaseEvents.put(clientKey, databaseEvent);
				logger.debug(className, function, "databaseEvents.put([{}], databaseEvent)", clientKey);
			}
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		printCachesStatic("After");
		logger.end(dbaddresses, function);
	}
	
	/**
	 * @param clientKey : Key for the Reading and Result
	 * @param dbaddresses : dbaddress to read
	 * @param databaseEvent : Callback for result
	 */
//	public void addDynamicRequest(String clientKey, String[] dbaddresses, DatabaseEvent databaseEvent) {
//		final String function = "addDynamicRequest";
//		logger.begin(className, function);
//		logger.info(className, function, "clientKey[{}] dbaddresses[{}]", clientKey, dbaddresses);
//		if ( null != databaseEvent) {
//			logger.info(className, function, "send request to database", clientKey);
//			requestDynamics.put(clientKey, dbaddresses);
//			logger.debug(className, function, "requestDynamics.put([{}], [{}]", clientKey, dbaddresses);
//			KeyAndAddress.put(clientKey, dbaddresses);
//			logger.debug(className, function, "KeyAndAddress.put([{}], [{}]", clientKey, dbaddresses);
//			databaseEvents.put(clientKey, databaseEvent);
//			logger.debug(className, function, "databaseEvents.put([{}], databaseEvent)", clientKey);
//		} else {
//			logger.warn(className, function, "databaseEvent IS NULL");
//		}
//		logger.end(className, function);
//	}
	

	
	/**
	 * @param clientKey : Key for the Reading and Result
	 * @param dbaddresses : dbaddress to read
	 * @param databaseEvent : Callback for result
	 */
	public void subscribe(String clientKey, String[] dbaddresses, DatabaseEvent databaseEvent) {
		final String function = "subscribe";
		logger.begin(className, function);
		logger.info(className, function, "clientKey[{}] dbaddresses[{}]", clientKey, dbaddresses);
		if ( null != databaseEvent) {
			logger.info(className, function, "send request to database", clientKey);
			
			requestDynamics.put(clientKey, dbaddresses);
			logger.debug(className, function, "requestDynamics.put([{}], [{}]", clientKey, dbaddresses);
			for (int i=0; i<dbaddresses.length; i++) {
				logger.debug(className, function, "requestDynamics dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			
			KeyAndAddress.put(clientKey, dbaddresses);
			logger.debug(className, function, "KeyAndAddress.put([{}], [{}]", clientKey, dbaddresses);
			for (int i=0; i<dbaddresses.length; i++) {
				logger.debug(className, function, "KeyAndAddress dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			
			databaseEvents.put(clientKey, databaseEvent);
			logger.debug(className, function, "databaseEvents.put([{}], databaseEvent)", clientKey);
		} else {
			logger.warn(className, function, "databaseEvent IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * @param clientKey : Key for the Reading and Result
	 * @param dbaddresses : dbaddress to read
	 * @param databaseEvent : Callback for result
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
				
				if ( logger.isDebugEnabled() ) {				
					if ( logger.isDebugEnabled() ) {
				    	logger.debug(className, function, "key[{}] errorCode[{}] errorMessage[{}]", new Object[]{key, errorCode, errorMessage});
				    	
						for(int i = 0; i < value.length; ++i) {
							logger.debug(className, function, "value[{}][{}]", i, value[i]);
						}	
					}
				}

		    	
		    	KeyAndValues.put(key, value);
		    	logger.debug(className, function, "KeyAndValues.put([{}], [{}])", key, value);
		    	
//		    	updateValue(key, value);
				
		    	// Static
				DatabaseEvent databaseEvent = databaseEvents.get(key);
				if ( null != databaseEvent ) databaseEvent.update(key, value);
				
				// Dynamic
//				if ( "dynamic".equals(key.split("_")[2]) ) {
//					if ( null != dynaimcDatabaseEvent ) {
//						dynaimcDatabaseEvent.update(key, value);
//					}
//				}

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

				if ( logger.isDebugEnabled() ) {
					logger.debug(className, function, "clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{clientKey, errorCode, errorMessage});
					for(int i = 0; i < instances.length; ++i) {
						logger.debug(className, function, "instances[{}][{}]", i, instances[i]);
					}	
				}
		    	
				KeyAndValues.put(clientKey, instances);
				logger.debug(className, function, "KeyAndValues.put([{}], [{}])", clientKey, instances);
				
//				updateValue(clientKey, instances);
				
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
		
		poller = new ScsPollerComponentAccess(new IPollerComponentClient() {

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
			public void setDeleteGroupResult(String key, int errorCode, String errorMessage) {
				
			}

			@Override
			public void setSubscribeResult(String key, String subUUID, String pollerState, String[] dbaddress,
					String[] values, int errorCode, String errorMessage) {
				final String function = "setSubscribeResult";
				
				if (getKeyAndSubId(key) == null) {
					Set<String> set = new HashSet<String>();
					set.add(subUUID);
					KeyAndSubId.put(key, set);
				}
				
				logger.begin(className, function);
				
				if ( logger.isDebugEnabled() ) {				
					if ( logger.isDebugEnabled() ) {
				    	logger.debug(className, function, "key[{}] errorCode[{}] errorMessage[{}]", new Object[]{key, errorCode, errorMessage});
				    	
						for(int i = 0; i < values.length; ++i) {
							logger.debug(className, function, "value[{}][{}]", i, values[i]);
						}	
					}
				}
				
				String [] savedDBAddresses = KeyAndAddress.get(key);
				String [] savedValues = KeyAndValues.get(key);
		
	    		if (savedDBAddresses != null && savedValues != null) {
	    			// GWT 2.4 does not support java String array clone or copyOf operations!!
					String [] updateValues = new String [savedValues.length];
					if (updateValues != null) {
		    			for (int i=0; i<savedDBAddresses.length; i++) {
		    				boolean found = false;
		    				for (int j=0; j<dbaddress.length; j++) {
		    					if (savedDBAddresses[i].compareTo(dbaddress[j]) == 0) {
		    						updateValues[i] = values[j];
		    						found = true;
		    						break;
		    					}
		    				}
		    				if (!found) {
		    					updateValues[i] = savedValues[i];
		    				}
		    			}
		    			KeyAndValues.put(key, updateValues);
					}
	    		} else {
	    			KeyAndValues.put(key, values);
	    		}
		    	
		    	//logger.debug(className, function, "KeyAndValues.put([{}], [{}])", key, values);
	
				// Dynamic
				if ( "dynamic".equals(key.split("_")[2]) ) {
					if ( null != dynamicDatabaseEvent ) {
						logger.debug(className, function, "dynamicDatabaseEvent.update");
						dynamicDatabaseEvent.update(key, values);
					} else {
						logger.warn(className, function, "dynamicDatabaseEvent is null");
					}
				} else {
					logger.debug(className, function, "key does not match dynamic");
				}

				logger.end(className, function);
			}

			@Override
			public void setUnSubscribeResult(String clientKey, int errorCode, String errorMessage) {
				if (errorCode != 0) {
					logger.error("unsubscribe failed. " + errorMessage);
				} else {
					logger.debug("unsubscribe success");
				}
			}

			
		});
		
		logger.end(className, function);
	}
	
	/**
	 * Timer for the database reading
	 */
	private Timer timer = null;
	
	/**
	 * Timer to execute the request
	 * @param periodMillis
	 */
	public void connectTimer(int periodMillis) {
		final String function = "connectTimer";
		
		final String strGetChildren = "GetChildren";
		final String strMultiReadValue = "multiReadValue";
		final int subscribePeriod = periodMillis;
		
		logger.begin(className, function);
		
		if ( null == timer ) {
			timer = new Timer() {
				boolean subscribeSent = false;

				@Override
				public void run() {
					if ( null != rtdb ) {
					
						if ( requestStatics.size() > 0 ) {
							
							logger.begin(className, function+" sendJSONRequest");
							
							JSONRequest jsonRequest = requestStatics.removeFirst();
							
							if (jsonRequest != null) {
							
								if ( jsonRequest.dbaddress != null && strGetChildren.equals(jsonRequest.api) ) {
									
									String api = jsonRequest.api;
									String clientKey = jsonRequest.clientKey;
									String scsEnvId = jsonRequest.scsEnvId;
									String dbaddress = jsonRequest.dbaddress;
									
									logger.debug(className, function, "api[{}] key[{}] scsEnvId[{}]",new Object[]{ api, clientKey, scsEnvId});
									
									rtdb.getChildren(clientKey, scsEnvId, dbaddress);
									
								} else if ( jsonRequest.dbaddresses != null && jsonRequest.dbaddresses.length > 0 && strMultiReadValue.equals(jsonRequest.api) ) {
									
									String api = jsonRequest.api;
									String clientKey = jsonRequest.clientKey;
									String scsEnvId = jsonRequest.scsEnvId;
									String[] dbaddresses = jsonRequest.dbaddresses;
									
									logger.debug(className, function, "api[{}] key[{}] scsEnvId[{}]",new Object[]{ api, clientKey, scsEnvId});
									
									rtdb.multiReadValueRequest(clientKey, scsEnvId, dbaddresses);
								}
							} else {
								logger.debug(className, function, "jsonRequest dbaddresses is empty");
							}

							logger.end(className, function+" sendJSONRequest");
							
//						} else if ( requestDynamics.size() > 0 ) {
//							
//							LinkedList<String> dbaddresslist = new LinkedList<String>();
//							for ( String key : requestDynamics.keySet() ) {
//								for ( String dbaddress : requestDynamics.get(key) ) {
//									dbaddresslist.add(dbaddress);
//								}
//							}
//							String[] dbaddresses = dbaddresslist.toArray(new String[0]);
//							
//							String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
//							
//							KeyAndAddress.put(clientKey, dbaddresses);
//
//							rtdb.multiReadValueRequest(clientKey, scsEnvId, dbaddresses);
						}
					}
					if (null != poller) {
						if ( !subscribeSent && requestDynamics.size() > 0 ) {
							
							LinkedList<String> dbaddresslist = new LinkedList<String>();
							for ( String key : requestDynamics.keySet() ) {
								logger.debug(className, function, "requestDynamics key [{}]", key);
								for ( String dbaddress : requestDynamics.get(key) ) {
									logger.debug(className, function, "requestDynamics dbaddress [{}]", dbaddress);
									if (!dbaddresslist.contains(dbaddress))	{
										dbaddresslist.add(dbaddress);
										logger.debug(className, function, "Added to subscription list [{}] ", dbaddress);
									} else {
										logger.debug(className, function, "NOT Added to subscription list [{}] ", dbaddress);
									}
								}
							}
							String[] dbaddresses = dbaddresslist.toArray(new String[0]);
							
							String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
							
							KeyAndAddress.put(clientKey, dbaddresses);
							logger.debug(className, function, "KeyAndAddress.put([{}], [{}]", clientKey, dbaddresses);

							poller.subscribe(clientKey, scsEnvId, clientKey, dbaddresses, subscribePeriod);
							subscribeSent = true;
						}
					}
				}
				
			};
			
			timer.scheduleRepeating(periodMillis);
		}

		logger.end(className, function);
	}
	
	/**
	 * Connect to database
	 */
	public void disconnectTimer() {
		requestStatics.clear();
		requestDynamics.clear();
		
		if ( null != timer ) timer.cancel();
		timer = null;
	}
	
	/**
	 * Disconnect to database
	 */
	public void disconnect() {

		try {
			rtdb.terminate();
			
			String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
			
			Set<String> subSet = KeyAndSubId.get(clientKey);
			if (subSet != null) {
				String subscriptionId = subSet.iterator().next();
				if (subscriptionId != null) {
					poller.unSubscribe(clientKey, scsEnvId, clientKey, subscriptionId);
			
					KeyAndSubId.remove(subscriptionId);
				}
			}
			poller.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtdb=null;
		poller=null;
	}
}
