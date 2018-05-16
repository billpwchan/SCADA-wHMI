package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class WrapperScsRTDBAccess {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());	
	
	private final String strWaitingList		= "waitingList";
	private final String strSubscriptions	= "subscriptions";
	private final String strGetChildrens	= "getChildrens";
	
	private Map<String, List<ReadRequest>> lists = null;

	// List
	private List<ReadRequest> waitingList		= null;
	private List<ReadRequest> reading			= null;
	private List<ReadRequest> subscriptions	= null;
	private List<ReadRequest> getChildrens	= null;
	
	private Map<String, MultiReadResult> multiReadMap = new HashMap<String, MultiReadResult>();
	private Map<String, GetChildrenResult> getChildrenMap = new HashMap<String, GetChildrenResult>();
	
	public void remove(String prefix) {
		String f = "remove";
		logger.begin(f);
		logger.debug(f, "prefix[{}]", prefix);
		remove(strWaitingList, prefix);
		remove(strSubscriptions, prefix);
		remove(strGetChildrens, prefix);
		logger.end(f);
	}
	
	public void remove(String key, String prefix) {
		String f = "remove";
		List<ReadRequest> list = lists.get(key);
		logger.begin(f);
		logger.debug(f, "list[{}] prefix[{}]", key, prefix);
    	Iterator<ReadRequest> readRequests = list.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();
    		if ( null != readRequest && readRequest.clientKey != null && readRequest.clientKey.startsWith(prefix) ) {
    			readRequests.remove();
    		}
    	}
    	logger.end(f);
	}
	
	private boolean enableCache = false;
	public void setEnableCache(boolean enable) { this.enableCache = enable; }
	
	private static WrapperScsRTDBAccess instance = null;
	public static WrapperScsRTDBAccess getInstance() {
		if ( null == instance ) instance = new WrapperScsRTDBAccess();
		return instance;
	}
	
	private Timer timer = null;
	private ScsRTDBComponentAccess rtdb = null;
	private WrapperScsRTDBAccess () {
		final String f = "WrapperScsRTDBAccess";
		
		logger.begin(f);
		
		lists = new HashMap<String, List<ReadRequest>>();

		// List
		waitingList		= new LinkedList<ReadRequest>();
		subscriptions	= new LinkedList<ReadRequest>();
		getChildrens	= new LinkedList<ReadRequest>();
		
		lists.put(strWaitingList, waitingList);
		lists.put(strSubscriptions, subscriptions);
		lists.put(strGetChildrens, getChildrens);
		
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
			public void setReadResult(String clientKey, String[] value, int errorCode, String errorMessage) {
				String f2 = f + " setReadResult";
				logger.begin(f2);
				
				//setReadResponse(clientKey, value, errorCode, errorMessage);
				
				MultiReadResult multiReadRes = multiReadMap.get(clientKey);

				if (multiReadRes != null) {
					multiReadRes.setReadResult(clientKey, value, errorCode, errorMessage);
					multiReadMap.remove(clientKey);
				}
				
				logger.end(f2);
				
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
				String f2 = f + " setGetChildrenResult";
				logger.begin(f2);
				
				//setGetChildrenResponse(clientKey, instances, errorCode, errorMessage);
				
				GetChildrenResult getChildrenRes = getChildrenMap.get(clientKey);

				if (getChildrenRes != null) {
					getChildrenRes.setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
					getChildrenMap.remove(clientKey);
				} else {
					logger.warn(f2, "result callback not found");
				}
				
				logger.end(f2);
				
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
		
		logger.end(f);
	}
	
	public void startTimer(int interval) {
		final String f = "startTimer";
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					String f2 = f + "run";
					
					logger.debug(f2, "waitingChildren[{}]", waitingChildren);
					
					logger.debug(f2, "waitingMultiRead[{}]", waitingMultiRead);
					
					if ( ! waitingChildren ) {
						if ( ! waitingMultiRead ) {
							
							List<ReadRequest> list = lists.get(strWaitingList);
							if ( null != list && list.size() > 0 ) {
								ReadRequest readRequest = ((LinkedList<ReadRequest>) list).removeFirst();
								
								if ( 0 == ChildrenResult.class.getName().compareTo(readRequest.resultName) ) {
									getChildren(readRequest);
								} else {
									performMultiReadValueRequest(readRequest);
								}
													
							}

						}
					}
					
				};
			};
			timer.scheduleRepeating(interval);			
		} else {
			logger.debug(f, "timer EXISTS");
		}
		
	}
	

//	private boolean multiReadValueRequest(ReadRequest readRequest) {
//		logger.info("multiReadValueRequest Begin");
//		
//		String clientKey = readRequest.clientKey;
//		String scsEnvId = readRequest.scsEnvId;
//		String[] dbaddresses = readRequest.dbaddresses;
//		Result readResult = readRequest.result;
//		boolean result = multiReadValueRequest(clientKey, scsEnvId, dbaddresses, readRequest.resultName, readResult);
//		
//		logger.info("multiReadValueRequest End");
//		return result;
//	}
    
    public boolean multiReadValueRequest(String key, String scsEnvId, String[] dbaddresses, String resultName, Result readResult) {
    	String f = "multiReadValueRequest";
    	boolean result = false;
    	
    	logger.begin(f);
    	
    	logger.info(f, "key[{}] scsEnvId[{}] putting to waiting list", key, scsEnvId);
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, resultName, readResult);
    		
    	waitingList.add(readRequest);
    	
    	logger.end(f);
        
		return result;
    }
    
    private boolean waitingMultiRead = false;
    private void performMultiReadValueRequest(ReadRequest readRequest) {
    	String f = "performMultiReadValueRequest";
    	logger.begin(f);

		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		
		logger.debug(f, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
		
		for(int i = 0; i < dbaddresses.length; ++i ) {
			logger.debug(f, "dbaddresses({})[{}]", i, dbaddresses[i]);
		}

		waitingMultiRead = true;
		
		reading.add(readRequest);
		
		this.rtdb.multiReadValueRequest(clientKey, scsEnvId, dbaddresses);
		
		logger.end(f);
    }
    
    /**
     * 
     * @param key
     * @param scsEnvId
     * @param dbaddresses
     * @param readResult
     */
    public void multiReadValueRequestCaches(String clientKey, String scsEnvId, String[] dbaddresses, String resultName, ReadResult readResult) {
    	String f = "multiReadValueRequestCaches";
    	logger.begin(f);
		
    	logger.debug(f, "enableCache[{}]", enableCache);

		if ( enableCache && readCashed.containsKey(clientKey) ) {
			
			logger.debug(f, "clientKey[{}] FOUND", clientKey);

			String[][] values = readCashed.get(clientKey);
			
			readResult.setReadResult(clientKey, values, 0, "");
			
		} else {
			
			multiReadValueRequest(clientKey, scsEnvId, dbaddresses, resultName, readResult);
			
		}
		
		logger.end(f);
    }
    
    private Map<String, String[][]> readCashed = new HashMap<String, String[][]>();
    private void setReadResponse(String clientKey, String[] values, int errorCode, String errorMessage) {
    	String f = "setReadResponse";
    	logger.begin(f);
    	logger.debug(f, "clientKey[{}]", clientKey);
    	logger.debug(f, "errorCode[{}]", errorCode);
    	logger.debug(f, "errorMessage[{}]", errorMessage);

    	logger.debug(f, "ReadResult.class.getName()[{}]", ReadResult.class.getName());
    	logger.debug(f, "SubscriptionResult.class.getName()[{}]", SubscriptionResult.class.getName());
    	
    	waitingMultiRead = false;

		// Remove the result from the reading list
    	Iterator<ReadRequest> readRequests = reading.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();

    		logger.debug(f, "readRequest.clientKey[{}]", readRequest.clientKey);
    		
    		if ( readRequest.equalToKey(clientKey) ) {
    			Result result = readRequest.result;
    			
    			logger.debug(f, "readRequest.resultName[{}]", readRequest.resultName);
    			
    			String results[][] = new String[values.length][];
    			for ( int i = 0 ; i < values.length ; ++i ) {
    				results[i] = new String[]{readRequest.dbaddresses[i], values[i]};
    			}
    			if ( 0 == ReadResult.class.getName().compareTo(readRequest.resultName) ) {
    				
    				readCashed.put(clientKey, results);
    				
    				((ReadResult)result).setReadResult(clientKey, results, errorCode, errorMessage);
    			} else if ( 0 == SubscriptionResult.class.getName().compareTo(readRequest.resultName) ) {
    				((SubscriptionResult)result).setReadResultSubscription(clientKey, results, errorCode, errorMessage);
    			}

    			readRequests.remove();
    		}
    	}
		for(int i = 0 ; i < values.length ; ++i ) {
			logger.debug(f, "values({}) values[{}]", i, values[i]);
		}
		logger.end(f);
    }
	
    public boolean subscriptionRequest(String key, String scsEnvId, String[] dbaddresses, SubscriptionResult subscription) {
    	String f = "subscriptionRequest";
    	logger.begin(f);
    	
    	logger.debug(f, "key[{}]", key);
    	logger.debug(f, "scsEnvId[{}]", scsEnvId);
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.debug(f, "dbaddresses({})[{}]", i, dbaddresses[i]);
    	}
    	
    	Iterator<ReadRequest> iterator = subscriptions.iterator();
    	while ( iterator.hasNext() ) {
    		ReadRequest readRequest = iterator.next();
    		
    		if ( readRequest.equalToKey(key) ) {
    			logger.debug(f, "subscriptionRequest key[{}] ALREADY EXISTS, subscription CANCELED", key);
        		return false;
    		}
    	}
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, SubscriptionResult.class.getName(), subscription);
    	
    	logger.debug(f, "add to subscriptions list");
    	
    	subscriptions.add(readRequest);
    	
    	logger.end(f);
            
        return true;
    }

    
    private HashMap<String, String[]> childrenCached = new HashMap<String, String[]>();
    private boolean waitingChildren = false;
    public void getChildren(String clientKey, String scsEnvId, String dbaddress, ChildrenResult readChildren) {
    	String f = "getChildren";
    	logger.begin(f);
    	logger.debug(f, "clientKey[{}]", clientKey);
    	logger.debug(f, "scsEnvId[{}]", scsEnvId);
    	logger.debug(f, "dbaddress[{}]", dbaddress);
    	
    	logger.info("getChildren enableCache["+enableCache+"]");
    	
    	if ( enableCache && childrenCached.containsKey(clientKey) ) {
    		
    		logger.debug(f, "clientKey[{}] FOUND", clientKey);
    		
    		String[] instances = childrenCached.get(clientKey);
    		
    		readChildren.setGetChildrenResult(clientKey, instances, 0, "");
    		
    	} else {
    		
    		ReadRequest readRequest = new ReadRequest(clientKey, scsEnvId, new String[]{dbaddress}, ChildrenResult.class.getName(), readChildren);
    		
 			waitingList.add(readRequest);   		
    		
    	}

    	logger.end(f);
    }
    
    private void getChildren(ReadRequest readRequest) {
    	
		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		
		String dbaddress = dbaddresses[0];
		    
		getChildrens.add(readRequest);
		
		waitingChildren = true;
		
		this.rtdb.getChildren(clientKey, scsEnvId, dbaddress);
    }
    
    private void setGetChildrenResponse(String clientKey, String[] instances, int errorCode, String errorMessage) {
    	String f = "setGetChildrenResponse";
    	logger.begin(f);
    	logger.debug(f, "clientKey[{}]", clientKey);
    	logger.debug(f, "errorCode[{}]", errorCode);
    	logger.debug(f, "errorMessage[{}]", errorMessage);
    	
        logger.debug(f, "ChildrenResult.class.getName()[{}]", ChildrenResult.class.getName());
        
        waitingChildren = false;
        
        childrenCached.put(clientKey, instances);
       
    	if ( null != instances ) {
    		// Remove the result from the reading list
        	Iterator<ReadRequest> readRequests = getChildrens.iterator();
        	while ( readRequests.hasNext() ) {
        		ReadRequest readRequest = readRequests.next();
        		
        		logger.debug(f, "readRequest.clientKey[{}]", readRequest.clientKey);
        		
        		if ( readRequest.equalToKey(clientKey) ) {
        			Result result = readRequest.result;
        			
        			logger.debug(f, "readRequest.resultName[{}]", readRequest.resultName);
        			
        			if ( 0 == ChildrenResult.class.getName().compareTo(readRequest.resultName) ) {
        				((ChildrenResult)result).setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
        			}
	
        			logger.debug(f, "remove readRequest");

        		}
        	}
			for(int i = 0 ; i < instances.length ; ++i ) {
				logger.debug(f, "instances({}) instances[{}]", i, instances[i]);
			}
    	} else {
    		logger.debug(f, "instances IS NULL");
    	}
    	logger.end(f);
    }
    
    public void getChildren(String key, String scsEnvId, String dbaddress, GetChildrenResult getChildrenRes) {
    	final String function = "getChildren";
    	getChildrenMap.put(key, getChildrenRes);
    	logger.debug(function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);
    	
    	rtdb.getChildren(key, scsEnvId, dbaddress);
    }
    
    public void multiReadValue(String key, String scsEnvId, String [] dbaddress, MultiReadResult multiReadRes) {
    	final String function = "multiReadValue";
    	multiReadMap.put(key, multiReadRes);
    	
    	logger.debug(function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);
    	
    	rtdb.multiReadValueRequest(key, scsEnvId, dbaddress);
    }
    
    public void writeIntValue(String key, String scsEnvId, String address, int value) {
    	final String function = "writeIntValue";
    	
    	logger.debug(function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);

    	rtdb.writeIntValueRequest(key, scsEnvId, address, value);
    }
    
    public void writeFloatValue(String key, String scsEnvId, String address, float value) {
    	final String function = "writeFloatValue";
    	
    	logger.debug(function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);

    	rtdb.writeFloatValueRequest(key, scsEnvId, address, value);
    }
    
    public void writeStringValue(String key, String scsEnvId, String address, String value) {
    	final String function = "writeStringValue";
    	
    	logger.debug(function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);

    	rtdb.writeStringValueRequest(key, scsEnvId, address, value);
    }
}
